package de.ecotram.backend.simulation;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.PassengerTram;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Network;
import de.ecotram.backend.simulation.event.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * A class that simulates a tram network's tram movement, emitting events for progress reporting.
 */
public final class SimulationRunner {
	private static final int EMIT_TICKS_AFTER = 5;

	private final ExecutorService eventExecutor;
	private final Timer timer = new Timer();
	private final List<OrderedTask> taskQueue = new ArrayList<>();
	@Getter
	private final int timerInterval;
	@Getter
	private final Network network;
	@Getter
	private final Schedule schedule;
	@Getter
	private final ProgressReporter progressReporter;
	private boolean internalStopped;
	private long ticks;
	private long nextEmit = -1;
	@Getter
	private boolean isRunning;

	public SimulationRunner(Schedule schedule) {
		this(schedule, 100, 1);
	}

	public SimulationRunner(Schedule schedule, int timerInterval, int eventThreadPoolCount) {
		this.eventExecutor = Executors.newFixedThreadPool(eventThreadPoolCount);
		this.timerInterval = timerInterval;
		this.network = schedule.getNetwork();
		this.schedule = schedule;
		this.progressReporter = new ProgressReporter(this);
	}

	// must be synchronized on this
	public synchronized long getTicks() {
		return this.ticks;
	}

	// TODO(erik): doc this
	public synchronized ProgressReporter start() {
		if(this.isRunning)
			throw new IllegalStateException("Cannot start runner that is currently running.");

		if(this.ticks != 0)
			throw new IllegalStateException("Runner was stopped and not reset before starting.");

		timer.schedule(new RunnerStartUpTask(this), 0);

		return this.progressReporter;
	}

	public synchronized void stop() {
		if(!this.isRunning)
			throw new IllegalStateException("Cannot stop runner that is not currently running.");

		this.timer.schedule(new RunnerStoppingTask(this, null, true), 0);
	}

	private void runInternalIteration() {
		if(this.taskQueue.isEmpty()) {
			this.internalStopped = true;
			return;
		}

		List<OrderedTask> relevantTasks = this.taskQueue.stream()
				.filter(task -> task.dispatchTick <= this.ticks)
				.collect(Collectors.toList());

		this.taskQueue.removeAll(relevantTasks);

		relevantTasks.forEach(task -> task.getNextDispatch(progressReporter).ifPresent(this.taskQueue::add));

		if(this.ticks >= this.nextEmit) {
			eventExecutor.execute(() -> progressReporter.getRunnerTicks().invoke(RunnerTicksUpdatedArgs.builder().currentTicks(this.ticks).build()));
			this.nextEmit = this.ticks + EMIT_TICKS_AFTER;
		}

		this.ticks++;
	}

	/**
	 * A subtask that is used to emitting single tram events per iteration.
	 */
	@AllArgsConstructor
	private static final class OrderedTask {
		private final long dispatchTick;
		private final int tickInterval;
		private final int currentCount;
		private final LineSchedule.Entry entry;
		private final Connection connection;

		public Optional<OrderedTask> getNextDispatch(ProgressReporter progressReporter) {
			progressReporter.getRunner().eventExecutor.execute(() -> progressReporter.getTramStopped()
					.invoke(TramStoppedArgs.builder().passengerTram(this.entry.passengerTram()).connection(connection).build()));

			Optional<OrderedTask> output = this.currentCount >= this.entry.maxCount()
					? Optional.of(new OrderedTask(
					this.dispatchTick + this.tickInterval,
					this.tickInterval,
					this.currentCount + 1,
					this.entry,
					this.entry.passengerTram().nextStation()))
					: Optional.empty();

			output.ifPresent(orderedTask -> progressReporter.getRunner().eventExecutor.execute(() -> progressReporter.getTramStarted()
					.invoke(TramStartedArgs.builder().passengerTram(this.entry.passengerTram()).connection(orderedTask.connection).build())));

			return output;
		}
	}

	/**
	 * A task that is run on each internal simulation iteration.
	 */
	private static final class RunnerTimerTask extends TimerTask {
		private final SimulationRunner runner;

		private RunnerTimerTask(SimulationRunner runner) {
			this.runner = runner;
		}

		@Override
		public void run() {
			synchronized (this.runner) {
				try {
					this.runner.runInternalIteration();

					if(!this.runner.internalStopped)
						this.runner.timer.schedule(new RunnerTimerTask(this.runner), this.runner.timerInterval);
					else
						this.runner.timer.schedule(new RunnerStoppingTask(this.runner, null, false), 0);
				} catch (Exception ex) {
					this.runner.timer.schedule(new RunnerStoppingTask(this.runner, ex, false), 0);
				}
			}
		}
	}

	/**
	 * A task that is run on simulation startup.
	 */
	private static final class RunnerStartUpTask extends TimerTask {
		private final SimulationRunner runner;

		private RunnerStartUpTask(SimulationRunner runner) {
			this.runner = runner;
		}

		@Override
		public void run() {
			synchronized (this.runner) {
				for(Map.Entry<Line, LineSchedule> lineSchedule : this.runner.schedule.getLineSchedules().entrySet()) {
					for(Map.Entry<PassengerTram, LineSchedule.Entry> entry : lineSchedule.getValue().getTrams().entrySet()) {
						this.runner.taskQueue.add(new OrderedTask(
								entry.getValue().startingTime(),
								this.runner.timerInterval,
								entry.getValue().maxCount(),
								entry.getValue(),
								entry.getValue().passengerTram().nextStation())
						);
					}
				}

				this.runner.eventExecutor.execute(() -> runner.progressReporter.getRunnerStarted().invoke(new RunnerStartedArgs()));
				this.runner.timer.schedule(new RunnerTimerTask(this.runner), this.runner.timerInterval);

				this.runner.isRunning = true;
			}
		}
	}

	/**
	 * A task that is run on simulation shutdown.
	 */
	private static final class RunnerStoppingTask extends TimerTask {
		private final SimulationRunner runner;
		private final Exception exception;
		private final boolean external;

		private RunnerStoppingTask(SimulationRunner runner, Exception exception, boolean external) {
			this.runner = runner;
			this.exception = exception;
			this.external = external;
		}

		@Override
		public void run() {
			synchronized (this.runner) {
				this.runner.isRunning = false;

				if(this.exception != null) {
					this.runner.eventExecutor.execute(() -> this.runner.progressReporter
							.getRunnerStopped()
							.invoke(RunnerStoppedArgs.builder()
									.reason("An exception was thrown.")
									.exception(Optional.of(this.exception))
									.cause(RunnerStoppedArgs.Cause.EXCEPTION)
									.build()
							));
				} else if(!this.external) {
					this.runner.eventExecutor.execute(() -> this.runner.progressReporter
							.getRunnerStopped()
							.invoke(RunnerStoppedArgs.builder()
									.reason("The runner was stopped externally.")
									.cause(RunnerStoppedArgs.Cause.STOP_CALLED)
									.build()
							));
				} else {
					this.runner.eventExecutor.execute(() -> this.runner.progressReporter
							.getRunnerStopped()
							.invoke(RunnerStoppedArgs.builder()
									.reason("Runner finished.")
									.cause(RunnerStoppedArgs.Cause.FINISHED)
									.build()
							));
				}
			}
		}
	}
}
package de.ecotram.backend.simulation;

import de.ecotram.backend.entity.network.Network;
import de.ecotram.backend.simulation.event.RunnerStartedArgs;
import de.ecotram.backend.simulation.event.RunnerStoppedArgs;
import lombok.Getter;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SimulationRunner {
    private final ExecutorService executor;
    private final ExecutorService eventExecutor;

    private boolean runInternal;
    private long ticks;

    @Getter
    private final Network network;

    @Getter
    private final Schedule schedule;

    @Getter
    private final ProgressReporter progressReporter;

    @Getter
    private boolean isRunning;

    public SimulationRunner(Schedule schedule) {
        this.executor = Executors.newFixedThreadPool(1);
        this.eventExecutor = Executors.newFixedThreadPool(1);
        this.network = schedule.getNetwork();
        this.schedule = schedule;
        this.progressReporter = new ProgressReporter(this);
    }

    // must tbe synchronized on this
    synchronized long getTicks() {
        return this.ticks;
    }

    public synchronized ProgressReporter start() {
        if (this.isRunning)
            throw new IllegalStateException("Cannot start runner that is currently running.");

        if (this.ticks != 0)
            throw new IllegalStateException("Runner was stopped and not reset before starting.");

        this.isRunning = true;
        this.runInternal = true;

        this.executor.execute(this::runInternal);
        return this.progressReporter;
    }

    public synchronized void stop() {
        if (!this.isRunning)
            throw new IllegalStateException("Cannot stop runner that is not currently running.");

        this.runInternal = false;
    }

    private void runInternal() {
        Exception exception = null;

        eventExecutor.execute(() -> this.progressReporter.getRunnerStarted().invoke(new RunnerStartedArgs()));

        while (true) {
            synchronized (this) {
                if (!this.runInternal)
                    break;
            }

            try {
                // TODO(erik): actual simulation logic
                //  * dispatch trams to some tram runner
                //  * each task should calculate time to send event when it arrives at next station
                //  * register continuation hook for next task
                //  maybe use priority blocking queue to add/consume tasks that represent the above idea,
                //  to avoid mistakes, use ticks to measure new dispatch time so incorrectly dispatched events don't
                //  influence subsequent events
            } catch (Exception ex) {
                exception = ex;
                break;
            }

            synchronized (this) {
                this.ticks++;
            }
        }

        synchronized (this) {
            this.isRunning = false;
        }

        if (exception != null) {
            final Exception ex = exception;
            eventExecutor.execute(() -> this.progressReporter.getRunnerStopped().invoke(RunnerStoppedArgs.builder()
                    .reason("An exception was thrown.")
                    .exception(Optional.of(ex))
                    .cause(RunnerStoppedArgs.Cause.EXCEPTION)
                    .build()
            ));
        } else if (!this.runInternal) {
            eventExecutor.execute(() -> this.progressReporter.getRunnerStopped().invoke(RunnerStoppedArgs.builder()
                    .reason("The runner was stopped externally.")
                    .cause(RunnerStoppedArgs.Cause.STOP_CALLED)
                    .build()
            ));
        } else {
            eventExecutor.execute(() -> this.progressReporter.getRunnerStopped().invoke(RunnerStoppedArgs.builder()
                    .reason("Runner finished.")
                    .cause(RunnerStoppedArgs.Cause.FINISHED)
                    .build()
            ));
        }
    }
}
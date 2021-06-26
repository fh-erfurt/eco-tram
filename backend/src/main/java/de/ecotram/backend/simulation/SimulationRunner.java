package de.ecotram.backend.simulation;

import de.ecotram.backend.entity.network.Network;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class SimulationRunner {
    private final Executor mainExecutor;
    private final Executor taskExecutor;
    private final List<Runnable> tasks = new ArrayList<>(); // TODO: determine whether multiple tasks are needed
    private final Object taskLock = new Object();

    private final FutureTask<Object> mainTask;
    private boolean runInternal;
    private long ticks;

    @Getter
    private final Network network;

    @Getter
    private boolean isRunning;

    @Getter
    private DispatchPlan dispatchPlan;

    @Getter
    private ProgressReporter progressReporter;

    private SimulationRunner(Network network) {
        this.mainExecutor = Executors.newFixedThreadPool(1);
        this.taskExecutor = Executors.newFixedThreadPool(1); // TODO(erik): determine appropriate degree of parallelism
        this.mainTask = new FutureTask<>(this::runInternal);
        this.network = network;
    }

    public synchronized ProgressReporter start(DispatchPlan plan) {
        if (this.isRunning)
            throw new IllegalStateException("Cannot start runner that is currently running.");

        if (this.ticks != 0 || this.dispatchPlan != null || this.progressReporter != null)
            throw new IllegalStateException("Runner was stopped and not reset before starting.");

        this.isRunning = true;
        this.dispatchPlan = plan;
        this.progressReporter = new ProgressReporter();
        this.runInternal = true;

        this.mainExecutor.execute(this.mainTask);
        return this.progressReporter;
    }

    public synchronized void stop() throws ExecutionException, InterruptedException {
        if (!this.isRunning)
            throw new IllegalStateException("Cannot stop runner that is not currently running.");

        this.runInternal = false;
        this.mainTask.get();
    }

    public synchronized void stop(long timeout, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.isRunning)
            throw new IllegalStateException("Cannot stop runner that is not currently running.");

        this.runInternal = false;
        this.mainTask.get(timeout, timeUnit);
    }

    private Object runInternal() {
        while (this.runInternal) {
            // TODO(erik): let task executor run these in parallel to some degree
            // TODO(erik): what kind of tasks should be stored here, how can they simulate the network
            for (Runnable task : this.tasks) {
                task.run();
            }
            synchronized (this) {
                this.ticks++;
            }
        }
        synchronized (this) {
            this.isRunning = false;
        }
        return null;
    }

    // TODO(erik): builder, allow running once create new if running again
    public SimulationRunner forNetwork(Network network) {
        SimulationRunner runner = new SimulationRunner(network);
        // room for any non trivial set up
        return runner;
    }
}
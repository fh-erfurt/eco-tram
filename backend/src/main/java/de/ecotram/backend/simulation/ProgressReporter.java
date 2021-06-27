package de.ecotram.backend.simulation;

import de.ecotram.backend.event.Event;
import de.ecotram.backend.simulation.event.RunnerStartedArgs;
import de.ecotram.backend.simulation.event.RunnerStoppedArgs;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

// TODO(erik): a reporter used to safely check simulation status
public final class ProgressReporter {
    private final Object _lock = new Object();
    private final System.Logger logger;

    @Getter(value = AccessLevel.PROTECTED, onMethod_ = {@Synchronized(value = "_lock")})
    private final Event<RunnerStartedArgs> runnerStarted;

    @Getter(value = AccessLevel.PROTECTED, onMethod_ = {@Synchronized(value = "_lock")})
    private final Event<RunnerStoppedArgs> runnerStopped;

    @Getter
    private final SimulationRunner runner;

    @Getter(onMethod_ = {@Synchronized(value = "_lock")})
    @Setter(value = AccessLevel.PRIVATE, onMethod_ = {@Synchronized(value = "_lock")})
    private State state;

    public Event<RunnerStartedArgs>.Accessor onRunnerStarted() {
        return this.runnerStarted.getAccess();
    }

    public Event<RunnerStoppedArgs>.Accessor onRunnerStopped() {
        return this.runnerStopped.getAccess();
    }

    public ProgressReporter(System.Logger logger, SimulationRunner runner) {
        this.logger = logger;
        this.runner = runner;
        this.runnerStarted = new Event<>(ex -> this.logger.log(System.Logger.Level.ERROR, "Exception while executing runnerStarted.", ex));
        this.runnerStopped = new Event<>(ex -> this.logger.log(System.Logger.Level.ERROR, "Exception while executing runnerStopped.", ex));

        this.onRunnerStarted().add(e -> this.setState(State.RUNNING));
        this.onRunnerStopped().add(e -> this.setState(State.STOPPED));
    }

    public enum State {
        NOT_STARTED,
        RUNNING,
        STOPPED
    }
}
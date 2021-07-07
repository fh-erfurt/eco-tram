package de.ecotram.backend.simulation;

import de.ecotram.backend.event.Event;
import de.ecotram.backend.simulation.event.RunnerStartedArgs;
import de.ecotram.backend.simulation.event.RunnerStoppedArgs;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public final class ProgressReporter {
    private final Object _lock = new Object();

    @Getter(value = AccessLevel.PACKAGE, onMethod_ = {@Synchronized(value = "_lock")})
    private final Event<RunnerStartedArgs> runnerStarted;

    @Getter(value = AccessLevel.PACKAGE, onMethod_ = {@Synchronized(value = "_lock")})
    private final Event<RunnerStoppedArgs> runnerStopped;

    @Getter(value = AccessLevel.PACKAGE, onMethod_ = {@Synchronized(value = "_lock")})
    private final Event<RunnerStoppedArgs> tramStarted;

    @Getter(value = AccessLevel.PACKAGE, onMethod_ = {@Synchronized(value = "_lock")})
    private final Event<RunnerStoppedArgs> tramStopped;

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

    public Event<RunnerStoppedArgs>.Accessor onTramStarted() {
        return this.tramStarted.getAccess();
    }

    public Event<RunnerStoppedArgs>.Accessor onTramStopped() {
        return this.tramStopped.getAccess();
    }

    public ProgressReporter(SimulationRunner runner) {
        this.runner = runner;
        this.runnerStarted = new Event<>(ex -> this.log.log(Level.WARNING, "Exception while executing runnerStarted.", ex));
        this.runnerStopped = new Event<>(ex -> this.log.log(Level.WARNING, "Exception while executing runnerStopped.", ex));
        this.tramStarted = new Event<>(ex -> this.log.log(Level.WARNING, "Exception while executing tramStarted.", ex));
        this.tramStopped = new Event<>(ex -> this.log.log(Level.WARNING, "Exception while executing tramStopped.", ex));

        this.onRunnerStarted().add(e -> this.setState(State.RUNNING));
        this.onRunnerStopped().add(e -> this.setState(State.STOPPED));
    }

    public long getElapsedTicks() {
        return this.runner.getTicks();
    }

    public enum State {
        NOT_STARTED,
        RUNNING,
        STOPPED
    }
}
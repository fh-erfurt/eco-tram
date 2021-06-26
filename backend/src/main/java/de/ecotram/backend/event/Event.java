package de.ecotram.backend.event;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class Event<T extends EventArgs> {
    private final List<Callback<T>> subscribers = new ArrayList<>();
    private final ErrorHandler<T> errorHandler;

    @Getter
    private final Accessor access = new Accessor();

    public Event() {
        this.errorHandler = (e, args) -> {
        };
    }

    public Event(ErrorHandler<T> errorHandler) {
        this.errorHandler = errorHandler;
    }

    public Event(Consumer<T> errorHandler) {
        this.errorHandler = (e, args) -> errorHandler.accept(args);
    }

    public synchronized void invoke(T args) {
        for (Callback<T> subscriber : subscribers) {
            try {
                subscriber.OnEvent(args);
            } catch (Exception ex) {
                errorHandler.OnException(ex, args);
            }
        }
    }

    private synchronized void internalAdd(Callback<T> handler) {
        this.subscribers.add(handler);
    }

    private synchronized void internalRemove(Callback<T> handler) {
        this.subscribers.remove(handler);
    }

    // only expose this to consumers, not the event itself
    // see @test.java.de.ecotram.backend.event.EventTests for example
    public final class Accessor {
        public void add(Event.Callback<T> handler) {
            internalAdd(handler);
        }

        public void remove(Event.Callback<T> handler) {
            internalRemove(handler);
        }

        public ErrorHandler<T> getErrorHandler() {
            return errorHandler;
        }
    }

    @FunctionalInterface
    public interface Callback<T extends EventArgs> {
        void OnEvent(T args);
    }

    @FunctionalInterface
    public interface ErrorHandler<T extends EventArgs> {
        void OnException(Exception e, T args);
    }
}
package de.ecotram.backend.event;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public final class Event<T extends EventArgs> {
    private final List<Consumer<T>> consumers = new ArrayList<>();
    private final ErrorHandler<T> errorHandler;

    @Getter
    private final Accessor access = new Accessor();

    public Event() {
        this.errorHandler = null;
    }

    public Event(ErrorHandler<T> errorHandler) {
        this.errorHandler = errorHandler;
    }

    public Event(Consumer<T> errorHandler) {
        this.errorHandler = (e, args) -> errorHandler.accept(args);
    }

    public synchronized void invoke(T args) {
        for (Consumer<T> subscriber : consumers) {
            try {
                subscriber.accept(args);
            } catch (Exception ex) {
                if (errorHandler == null)
                    throw ex;

                errorHandler.OnException(ex, args);
            }
        }
    }

    private synchronized void internalAdd(Consumer<T> consumer) {
        this.consumers.add(consumer);
    }

    private synchronized void internalRemove(Consumer<T> consumer) {
        this.consumers.remove(consumer);
    }

    // only expose this to consumers, not the event itself
    // see @test.java.de.ecotram.backend.event.EventTests for example
    public final class Accessor {
        public void add(Consumer<T> consumer) {
            internalAdd(consumer);
        }

        public void remove(Consumer<T> consumer) {
            internalRemove(consumer);
        }

        public Optional<ErrorHandler<T>> getErrorHandler() {
            return errorHandler != null ? Optional.of(errorHandler) : Optional.empty();
        }
    }

    @FunctionalInterface
    public interface ErrorHandler<T extends EventArgs> {
        void OnException(Exception e, T args);
    }
}
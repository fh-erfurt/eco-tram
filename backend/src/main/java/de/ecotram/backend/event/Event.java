package de.ecotram.backend.event;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A simple synchronous event used for peer-to-peer events.
 *
 * @param <T> The type of EventArgs.
 */
public final class Event<T extends EventArgs> {
    private final List<Consumer<T>> consumers = new ArrayList<>();
    private final ErrorHandler<T> errorHandler;

    /**
     * The public accessor of this event.
     */
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

    /**
     * Invokes all subscribed consumers synchronously and sequentially.
     *
     * @param args The EventArgs to pass to each consumer, each consumer will get a reference to the same EventArgs.
     */
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

    /**
     * An accessor to expose to consumers of the event for subscription and un-subscription of consumers.
     * This class only exposes methods for consumers, not invokers of events.
     */
    public final class Accessor {
        public void add(Consumer<T> consumer) {
            internalAdd(consumer);
        }

        public void remove(Consumer<T> consumer) {
            internalRemove(consumer);
        }

        public Optional<ErrorHandler<T>> getErrorHandler() {
            return Optional.ofNullable(errorHandler);
        }
    }

    /**
     * An error handler to execute on exception caused by an event subscriber.
     *
     * @param <T> The type of EventArgs.
     */
    @FunctionalInterface
    public interface ErrorHandler<T extends EventArgs> {
        void OnException(Exception e, T args);
    }
}
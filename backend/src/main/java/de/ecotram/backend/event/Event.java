package de.ecotram.backend.event;

import java.util.ArrayList;
import java.util.List;

public final class Event<T extends EventArgs> {
    private final List<Callback<T>> _subscribers = new ArrayList<>();

    public synchronized void add(Callback<T> handler) {
        _subscribers.add(handler);
    }

    public synchronized void remove(Callback<T> handler) {
        _subscribers.remove(handler);
    }

    public synchronized void invoke(T args) {
        for (Callback<T> subscriber : _subscribers) {
            subscriber.OnEvent(args);
        }
    }

    @FunctionalInterface
    public interface Callback<T extends EventArgs> {
        void OnEvent(T args);
    }
}
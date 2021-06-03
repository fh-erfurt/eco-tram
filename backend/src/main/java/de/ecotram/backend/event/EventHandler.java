package de.ecotram.backend.event;

public final class EventHandler<T extends EventArgs> {
    private final Event<T> _boundEvent;

    public EventHandler(Event<T> boundEvent) {
        _boundEvent = boundEvent;
    }

    public void add(Event.Callback<T> handler) {
        _boundEvent.add(handler);
    }

    public void remove(Event.Callback<T> handler) {
        _boundEvent.remove(handler);
    }
}
package de.fhe.ai.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that enables event based communication between models the {@link TrafficManager}
 */
public class EventManager {
    private final Map<Object, EventEntity> boundEventEntities = new HashMap<>();

    //#region SingletonPattern
    private static EventManager INSTANCE;

    public static EventManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new EventManager();
        return INSTANCE;
    }

    private EventManager() {
    }
    //#endregion

    //#region Getters & Setters
    /**
     * Gets or creates an event entity for the given object
     * 
     * @param object the object to bind an event entity to
     * 
     * @return the event entity bound to the given object
     */
    public EventEntity getEventEntity(Object object) {
        if (boundEventEntities.containsKey(object))
            return boundEventEntities.get(object);

        EventEntity eventEntity = new EventEntity(object);
        boundEventEntities.put(object, eventEntity);

        return eventEntity;
    }

    // removeEventEntityByObject(Object object) should be an overload for this
    public void removeEventEntity(EventEntity eventEntity) {
        if (boundEventEntities.entrySet().removeIf(entry -> entry.getValue().equals(eventEntity)))
            eventEntity.removeAllListeners();
    }

    public void removeEventEntityByObject(Object object) {
        if (boundEventEntities.containsKey(object))
            this.removeEventEntity(boundEventEntities.get(object));
    }
    //#endregion

    /**
     * Emits the given event with the given data to all entities of type target
     * 
     * @param target the type of the entities that shold recieve the event
     * @param event  the event to emit
     * @param data   the data to send with the event
     */
    public void emitToAllEntitiesOfType(Class<?> target, String event, Object data) {
        boundEventEntities
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey().getClass().equals(target))
            .forEach(entry -> entry.getValue().emit(event, data));
    }

    /**
     * Same as {@link #emitToAllEntitiesOfType(Class<?> target, String event, Object data)} but emits the given event with no data
     * 
     * @param target the type of the entities that shold recieve the event
     * @param event  the event to emit
     */
    public void emitToAllEntitiesOfType(Class<?> target, String event) {
        boundEventEntities
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey().getClass().equals(target))
            .forEach(entry -> entry.getValue().emit(event, null));
    }

    /**
     * A class that represents an event entity bound to a runtime-object which manages event callbacks for said object
     */
    public static class EventEntity {
        private final Object entity;
        private final Map<String, ArrayList<EventCallback>> callbacks = new HashMap<>();

        /**
         * Initializes a new EventEntity
         * 
         * @param entity the object to bind this event entity to, must be non null
         * 
         * @exception IllegalArgumentException if the given entity is null
         */
        public EventEntity(Object entity) {
            if (entity == null)
                throw new IllegalArgumentException("Entity of `" + this + "` cannot be null.");

            this.entity = entity;
        }

        //#region Getters & Setters
        public Object getEntity() { return entity; }
        //#endregion

        /**
         * Adds a callback to execute when the given event is recieved by this entity
         * 
         * @param event         the event to listen for, can be null or empty which registers this listener for all events
         * @param eventCallback the callback to execute on receival of the event
         */
        public void addListener(String event, EventCallback eventCallback) {
            String targetEvent = event == null ? "" : event;

            ArrayList<EventCallback> eventCallbacks = null;
            if (callbacks.containsKey(targetEvent))
                eventCallbacks = callbacks.get(targetEvent);
            else {
                eventCallbacks = new ArrayList<>();
                callbacks.put(targetEvent, eventCallbacks);
            }
            eventCallbacks.add(eventCallback);
        }

        /**
         * Same as {@link #addListener(String event, EventCallback eventCallback)} but registers this listener for all events
         * 
         * @param eventCallback the callback to execute on receival of all events
         */
        public void addListener(EventCallback eventCallback) { this.addListener(null, eventCallback); }

        /**
         * Removes the given callback for the given event if they exist
         * 
         * @param event         the event to remove the callback for, can be null or empty to remove a callback that is registered for all events
         * @param eventCallback the callback to remove
         */
        public void removeListener(String event, EventCallback eventCallback) {
            String targetEvent = event == null ? "" : event;

            if (callbacks.containsKey(targetEvent)) {
                ArrayList<EventCallback> eventCallbacks = callbacks.get(targetEvent);
                eventCallbacks.remove(eventCallback);
            }
        }

        /**
         * Same as {@link #removeListener(String event, EventCallback eventCallback)} but removes this listener for all events
         * 
         * @param eventCallback the callback taht is registered for all events
         */
        public void removeListener(EventCallback eventCallback) { this.removeListener(null, eventCallback); }

        /**
         * Removes all callbacks that registered for the given event
         * @param event the event to remove all callbacks for
         */
        public void removeAllListeners(String event) { callbacks.remove(event); }

        /**
         * Removes all callbacks for all events
         */
        public void removeAllListeners() { callbacks.clear(); }

        /**
         * Checks whether a callback is registered for any event
         * 
         * @param eventCallback the callback to check for
         * 
         * @return {@code true} if the callback is registered for any event; otherwise {@code false}
         */
        public boolean isListenerRegistered(EventCallback eventCallback) { return callbacks.values().stream().anyMatch(list -> list.contains(eventCallback)); }

        /**
         * Emits the given event with the given data to all registered listeners and executes their callbacks
         * 
         * @param event the event to emit
         * @param data  the data to send with the event
         */
        public void emit(String event, Object data) {
            if (callbacks.containsKey(event))
                callbacks.get(event).forEach(callback -> callback.onEvent(entity, event, data));

            // Emits event again to listeners receiving all events
            if (!event.equals("") && callbacks.containsKey(""))
                callbacks.get("").forEach(callback -> callback.onEvent(entity, event, data));
        }

        /**
         * Emits the given event with no data to all registered listeners and executes their callbacks
         * 
         * @param event the event to emit
         */
        public void emit(String event) { this.emit(event, null); }
    }

    /**
     * An interface that exposes the callback action for event listeners
     */
    public interface EventCallback {
        /**
         * The callback action that is executed on event receival
         * 
         * @param entity the entity of this callback
         * @param event  the event that is received
         * @param data   the data that is sent to the event
         */
        void onEvent(Object entity, String event, Object data);
    }
}
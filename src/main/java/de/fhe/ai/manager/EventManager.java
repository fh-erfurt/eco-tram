package de.fhe.ai.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager
{
    private static EventManager INSTANCE; //Instance of SingletonPattern
    private final Map<Object,EventEntity> boundEventEntities = new HashMap<>();

    //SingletonPattern
    public static EventManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new EventManager();
        return INSTANCE;
    }

    //Constructor
    private EventManager() { }


    public EventEntity getEventEntity(Object object) {
        if(boundEventEntities.containsKey(object))
            return boundEventEntities.get(object);

        EventEntity eventEntity = new EventEntity(object);
        boundEventEntities.put(object,eventEntity);

        return eventEntity;
    }

    public void removeEventEntity(EventEntity eventEntity) {
        if(boundEventEntities.entrySet().removeIf(entry -> entry.getValue().equals(eventEntity)))
            eventEntity.removeAllListeners();
    }

    public void removeEventEntityByObject(Object object) {
        if(boundEventEntities.containsKey(object))
            this.removeEventEntity(boundEventEntities.get(object));
    }

    public void emitToAllEntitiesOfType(Class<?> target, String event, Object data) {
        boundEventEntities
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getClass().equals(target))
                .forEach(entry -> entry.getValue().emit(event,data));
    }

    public static class EventEntity {

        private final Object entity;
        private final Map<String, ArrayList<EventCallback>> callbacks = new HashMap<>();

        public EventEntity(Object entity) {
            this.entity = entity;
        }

        public Object getEntity() {
            return entity;
        }

        public void addListener(String event, EventCallback eventCallback) {
            String targetEvent = event == null ? "" : event;

            ArrayList<EventCallback> eventCallbacks = null;
            if(callbacks.containsKey(targetEvent))
                eventCallbacks = callbacks.get(targetEvent);
            else {
                eventCallbacks = new ArrayList<>();
                callbacks.put(targetEvent, eventCallbacks);
            }
            eventCallbacks.add(eventCallback);
        }

        public void addListener(EventCallback eventCallback) {
            this.addListener(null,eventCallback);
        }

        public void removeListener(String event, EventCallback eventCallback) {
            String targetEvent = event == null ? "" : event;

            if(callbacks.containsKey(targetEvent)) {
                ArrayList<EventCallback> eventCallbacks = callbacks.get(targetEvent);
                eventCallbacks.remove(eventCallback);
            }
        }

        public void removeListener(EventCallback eventCallback) {
            this.removeListener(null,eventCallback);
        }

        public void removeAllListeners(String event) {
            callbacks.remove(event);
        }

        public void removeAllListeners() {
            callbacks.clear();
        }

        public boolean isListenerRegistered(EventCallback eventCallback) {
            return callbacks
                    .values()
                    .stream()
                    .anyMatch(list -> list.contains(eventCallback));
        }

        public void emit(String event, Object data) {
            if(callbacks.containsKey(event))
                callbacks.get(event).forEach(callback -> callback.onEvent(entity,event,data));

            //Emits event again to listeners receiving all events
            if(!event.equals("") && callbacks.containsKey(""))
                callbacks.get("").forEach(callback -> callback.onEvent(entity,event,data));
        }

        public void emit(String event) {
            this.emit(event,null);
        }
    }

    public interface EventCallback {
        void onEvent(Object entity, String event, Object data);
    }
}
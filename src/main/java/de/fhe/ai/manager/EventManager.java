package de.fhe.ai.manager;

public class EventManager {
    private static EventManager INSTANCE;

    public static EventManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new EventManager();
        return INSTANCE;
    }

    private EventManager() {

    }
}
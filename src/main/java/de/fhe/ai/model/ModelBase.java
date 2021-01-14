package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;

/**
 * Provides a base class for all models
 */
public abstract class ModelBase {
    private final int id;
    protected final EventManager eventManager;

    /**
     * Initalizes a new ModelBase
     * 
     * @param id           the internal id of the tram
     * @param eventManager the eventManager used to communicate with the
     * @throws IllegalArgumentException if invalid parameters are passed
     */
    public ModelBase(int id, EventManager eventManager) {
        if (eventManager == null) {
            throw new IllegalArgumentException("TramType of tram `" + this + "` cannot be null.");
        }

        this.id = id;
        this.eventManager = eventManager;
    }

    public int getId() {
        return id;
    }

    protected EventManager getEventManager() {
        return eventManager;
    }
}
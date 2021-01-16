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
     * @param eventManager the eventManager used to communicate with the TrafficManager, must be non-null
     * 
     * @throws IllegalArgumentException if invalid arguments are passed
     */
    public ModelBase(int id, EventManager eventManager) {
        if (eventManager == null)
            throw new IllegalArgumentException("TramType of `" + this + "` cannot be null.");

        this.id = id;
        this.eventManager = eventManager;
    }

    //#region Getters & Setters
    /**
     * @return the internal id of this model
     */
    public int getId() { return id; }
    
    /**
     * @return the EventManager used for this model
     */
    protected EventManager getEventManager() { return eventManager; }
    //#endregion
}
package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;

/**
 * Provides a base class for all models
 */
public abstract class ModelBase {
    private final int          id;           // internal id given by the model factory for easy comparison and identification
    private final EventManager eventManager; // communication interface with the traffic manager that controls this model

    /**
     * Initalizes a new ModelBase
     * 
     * @param id           the internal id of the model
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
    public int getId() { return id; }

    public EventManager getEventManager() { return eventManager; }
    //#endregion
}
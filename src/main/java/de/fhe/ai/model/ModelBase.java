package de.fhe.ai.model;

import de.fhe.ai.Utils;
import de.fhe.ai.manager.EventManager;

/**
 * Provides a base class for all models
 */
public abstract class ModelBase {

    private final int id;
    protected final EventManager eventManager;

    public ModelBase(int id, EventManager eventManager) {
        this.id = id;
        this.eventManager = eventManager;
    }

    public int getId() {
        return id;
    }

    protected EventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public String toString() {
        // TODO: only return public fields in non-debug enviroments
        // either add gloabl debug/testing variable and check on that
        // or implement override in each child class
        return Utils.getVerboseModelRepresentation(this, 0);
    }
}
package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;

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

    protected String getString(ModelBase modelBase) {
        // TODO

        return "";
    }
}
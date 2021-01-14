package de.fhe.ai.model;

import java.util.ArrayList;

import de.fhe.ai.manager.EventManager;

/**
 * A class that represents a {@link Line} that only exists temporarily, such as
 * a return path for a Tram should it be stopped mid-traversal
 */
public class TemporaryLine extends Line {

    /**
     * Initialises a new TemporaryLine
     * 
     * @param id           the internal id of the tram
     * @param eventManager the eventManager used to communicate with the
     * @param route        the route of this line
     */
    public TemporaryLine(int id, EventManager eventManager, ArrayList<Traversable> route) {
        super(id, eventManager, "Temporary Line", route);
    }
}
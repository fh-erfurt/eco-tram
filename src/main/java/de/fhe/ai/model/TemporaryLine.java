package de.fhe.ai.model;

import java.util.Collection;

import de.fhe.ai.manager.EventManager;

/**
 * A class that represents a {@link Line} that only exists temporarily, such as a return path for a Tram should it be stopped mid-traversal
 */
public class TemporaryLine extends Line {

    /**
     * Initialises a new TemporaryLine
     * 
     * @param id           the internal id of the temporary line
     * @param eventManager the eventManager used to communicate with the TrafficManager, must be non-null
     * @param route        the route of this line, must be non-null
     * 
     * @throws IllegalArgumentException if invalid arguments are passed
     */
    public TemporaryLine(int id, EventManager eventManager, Collection<Traversable> route) {
        super(id, eventManager, "", route);
    }
}
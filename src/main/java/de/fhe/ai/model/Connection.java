package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;

/**
 * A class that represents a one-directional connecton between two {@link Station}s
 */
public class Connection extends Traversable {
    private int           maximumWeight;
    private final Station sourceStation;
    private final Station destinationStation;

    /**
     * Initializes the connection class instance and sets the default parameters
     * 
     * @param id                 the internal id of the traversable
     * @param eventManager       the eventManager used to communicate with the TrafficManager, must be non-null
     * @param sourceStation      the source station, must be non-null
     * @param destinationStation the destination station, must be non-null
     * @param length             the length of the traversable in km, must be above 0
     * @param maxWeight          the maximum allowed weight of a traverser, must be above 0
     * @param trafficFactor      the traversion factor, must be between 0 and 1.0f
     * 
     * @throws IllegalArgumentException if invalid arguments are passed
     */
    public Connection(int id, EventManager eventManager, Station sourceStation, Station destinationStation, float length, int maxWeight, int trafficFactor) {
        super(id, eventManager, length, maxWeight, trafficFactor);

        if (sourceStation == null)
            throw new IllegalArgumentException("SourceStation of `" + this + "` cannot to be null.");
        if (destinationStation == null)
            throw new IllegalArgumentException("DestinationStation of `" + this + "` cannot to be null.");

        this.maximumWeight = maxWeight;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
    }

    //#region Getters & Setters
    public int getMaximumWeight() { return maximumWeight; }
    public void setMaximumWeight(int maximumWeight) { this.maximumWeight = maximumWeight; }

    public Station getSourceStation() { return sourceStation; }

    public Station getDestinationStation() { return destinationStation; }
    //#endregion
}
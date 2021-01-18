package de.fhe.ai.model;

import de.fhe.ai.manager.*;

/**
 * A class that represents a one-directional connecton between two {@link Station}s
 */
public class Connection extends Traversable {
    private final Station sourceStation;
    private final Station destinationStation;

    /**
     * Initializes a new Connection
     * 
     * @param id                 the internal id of the connection
     * @param eventManager       the eventManager used to communicate with the TrafficManager, must be non-null
     * @param sourceStation      the source station, must be non-null
     * @param destinationStation the destination station, must be non-null
     * @param length             the length of the connection in km, must be above 0
     * @param maxWeight          the maximum allowed weight of a traverser, must be above 0
     * @param trafficFactor      the traversion factor, must be between 0 and 1.0f
     * 
     * @throws IllegalArgumentException if invalid arguments are passed
     */
    public Connection(int id, EventManager eventManager, TrafficManager trafficManager, Station sourceStation, Station destinationStation, float length, int maxWeight, float trafficFactor) {
        super(id, eventManager, trafficManager, length, maxWeight, trafficFactor);

        if (sourceStation == null)
            throw new IllegalArgumentException("SourceStation of `" + this + "` cannot to be null.");
        if (destinationStation == null)
            throw new IllegalArgumentException("DestinationStation of `" + this + "` cannot to be null.");

        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
    }

    //#region Getters & Setters
    public Station getSourceStation() { return sourceStation; }

    public Station getDestinationStation() { return destinationStation; }
    //#endregion

    /**
     * Checks whether a given connection is a reverse of this connection
     * 
     * @param otherConnection the other connection to check for
     * 
     * @return {@code true} if the given connecion is a reverse of this connection; otherwise {@code false}
     */
    public boolean isReverse(Connection otherConnection) {
        if (this.getDestinationStation() == otherConnection.getSourceStation()
         && this.getSourceStation() == otherConnection.getDestinationStation())
            return true;

        return false;
    }
}
package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;

import java.util.HashSet;
import java.util.Set;

// TODO: define waitingTime dimension and add factor to accomodate the conversion from said dimension to hours
// to change:
//     description of parameter in constructor
//     conversion from waitingTime in getTraversionTime(), waitingTimecurrently assumed to be given in hours

/**
 * A class that represents a Station for {@link Tram}s to stop at and exchange passengers
 */
public class Station extends Traversable {
    private final String    name;
    private final int       maxPassengers;
    private int             currentPassengers;
    private long            waitingTime;
    private Set<Connection> adjacentConnections = new HashSet<>(); // set of connections that lead form this Station to another station (one-directional)

    /**
     * Initializes a new Station
     * 
     * @param id               the internal id of the station
     * @param eventManager     the eventManager used to communicate with the TrafficManager, must be non-null
     * @param name             the name of the station for public use, must be non-null and not empty
     * @param waitingTime      the waiting time of this conenction in ???
     * @param maxPassengers    the maximum amount of passengers that can be present on this station
     * @param length           the length of this station in km
     * @param maxWeight        the maximum allowed weight of a traverser, must be above positive or 0
     * @param trafficFactor    the traversion factor, must be between 0 and 1.0f
     * 
     * @throws IllegalArgumentException if invalid arguments are passed
     */
    public Station(int id, EventManager eventManager, String name, long waitingTime, int maxPassengers, float length, int maxWeight, float trafficFactor) {
        super(id, eventManager, length, maxWeight, trafficFactor);

        if (name == null || name == "")
            throw new IllegalArgumentException("Name of `" + this + "` cannot be null or empty.");

        this.name = name;
        this.waitingTime = waitingTime;
        this.maxPassengers = maxPassengers;
    }

    //#region Getters & Setters
    public String getName() { return name; }

    public Set<Connection> getAdjacentConnections() { return adjacentConnections; }
    public void setAdjacentConnections(Set<Connection> adjacentConnections) { this.adjacentConnections = adjacentConnections; }
    public void addAdjacentConnection(Connection adjacentConnection) { this.adjacentConnections.add(adjacentConnection); }
    public void removeAdjacentConnection(Connection adjacentConnection) { this.adjacentConnections.remove(adjacentConnection); }

    public long getWaitingTime() { return waitingTime; }
    public void setWaitingTime(long waitingTime) { this.waitingTime = waitingTime; }

    public int getMaxPassengers() { return maxPassengers; }

    public int getCurrentPassengers() { return currentPassengers; }

    /**
     * Attemps to set the current amount of passengers to the given value
     * 
     * @param currentPassengers the passengers to set the current station's amount to
     * 
     * @exception IllegalArgumentException if the given amount of passengers exceeds the allowed maximum passengers
     */
    public void setCurrentPassengers(int currentPassengers) {
        if (this.maxPassengers < currentPassengers)
            throw new IllegalArgumentException("Passengers of `" + this + "` cannot be larger than max passengers.");

        this.currentPassengers = currentPassengers;
    }
    //#endregion

    @Override
    public float getTraversionTime(Tram tram) { return (this.getTrafficFactor() * (this.getLength() / tram.getSpeed()) + /* factor 1 */ this.getWaitingTime()); }
}
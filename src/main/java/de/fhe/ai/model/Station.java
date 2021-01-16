package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;

import java.util.HashSet;
import java.util.Set;

/**
 * A class that represents a Station for {@link Tram}s to stop at and exchange passengers
 */
public class Station extends Traversable {
    private final String name;
    private long waitingTime;
    private final int maxPassengers;
    private int actualPassengers;
    private Set<Connection> adjacentConnections = new HashSet<>();

    /**
     * Initializes a new Station
     * @param id               the internal id of the tram
     * @param eventManager     the eventManager used to communicate with the TrafficManager, must be non-null
     * @param name             the name of the station for public use, must be non-null and not empty
     * @param waitingTime      the waiting time of this conenction
     * @param maxPassengers    the maximum amount of passengers that can be present on this station
     * @param actualPassengers the actual amount of passengers that are present on this station, must be lower than or equal to maxPassengers
     * @param length           the length of this station in km
     * @param maxWeight        the maximum allowed weight of a traverser, must be above positive or 0
     * @param trafficFactor    the traversion factor, must be between 0 and 1.0f
     * 
     * @throws IllegalArgumentException if invalid arguments are passed
     */
    public Station(int id, EventManager eventManager, String name, long waitingTime, int maxPassengers, int actualPassengers, float length, int maxWeight, int trafficFactor) {
        super(id, eventManager, length, maxWeight, trafficFactor);

        if (name == null || name == "")
            throw new IllegalArgumentException("Name of `" + this + "` cannot be null or empty.");

        if (maxPassengers < actualPassengers)
            throw new IllegalArgumentException("Passengers of `" + this + "` cannot be larger than max passengers.");

        this.name = name;
        this.waitingTime = waitingTime;
        this.maxPassengers = maxPassengers;
        this.actualPassengers = actualPassengers;
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

    public int getActualPassengers() { return actualPassengers; }

    /**
     * Attemps to set the current amount of passengers to the given value
     * 
     * @param actualPassengers the passengers to set the current station's amount to
     * 
     * @exception IllegalArgumentException if the given amount of passengers exceeds the allowed maximum passengers
     */
    public void setActualPassengers(int actualPassengers) {
        if (this.maxPassengers < actualPassengers)
            throw new IllegalArgumentException("Passengers of `" + this + "` cannot be larger than max passengers.");

        this.actualPassengers = actualPassengers;
    }
    //#endregion

    @Override
    public float getTraversionTime(Tram tram) { return (this.getTrafficFactor() * (this.getLength() / tram.getSpeed()) + this.getWaitingTime()); }
}
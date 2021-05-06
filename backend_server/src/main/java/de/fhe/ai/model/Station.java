package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;
import de.fhe.ai.manager.TrafficManager;

import java.util.HashSet;
import java.util.Set;

/**
 * A class that represents a Station for {@link Tram}s to stop at and exchange passengers
 */
public class Station extends Traversable {
    private final String name;
    private final int maxPassengers;
    private final Set<Connection> adjacentConnections = new HashSet<>(); // set of connections that lead form this Station to another station (one-directional)
    private int currentPassengers;
    private float waitingTime;

    /**
     * Initializes a new Station
     *
     * @param id            the internal id of the station
     * @param eventManager  the eventManager used to communicate with the TrafficManager, must be non-null
     * @param name          the name of the station for public use, must be non-null and not empty
     * @param waitingTime   the waiting time of this conenction in hours
     * @param maxPassengers the maximum amount of passengers that can be present on this station
     * @param length        the length of this station in km
     * @param maxWeight     the maximum allowed weight of a traverser, must be above positive or 0
     * @param trafficFactor the traversion factor, must be between 0 and 1.0f
     * @throws IllegalArgumentException if invalid arguments are passed
     */
    public Station(int id, EventManager eventManager, TrafficManager trafficManager, String name, float waitingTime, int maxPassengers, float length, int maxWeight, float trafficFactor) {
        super(id, eventManager, trafficManager, length, maxWeight, trafficFactor);

        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name of `" + this + "` cannot be null or empty.");

        this.name = name;
        this.waitingTime = waitingTime;
        this.maxPassengers = maxPassengers;
    }

    public String getName() {
        return name;
    }

    public Set<Connection> getAdjacentConnections() {
        return adjacentConnections;
    }

    public void addAdjacentConnection(Connection adjacentConnection) {
        this.adjacentConnections.add(adjacentConnection);
    }

    public void removeAdjacentConnection(Connection adjacentConnection) {
        this.adjacentConnections.remove(adjacentConnection);
    }

    public float getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public int getCurrentPassengers() {
        return currentPassengers;
    }

    /**
     * Attempts to set the current amount of passengers to the given number
     *
     * @param passengers the amount to set the current amount of passengers to, must be between 0 and maxPassengers
     * @throws IllegalArgumentException if the given amount not between 0 and maxPassengers
     */
    public void setCurrentPassengers(int passengers) {
        if (passengers < 0)
            throw new IllegalArgumentException("CurrentPassengers of `" + this + "` must be between 0 and `" + this.maxPassengers + "`.");

        this.currentPassengers = passengers;
    }

    /**
     * Attempts to add the given number of passengers to the given station
     *
     * @param passengers the amount of passengers to add to the station
     * @throws IllegalArgumentException if it is attempted to add passengers past the maximum allowed passengers
     */
    public void addPassengers(int passengers) {
        if (this.currentPassengers + passengers > maxPassengers)
            throw new IllegalStateException("Cannot add more passengers to `" + this + "` than max allowed passengers.");

        this.currentPassengers += passengers;

        if (this.currentPassengers == this.maxPassengers)
            this.getEventManager().getEventEntity(this.getTrafficManager()).emit("STATION_MAXIMUM_PASSENGERS_REACHED");
    }

    /**
     * Attempts to remove the given number of passengers from the given station
     *
     * @param passengers the amount of passengers to remove from the station
     * @throws IllegalArgumentException if it is attempted to remove more passengers than there are in the station
     */
    public void removePassengers(int passengers) {
        if (this.currentPassengers - passengers < 0)
            throw new IllegalStateException("Cannot remove more passengers from `" + this + "` than currently on the station.");

        this.currentPassengers -= passengers;

        if (this.currentPassengers == 0)
            this.getEventManager().getEventEntity(this.getTrafficManager()).emit("STATION_MINIMUM_PASSENGERS_REACHED");
    }

    @Override
    public float getTraversionTime(Tram tram) {
        return (this.getTrafficFactor() * (this.getLength() / tram.getSpeed()) + this.getWaitingTime());
    }
}
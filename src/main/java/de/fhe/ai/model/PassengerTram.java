package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;

/**
 * A cass that represents tram for passenger transport
 */
public class PassengerTram extends Tram
{
    private final int maxPassengers;
    private int passengers;

    /**
     * Initializes a new passenger Tram
     * 
     * @param id            the internal id of the tram
     * @param eventManager  the eventManager used to communicate with the
     *                      TrafficManager, must be non-null
     * @param maxPassengers the maximum capacity of passengers of the tram, must be
     *                      a positive integer
     * @param weight        the total weight of the tram excluding passengers, must
     *                      be a positive integer
     * @param speed         the speed the tram will move at, must be a positive
     *                      integer
     * @param tramType      the type identifier of the tram, must be non-null
     * @throws IllegalArgumentException if invalid parameters are passed
     */
    public PassengerTram(int id, EventManager eventManager, int maxPassengers, int weight, int speed, String tramType) {
        super(id, eventManager, weight, speed, tramType);

        if (maxPassengers < 0)
            throw new IllegalArgumentException("MaxPassengers of tram `" + this + "` cannot to be negative.");
        this.maxPassengers = maxPassengers;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public int getPassengers() {
        return passengers;
    }

    /**
     * Attempts to add the given number of passengers to the tram
     * 
     * @param passengers
     * @throws IllegalStateException    if it is attempted to add passengers while
     *                                  the tram is on a connection
     * @throws IllegalArgumentException if it is attempted to add passengers past
     *                                  the maximum allowed number
     */
    public void addPassengers(int passengers) {
        if (this.isOnConnection())
            throw new IllegalStateException("Cannot add passengers while on a connection.");
        if (this.passengers + passengers > maxPassengers)
            throw new IllegalArgumentException("Cannot add more than " + (maxPassengers - this.passengers) + " passengers, tried to add " + passengers + " passengers.");
        this.passengers += passengers;
    }

    /**
     * Attempts to remove the given number of passengers from the tram
     * 
     * @param passengers
     * @throws IllegalStateException    if it is attempted to remove passengers
     *                                  while the tram is on a connection
     * @throws IllegalArgumentException if it is attempted to remove more passengers
     *                                  than there are in the tram
     */
    public void removePassengers(int passengers) {
        if (this.isOnConnection())
            throw new IllegalStateException("Cannot remove passengers while on a connection.");
        if (this.passengers - passengers < 0)
            throw new IllegalArgumentException("Cannot remove more than " + this.passengers + " passengers, tried to remove " + passengers + " passengers.");
        this.passengers -= passengers;
    }
}
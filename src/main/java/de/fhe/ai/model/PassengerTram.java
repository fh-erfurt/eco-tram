package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;
import de.fhe.ai.manager.TrafficManager;

/**
 * A class that represents {@link Tram} for passenger transport
 */
public class PassengerTram extends Tram {
    private final int maxPassengers;
    private int passengers;

    /**
     * Initializes a new passenger Tram
     * 
     * @param id            the internal id of the tram
     * @param eventManager  the eventManager used to communicate with the TrafficManager, must be non-null
     * @param maxPassengers the maximum capacity of passengers of the tram, must be a positive integer
     * @param weight        the total weight of the tram excluding passengers in kg, must be a positive integer
     * @param speed         the speed the tram will move at in km/h, must be a positive integer
     * @param tramType      the type identifier of the tram, must be non-null and not empty
     * 
     * @throws IllegalArgumentException if invalid parameters are passed
     */
    public PassengerTram(int id, EventManager eventManager, int maxPassengers, int weight, int speed, String tramType) {
        super(id, eventManager, weight, speed, tramType);

        if (maxPassengers < 0)
            throw new IllegalArgumentException("MaxPassengers of `" + this + "` cannot to be negative.");

        this.maxPassengers = maxPassengers;
    }

    //#region Getters & Setters
    public int getMaxPassengers() { return maxPassengers; }
    public int getPassengers() { return passengers; }

    /**
     * Attempts to add the given number of passengers to the given tram
     * 
     * @param passengers the amount of passengers to add to the tram
     * 
     * @throws IllegalStateException    if it is attempted to add passengers while the tram is on a connection
     * @throws IllegalArgumentException if it is attempted to add passengers past the maximum allowed passengers
     */
    public void addPassengers(int passengers) {
        if (this.isOnConnection())
        throw new IllegalStateException("Cannot add passengers to `" + this + "` while on connection.");
        if (this.passengers + passengers > maxPassengers)
        throw new IllegalStateException("Cannot add more passengers to `" + this + "` than max allowed passengers.");
        
        this.passengers += passengers;
        
        if (this.passengers == this.maxPassengers)
        this.eventManager.getEventEntity(TrafficManager.getInstance()).emit("MAXIMUM_PASSENGERS_REACHED", this);
    }

    /**
     * Attempts to remove the given number of passengers from the given tram
     * 
     * @param passengers the amount of passengers to remove from the tram
     * 
     * @throws IllegalStateException    if it is attempted to remove passengers while the tram is on a connection
     * @throws IllegalArgumentException if it is attempted to remove more passengers than there are in the tram
     */
    public void removePassengers(int passengers) {
        if (this.isOnConnection())
        throw new IllegalStateException("Cannot remove passengers from `" + this + "` while on connection.");
        if (this.passengers - passengers < 0)
        throw new IllegalStateException("Cannot remove more passengers from `" + this + "` than currently on the tram.");
        
        this.passengers -= passengers;
        
        if (this.passengers == 0)
        this.eventManager.getEventEntity(TrafficManager.getInstance()).emit("MINIMUM_PASSENGERS_REACHED", this);
    }
    //#endregion
}
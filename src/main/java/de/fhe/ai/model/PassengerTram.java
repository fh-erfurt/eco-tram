package de.fhe.ai.model;

/**
 * A cass taht represents tram for pasenger transport
 */
public class PassengerTram extends Tram {

    private int maxPassengers;
    private int passengers;

    /**
     * Initializes a new passenger Tram
     * 
     * @param id            the id of the tram
     * @param maxPassengers the maximum capacity of passengers of the tram
     * @param weight        the total weight of the tram excluding passengers
     * @param speed         the speed the tram will move at
     * @param tramType      the type identifier of the tram
     */
    public PassengerTram(int id, int maxPassengers, int weight, int speed, String tramType) {
        super(id, weight, speed, tramType);
        this.maxPassengers = maxPassengers;
    }

    // #region Getters & Setters
    /**
     * Returns the passenger capacity of the given tram
     * 
     * @return the maximum amount of passengers the tram can carry
     */
    public int getMaxPassengers() {
        return this.maxPassengers;
    }

    /**
     * Updates the passenger capacity of the given tram
     * 
     * @param maxPassengers the maximum amount of passengers the tram can carry
     */
    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    /**
     * Returns the current amount of passengers of the given tram
     * 
     * @return the amount of passengers the tram
     */
    public int getPassengers() {
        return this.passengers;
    }

    /**
     * Updates the current passenger amount of the given tram
     * 
     * @param passengers the new amount of passengers the tram is carrying
     */
    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }
    // #endregion
}

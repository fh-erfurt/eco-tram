package de.fhe.ai.model;

/**
 * A cass that represents tram for pasenger transport
 */
public class PassengerTram extends Tram {

    private int maxPassengers;
    private int passengers;

    /**
     * Initializes a new passenger Tram
     * 
     * @param id            the internal id of the tram
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
    public int getMaxPassengers() {
        return this.maxPassengers;
    }

    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public int getPassengers() {
        return this.passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }
    // #endregion
}

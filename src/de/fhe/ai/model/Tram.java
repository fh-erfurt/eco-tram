package de.fhe.ai.model;

/**
 * A class that represents regular tram for passenger transport
 */
public class Tram {
    private int capacity;
    private int weight;
    private int maxSpeed;
    private String tramType;
    private Connection currentConnection;
    private int progress;

    /**
     * Initializes a new Tram
     * 
     * @param passengerCapacity the maximum capacity of passengers of the tram
     * @param weight            the total weight of the tram excluding passengers
     * @param maxSpeed          the maximum speed the tram will be able to move at
     * @param tramType          the type identifier of the tram
     */
    public Tram(int passengerCapacity, int weight, int maxSpeed, String tramType) {
        this.capacity = passengerCapacity;
        this.weight = weight;
        this.maxSpeed = maxSpeed;
        this.tramType = tramType;
    }

    // #region Getters & Setters
    /**
     * Returns the passenger capacity of the given tram
     * 
     * @return the amount of passengers the tram can carry
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Updates the passenger capacity of the given tram
     * 
     * @param passengerCapacity the amount of passengers the tram can carry
     */
    public void setCapacity(int passengerCapacity) {
        this.capacity = passengerCapacity;
    }

    /**
     * Returns the weight of the given tram
     * 
     * @return the weight of the given tram in kilo grams
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Updates the weight of the given tram
     * 
     * @param weight the weight of the given tram in kilo grams
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Returns the maximum speed of the given tram
     * 
     * @return the max speed of the given tram in meters per second
     */
    public int getMaxSpeed() {
        return this.maxSpeed;
    }

    /**
     * Updates the max speed of the given tram
     * 
     * @param maxSpeed the max speed of the given tram in meters per second
     */
    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Returns the tram type of the given tram
     * 
     * @return tram type of the given tram
     */
    public String getTramType() {
        return this.tramType;
    }

    /**
     * Updates the tram type of the given tram
     * 
     * @param tramType tram type of the given tram
     */
    public void setTramType(String tramType) {
        this.tramType = tramType;
    }

    /**
     * Returns the current connection of the given tram
     * 
     * @return the connection the tram is currently on
     */
    public Connection getCurrentConnection() {
        return this.currentConnection;
    }

    /**
     * Updates the current connection of the given tram
     * 
     * @param currentConnection the connection the tram is currently on
     */
    public void setCurrentConnection(Connection currentConnection) {
        this.currentConnection = currentConnection;
    }

    /**
     * Returns the progress of the given tram
     * 
     * @return progress of the tram in meters
     */
    public int getProgress() {
        return this.progress;
    }

    /**
     * Updates the progress of the given tram
     * 
     * @param progress of the tram in meters
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }
    // #endregion

    /**
     * Moves the given tram to a specific station
     * 
     * @param station the target station to move to
     */
    public void moveToStation(/* Station station */) {
        // ...
    }
}
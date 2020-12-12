package de.fhe.ai.model;

public class Connection/* extends ModelBase*/ implements ITraversable {

    private int length;
    private int maximumWeight;

    private Station sourceStation;
    private Station destinationStation;

    private int traversionTime;
    private float trafficFactor;

    /**
     * Initializes the connection class instance and sets the default parameters
     * @param length length of the connection in meters
     * @param maximumWeight maximum weight in kilograms
     * @param sourceStation source station
     * @param destinationStation destination station
     * @param traversionTime traversion time
     * @param trafficFactor traffic factor
     */
    public Connection(/*int id,*/int length, int maximumWeight, Station sourceStation, Station destinationStation, int traversionTime, int trafficFactor) {
        /*super(id);*/
        this.length = length;
        this.maximumWeight = maximumWeight;

        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;

        this.traversionTime = traversionTime;
        this.trafficFactor = trafficFactor;
    }

    /**
     * Initializes the connection class instance and sets the default parameters
     * @param length length of the connection in meters
     * @param maximumWeight maximum weight in kilograms
     * @param sourceStation source station
     * @param destinationStation destination station
     */
    public Connection(/*int id,*/int length, int maximumWeight, Station sourceStation, Station destinationStation) {
        /*super(id);*/
        this.length = length;
        this.maximumWeight = maximumWeight;

        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
    }

    /**
     * Returns the length of the given connection
     * @return length of connection in meters
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Updates the length of the given connection
     * @param length length of the connection in meters
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Returns the maximum weight of the given connection
     * @return maximum weight in kilograms
     */
    public int getMaximumWeight() {
        return this.maximumWeight;
    }

    /**
     * Updates the maximum weight of the given connection
     * @param maximumWeight maximum weight in kilograms
     */
    public void setMaximumWeight(int maximumWeight) {
        this.maximumWeight = maximumWeight;
    }

    /**
     * Returns the source station
     * @return station object
     */
    public Station getSourceStation() {
        return this.sourceStation;
    }

    /**
     * Updates the source station
     * @param station source station
     */
    public void setSourceStation(Station station) {
        this.sourceStation = station;
    }

    /**
     * Returns the second station
     * @return station object
     */
    public Station getDestinationStation() {
        return this.destinationStation;
    }

    /**
     * Updates the destination station
     * @param station destination station
     */
    public void setDestinationStation(Station station) {
        this.destinationStation = station;
    }

    @Override
    public int getTraversionTime(int tramSpeed) {
        return this.traversionTime;
    }

    @Override
    public boolean isTramAllowed(Tram tram) {
        return tram.getWeight() <= this.maximumWeight;
    }

    @Override
    public float getTrafficFactor() {
        return this.trafficFactor;
    }

    @Override
    public void setTrafficFactor(float trafficFactor) {
        this.trafficFactor = trafficFactor;
    }
}

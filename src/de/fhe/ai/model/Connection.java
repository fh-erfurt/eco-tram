package de.fhe.ai.model;

import java.util.Arrays;

public class Connection {

    private int length;
    private String[] tramTypes;
    private int maximumWeight;

    // private Station station1;
    // private Station station2;

    /**
     * Initializes the connection class instance and sets the default parameters
     * @param length length of the connection in meters
     * @param tramTypes array of allowed tram types
     * @param maximumWeight maximum weight in kilograms
     */
    public Connection(int length, String[] tramTypes, int maximumWeight/* Station station1, Station station2 */) {
        this.length = length;
        this.tramTypes = tramTypes;
        this.maximumWeight = maximumWeight;
        /*
        this.station1 = station1;
        this.station2 = station2;
         */
    }

    /*
     * Checks whether the given tram is qualified for the connection by checking for the tram type and weight
     * @param tram tram to check
     * @return boolean if the tram is qualified for the connection
    public boolean isTramQualified(Tram tram) {
        if(!isTramTypeListed(tram.getTramType())) return false;
        if(tram.getWeight() > this.maximumWeight) return false;

        return true;
    }
     */

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
     * Returns array of the types of tram of the given connection
     * @return array of types of tram
     */
    public String[] getTramTypes() {
        return this.tramTypes;
    }

    /**
     * Updates the array of types of tram of the given connection
     * @param tramTypes array of types of tram
     */
    public void setTramTypes(String[] tramTypes) {
        this.tramTypes = tramTypes;
    }

    /**
     * Checks whether the given tram type of listed or not
     * @param tramType given tram type
     * @return boolean if the tram type is listed or not
     */
    public boolean isTramTypeListed(String tramType) {
        return Arrays.stream(this.tramTypes).anyMatch(type -> type.equalsIgnoreCase(tramType));
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

    /*
    public Station getStation1() {
        return this.station1;
    }

    public void setStation1(Station station) {
        this.station1 = station;
    }

    public Station getStation2() {
        return this.station2;
    }

    public void setStation2(Station station) {
        this.station2 = station;
    }
    */



}

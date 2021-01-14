package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;

/**
 * A class that represents something that can be traversed by a
 * {@link PassengerTram}
 */
public abstract class Traversable extends ModelBase {

    // instance fields
    private final int maximumWeight;
    private final float length;
    private float trafficFactor;

    public Traversable(int id, EventManager eventManager, float length, int maximumWeight, int trafficFactor) {
        super(id, eventManager);

        this.length = length;
        this.maximumWeight = maximumWeight;
        this.trafficFactor = trafficFactor;
    }

    // #region Getters & Setters

    /**
     * @return the given traversable's length in kilometers
     */
    public float getLength() {
        return this.length;
    }

    /**
     * @return the given traversable's traversion factor
     */
    public float getTrafficFactor() {
        return this.trafficFactor;
    }

    /**
     * @return the given traversable's maximum allowed traversion weight in kg
     */
    public int getMaximumWeight() {
        return this.maximumWeight;
    }

    /**
     * Attempts to set the current traffic factor of the given traversable, that is
     * a percentage of a traverser top speed can be used freely while traversion
     * 
     * @param trafficFactor the new traffic factor, this value should be between 0
     *                      and 1.0
     * @exception IllegalArgumentException if the traffic factor is below 0 or above
     *                                     1.0
     */
    public void setTrafficFactor(float trafficFactor) {
        if (trafficFactor < 0 || trafficFactor > 1.0) {
            throw new IllegalArgumentException("The traffic factor can only be a value between 0f and 1.0f.");
        }

        this.trafficFactor = trafficFactor;
    }
    // #endregion

    /**
     * Calculates the time in hours needed for a tram to traverse this traversable
     * 
     * @param tram the tram to calcualte the traversion time for
     * @return the traversion time in hours
     */
    public float getTraversionTime(Tram tram) {
        return this.getTrafficFactor() * (this.getLength() / tram.getSpeed());
    }

    /**
     * @param tram the tram to check for
     * @return {@code true} if the given tram can traverse this traversable;
     *         otherwise {@code false}
     */
    public boolean isTramAllowed(Tram tram) {
        return tram.getWeight() <= this.getMaximumWeight();
    }
}
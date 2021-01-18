package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;

/**
 * A class that represents something that can be traversed by a {@link Tram}
 */
public abstract class Traversable extends ModelBase {
    private final float length;
    private int         maxWeight;
    private float       trafficFactor; // [0..1.0f] defines percentage of traverser speed that is actively usable

    /**
     * Initializes a new Traversable
     * 
     * @param id            the internal id of the traversable
     * @param eventManager  the eventManager used to communicate with the TrafficManager, must be non-null
     * @param length        the length of the traversable in km, must be above 0
     * @param maxWeight     the maximum allowed weight of a traverser, must be above positive or 0
     * @param trafficFactor the traversion factor, must be between 0 and 1.0f
     * 
     * @throws IllegalArgumentException if invalid arguments are passed
     */
    public Traversable(int id, EventManager eventManager, float length, int maxWeight, float trafficFactor) {
        super(id, eventManager);

        if (length <= 0)
            throw new IllegalArgumentException("Length of `" + this + "` cannot be 0 or negative.");
        if (maxWeight < 0)
            throw new IllegalArgumentException("MaxWeight of `" + this + "` cannot be negative.");
        if (trafficFactor < 0 || trafficFactor > 1.0f)
            throw new IllegalArgumentException("TrafficFactor of `" + this + "` cannot be above 1.0f or negative.");

        this.length = length;
        this.maxWeight = maxWeight;
        this.trafficFactor = trafficFactor;
    }

    // #region Getters & Setters

    public float getLength() { return this.length; }

    public int getMaximumWeight() { return this.maxWeight; }
    public void setMaximumWeight(int maxWeight) { this.maxWeight = maxWeight; }

    public float getTrafficFactor() { return this.trafficFactor; }
    
    /**
     * Attempts to set the current traffic factor of the given traversable, that is
     * a percentage of a traverser top speed can be used freely while traversion
     * 
     * @param trafficFactor the new traffic factor, this value should be between 0 and 1.0f
     * 
     * @exception IllegalArgumentException if the traffic factor is below 0 or above 1.0f
     */
    public void setTrafficFactor(float trafficFactor) {
        if (trafficFactor < 0 || trafficFactor > 1.0f) {
            throw new IllegalArgumentException("TrafficFactor of `" + this + "` cannot be above 1.0f or negative.");
        }

        this.trafficFactor = trafficFactor;
    }
    // #endregion

    /**
     * Calculates the time in hours needed for a tram to traverse this traversable
     * 
     * @param tram the tram to calcualte the traversion time for
     * 
     * @return the traversion time in hours
     */
    public float getTraversionTime(Tram tram) {
        return this.getTrafficFactor() * (this.getLength() / tram.getSpeed());
    }

    /**
     * Checks whether a given tram is allowed to traverse this traversable
     * 
     * @param tram the tram to check for
     * 
     * @return {@code true} if the given tram can traverse this traversable; otherwise {@code false}
     */
    public boolean isTramAllowed(Tram tram) {
        return tram.getWeight() <= this.getMaximumWeight();
    }
}
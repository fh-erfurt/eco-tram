package de.fhe.ai.model;

import java.util.ArrayDeque;

/**
 * A base class for all tram types
 */
public abstract class Tram /* extends ModleBase */ {
    private int weight;
    private int speed;
    private String tramType;
    private ArrayDeque<ArrayDeque<ITraversable>> paths = new ArrayDeque<>();
    private ArrayDeque<ITraversable> currentPath = new ArrayDeque<>();
    private ITraversable position;
    private ITraversable destination;

    /**
     * @param weight   the total weight of the tram excluding passengers
     * @param speed    the speed the tram will move at
     * @param tramType the type identifier of the tram
     */
    protected Tram(int id, int weight, int speed, String tramType) {
        // super(id);
        this.weight = weight;
        this.speed = speed;
        this.tramType = tramType;
    }

    // #region Getters & Setters
    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getTramType() {
        return this.tramType;
    }

    public void setTramType(String tramType) {
        this.tramType = tramType;
    }

    public ArrayDeque<ArrayDeque<ITraversable>> getPaths() {
        return this.paths;
    }

    public void setPaths(ArrayDeque<ArrayDeque<ITraversable>> paths) {
        this.paths = paths;
    }

    public void addPath(ArrayDeque<ITraversable> path) {
        this.paths.add(path);
    }

    public ArrayDeque<ITraversable> getCurrentPath() {
        return this.currentPath;
    }

    public void setCurrentPath(ArrayDeque<ITraversable> currentPath) {
        this.currentPath = currentPath;
    }

    public ITraversable getPosition() {
        return this.position;
    }

    public ITraversable getDestination() {
        return this.destination;
    }
    // #endregion

    /**
     * Checks whether the given tram is currently in use
     * 
     * @return true if it still has a destination to move to; otherwise false
     */
    public boolean isInUse() {
        if (this.destination != null) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether the given tram is currently on a station
     * 
     * @return true if the given tram is currently on a station; otherwise false
     */
    public boolean isOnStation() {
        if (this.position instanceof Station) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether the given tram is currently on a connection
     * 
     * @return true if the given tram is currently on a connection; otherwise false
     */
    public boolean isOnConnection() {
        if (this.position instanceof Connection) {
            return true;
        }
        return false;
    }

    /**
     * Advances the tram along it's path if possible
     * 
     * @return true if there are paths left to move to; otherwise false
     */
    /*
    public boolean moveForward() {
        // position is the previously dequeued head of the this.currentPath
        // destination is the queue tail of this.currentPath
        // position
        // path[0]
        // path[1]
        // path[2]
        // ...
        // path[n] == destinaton
        if (this.currentPath.poll() instanceof ITraversable polledTraversable) {
            this.position = polledTraversable;
            return true;
        } else if (this.paths.poll() instanceof ArrayDeque<ITraversable>polledPath
                && polledPath.poll() instanceof ITraversable polledTravesable) {
            this.currentPath = polledPath;
            this.position = polledTravesable;
            this.destination = polledPath.getLast();
            return true;
        }

        this.position = null;
        this.destination = null;
        return false;
    }
    */
}
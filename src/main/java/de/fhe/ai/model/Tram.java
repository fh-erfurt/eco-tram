package de.fhe.ai.model;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A base class for all tram types
 */
public abstract class Tram /* extends ModleBase */ {
    private int weight;
    private int speed;
    private String tramType;
    private Queue<Line> paths = new ArrayDeque<>();
    private Queue<ITraversable> currentPath = new ArrayDeque<>();
    private ITraversable position;
    private ITraversable destination;

    /**
     * @param id       the internal id of the tram
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

    public int getSpeed() {
        return this.speed;
    }

    public String getTramType() {
        return this.tramType;
    }

    public Queue<Line> getPaths() {
        return this.paths;
    }

    public void addPath(Line path) {
        this.paths.add(path);
    }

    public Queue<ITraversable> getCurrentPath() {
        return this.currentPath;
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
        return this.destination != null;
    }

    /**
     * Checks whether the given tram is currently on a station
     * 
     * @return true if the given tram is currently on a station; otherwise false
     */
    public boolean isOnStation() {
        return this.position instanceof Station;
    }

    /**
     * Checks whether the given tram is currently on a connection
     * 
     * @return true if the given tram is currently on a connection; otherwise false
     */
    public boolean isOnConnection() {
        return this.position instanceof Connection;
    }

    /**
     * Advances the tram along it's path if possible
     * 
     * @return true if there were paths left to move to; otherwise false
     */
    public boolean moveForward() {
        // if path still has traversable in it, move normally
        // otherwise it's empty
        ITraversable polledTraversable = this.currentPath.poll();
        if (polledTraversable != null) {
            this.position = polledTraversable;
            this.destination = currentPath.peek();
            return true;
        }

        // if path is empty pull next path
        // otherwise the tram is finished
        Line polledLine = this.paths.poll();
        if (polledLine != null) {
            for (ITraversable iTraversable : polledLine.getRoute()) {
                this.currentPath.add(iTraversable);
            }

            // skip A to A movement on Line return
            if (this.currentPath.peek() == this.position) {
                this.currentPath.poll();
            }

            // if currentPath has elements again it should default to the first if
            this.moveForward();
        }

        // no next path in paths
        // position is kept, no destination given until a path is added again
        this.destination = null;
        return false;
    }
}
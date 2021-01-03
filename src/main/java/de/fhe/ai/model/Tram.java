package de.fhe.ai.model;

import java.util.ArrayDeque;
import java.util.Queue;

import de.fhe.ai.manager.EventManager;

/**
 * A base class for all tram types
 */
public abstract class Tram extends ModelBase {
    // regular fields
    private int weight;
    private int speed;
    private String tramType;

    // position state fields
    private Line currentLine;
    private int currentIndex;
    private ArrayDeque<Line> queuedLines = new ArrayDeque<>();

    /**
     * @param id           the internal id of the tram
     * @param eventManager the eventManager used to communicate with the
     *                     TrafficManager
     * @param weight       the total weight of the tram excluding passengers
     * @param speed        the speed the tram will move at
     * @param tramType     the type identifier of the tram
     */
    protected Tram(int id, EventManager eventManager, int weight, int speed, String tramType) {
        super(id, eventManager);
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

    public Line getCurrentLine() {
        return this.currentLine;
    }

    public Queue<Line> getPaths() {
        return this.queuedLines;
    }

    /**
     * Returns the current position if possible
     * 
     * @return {@code true} if the tram has a line assigned to it; otherwise
     *         {@code false}
     */
    public ITraversable getCurrentPosition() {
        if (this.currentLine != null) {
            return this.currentLine.getRoute().get(currentIndex);
        }

        return null;
    }

    /**
     * @deprecated Use {@link #getCurrentPosition()} for clarity instead
     * @return
     */
    @Deprecated
    public ITraversable getPosition() {
        return this.getCurrentPosition();
    }

    /**
     * Returns the next postiion if possible
     * 
     * @return the next position or {@code null} if none exists
     */
    public ITraversable getNextPosition() {
        if (this.currentLine != null) {
            // path still has nextPosition
            if (this.currentLine.getRoute().size() - 1 >= currentIndex + 1) {
                return this.currentLine.getRoute().get(currentIndex + 1);
            }

            // nextPath exists and has nextPosition
            var nextPath = this.queuedLines.peek();
            if (nextPath != null) {
                // prevent position == nextPosition
                if (this.getCurrentPosition() == nextPath.getRoute().get(0)) {
                    return nextPath.getRoute().get(1);
                } else {
                    return nextPath.getRoute().get(0);
                }
            }
        }

        return null;
    }

    public Station getDestination() {
        if (this.queuedLines.peekLast() != null) {
            return this.queuedLines.peekLast().getDestination();
        }

        return null;
    }

    /**
     * @deprecated Use {@link #addLine(Line line)} for clarity instead
     * @return
     */
    @Deprecated
    public void addPath(Line path) {
        this.addLine(path);
    }

    /**
     * Adds a new line to the current enqued lines if possible
     * 
     * @param line the line to add to the enquedLines
     * @exception IllegalStateException if the tram cannot move from it's current
     *                                  destination to the start of the new line
     */
    public void addLine(Line line) {
        // enque if either no current destination null or path starts on/at destination
        if (this.getDestination() == null
                || this.getDestination().getAdjecentConnections().contains(line.getRoute().get(0))) {
            if (this.currentLine == null) {
                this.currentLine = line;
            } else {
                this.queuedLines.add(line);
            }
        } else {
            throw new IllegalStateException("Cannot directly move from current destination `" + this.getDestination()
                    + "` to path start `" + line.getRoute().get(0) + "`.");
        }
    }
    // #endregion

    /**
     * Checks whether the given tram is currently in use
     * 
     * @return true if it still has a destination to move to; otherwise false
     */
    public boolean isInUse() {
        return this.getDestination() != null;
    }

    /**
     * Checks whether the given tram is currently on a station
     * 
     * @return true if the given tram is currently on a station; otherwise false
     */
    public boolean isOnStation() {
        return this.getCurrentPosition() instanceof Station;
    }

    /**
     * Checks whether the given tram is currently on a connection
     * 
     * @return true if the given tram is currently on a connection; otherwise false
     */
    public boolean isOnConnection() {
        return this.getCurrentPosition() instanceof Connection;
    }

    /**
     * Advances the tram along it's path if possible, this method will ignore
     * duplicates along it's path
     * 
     * @return {@code true} if it moved to an new position; otherwise {@code false}
     */
    public boolean moveForward() {
        ITraversable prevPos = this.getCurrentPosition();

        // preceed normally if path still has elements left
        if (this.currentLine.getRoute().size() - 1 >= currentIndex + 1) {
            currentIndex++;
            return true;
        }

        // try to get new path
        this.currentLine = this.queuedLines.poll();
        if (currentLine != null) {
            // skip A to A movement
            if (currentLine.getRoute().get(0) == prevPos) {
                currentIndex = 1;
            } else {
                currentIndex = 0;
            }

            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return getString(this);
    }
}
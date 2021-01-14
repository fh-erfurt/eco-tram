package de.fhe.ai.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import de.fhe.ai.manager.EventManager;

/**
 * Provides a base class for all tram types
 */
public abstract class Tram extends ModelBase {
    private final int weight;
    private final String tramType;
    private int speed;

    private Line currentLine;
    private int currentIndex;
    private ArrayDeque<Line> queuedLines = new ArrayDeque<>();

    /**
     * Initializes a new Tram
     * 
     * @param id           the internal id of the tram
     * @param eventManager the eventManager used to communicate with the
     *                     TrafficManager, must be non-null
     * @param weight       the total weight of the tram excluding passengers in kg,
     *                     must be a positive integer
     * @param speed        the speed the tram will move at in km/h, must be a
     *                     positive integer
     * @param tramType     the type identifier of the tram, must be non-null
     * @throws IllegalArgumentException if invalid parameters are passed
     */
    protected Tram(int id, EventManager eventManager, int weight, int speed, String tramType) {
        super(id, eventManager);
        if (weight < 0) {
            throw new IllegalArgumentException("Weight of tram `" + this + "` cannot to be negative.");
        } else if (speed < 0) {
            throw new IllegalArgumentException("Speed of tram `" + this + "` cannot to be negative.");
        } else if (tramType == null) {
            throw new IllegalArgumentException("TramType of tram `" + this + "` cannot be null.");
        }

        this.weight = weight;
        this.speed = speed;
        this.tramType = tramType;
    }

    /**
     * @return the given tram's weight in kg
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @return the given tram's speed in km/h
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @return the given tram's type descriptor
     */
    public String getTramType() {
        return tramType;
    }

    /**
     * @return the given tram's enqueued lines
     */
    public Queue<Line> getPaths() {
        return queuedLines;
    }

    /**
     * Returns the current line if possible
     * 
     * @return the current line or {@code null} if none exists
     */
    public Line getCurrentLine() {
        return currentLine;
    }

    /**
     * Returns the current position if possible
     * 
     * @return the current position or {@code null} if none exists
     */
    public Traversable getCurrentPosition() {
        return currentLine != null ? currentLine.getRoute().get(currentIndex) : null;
    }

    /**
     * Returns the next postiion if possible
     * 
     * @return the next position or {@code null} if none exists
     */
    public Traversable getNextPosition() {
        if (currentLine != null) {
            // path still has nextPosition
            if (currentLine.getRoute().size() - 1 >= currentIndex + 1)
                return currentLine.getRoute().get(currentIndex + 1);

            // nextPath exists and has nextPosition
            Line nextPath = queuedLines.peek();
            if (nextPath != null)
                return getCurrentPosition() == nextPath.getRoute().get(0) ? nextPath.getRoute().get(1)
                        : nextPath.getRoute().get(0); // prevent position == nextPosition
        }
        return null;
    }

    /**
     * Returns the current destination (destination of the tram's last path) if
     * possible
     * 
     * @return the current destination or {@code null} if none exists
     */
    public Station getDestinationStation() {
        if (queuedLines.peekLast() != null)
            return queuedLines.peekLast().getDestinationStation();
        else if (currentLine != null)
            return currentLine.getDestinationStation();
        return null;
    }

    /**
     * Adds a new line to the current enqued lines if possible
     * 
     * @param line the line to add to the enquedLines
     * @exception IllegalStateException if the tram cannot move from it's current
     *                                  destination/position to the start of the new
     *                                  line
     */
    public void addLine(Line line) {
        if (canAddLine(line)) {
            if (currentLine == null)
                currentLine = line;
            else
                queuedLines.add(line);
        } else if (getDestinationStation() == null)
            throw new IllegalStateException("Cannot directly move from current position `" + getCurrentPosition()
                    + "` to path start `" + line.getRoute().get(0) + "`.");
        else
            throw new IllegalStateException("Cannot directly move from current destination `" + getDestinationStation()
                    + "` to path start `" + line.getRoute().get(0) + "`.");
    }

    /**
     * Checks whether or not a the tram can move from the current destination (or
     * position if non is given) to the start fo the line
     * 
     * @param line the line to check validity for
     * @return {@code true} if the tram can move to the start of the given line;
     *         otherwise {@code false}
     */
    public boolean canAddLine(Line line) {
        // can move to start since not yet deployed
        if (getDestinationStation() == null && getCurrentPosition() == null)
            return true;

        var lineStartStation = line.getRoute().get(0);
        var lineStartConnection = line.getRoute().get(1);

        // can directly move from destination to start of line path
        // either can move destination same as start
        // or destination adjacent to startConnection
        if (this.getDestinationStation() != null && (this.getDestinationStation() == lineStartStation
                || getDestinationStation().getAdjacntConnections().contains(lineStartConnection)))
            return true;

        // destination must be null, therefore position cannot be null, no check needed
        // instanceof Connection check not needed yet but might be later and increases
        // readability

        // either can move from position to startStation
        // or position to startConnection
        if (this.getCurrentPosition() == lineStartStation
                || (this.getCurrentPosition() instanceof Station
                        && ((Station) this.getCurrentPosition()).getAdjacntConnections().contains(lineStartConnection))
                || (this.getCurrentPosition() instanceof Connection
                        && ((Connection) this.getCurrentPosition()).getDestinationStation() == lineStartStation)) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether the given tram is currently in use
     * 
     * @return true if it still has a destination to move to; otherwise false
     */
    public boolean isInUse() {
        return getDestinationStation() != null;
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
        Traversable prevPos = getCurrentPosition();

        // preceed normally if path still has elements left
        if (currentLine.getRoute().size() - 1 >= currentIndex + 1) {
            currentIndex++;
            return true;
        }

        // try to get new path
        currentLine = queuedLines.poll();
        if (currentLine != null) {
            // skip A to A movement
            currentIndex = currentLine.getRoute().get(0) == prevPos ? 1 : 0;
            return true;
        }
        return false;
    }

    /**
     * Clears all current paths and moves the given tram along the temporary line,
     * the line path has to start on or next to the current position
     * 
     * @param returnPath the line to use as returnPath for the tram
     * @exception IllegalStateException if the tram cannot move from it's current
     *                                  destination/position to the start of the new
     *                                  line
     */
    public void reassign(TemporaryLine returnPath) {
        unassign();
        addLine(returnPath);
    }

    /**
     * Clears all current paths and stops the line at it's current position,
     * preventing further movement without additon of a new line
     */
    public void unassign() {
        queuedLines.clear();
        // TODO: use factory method to get temporary line
        // so far id is copied which means it is not uniqe
        currentLine = new TemporaryLine(currentLine.getId(), currentLine.getEventManager(),
                new ArrayList<>(currentLine.getRoute().subList(0, currentIndex)));
    }
}
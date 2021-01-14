package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;

import java.util.List;

public class Station extends ModelBase implements ITraversable
{
    private final String name;
    private List<Connection> adjacentConnections;
    private long waitingTime;
    private final int maxPassengers;
    private int actualPassengers;

    public Station(int id, EventManager eventManager, String name, List<Connection> adjacentConnections, long waitingTime, int maxPassengers, int actualPassengers) {
        super(id, eventManager);

        this.name = name;
        this.adjacentConnections = adjacentConnections;
        this.waitingTime = waitingTime;
        this.maxPassengers = maxPassengers;
        this.actualPassengers = actualPassengers;
    }

    public String getName() { return name; }

    public List<Connection> getAdjacntConnections() { return adjacentConnections; }
    public void setAdjacentConnections(List<Connection> adjacentConnections) { this.adjacentConnections = adjacentConnections; }

    public long getWaitingTime() { return waitingTime; }
    public void setWaitingTime(long waitingTime) { this.waitingTime = waitingTime; }

    public int getMaxPassengers() { return maxPassengers; }

    public int getActualPassengers() { return actualPassengers; }
    public void setActualPassengers(int actualPassengers) { this.actualPassengers = actualPassengers; }


    @Override
    public int getTraversionTime(int tramSpeed) { /*TODO*/ return 0; }

    @Override
    public float getTrafficFactor() {/*TODO*/ return 0; }

    @Override
    public void setTrafficFactor(float trafficFactor) { /*TODO*/ }

    @Override
    public boolean isTramAllowed(Tram tram) { /*TODO*/ return false; }
}
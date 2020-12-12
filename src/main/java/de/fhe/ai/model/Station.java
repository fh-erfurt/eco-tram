package de.fhe.ai.model;

import java.util.List;

public class Station /*extends ModelBase*/ implements ITraversable {

    private final String name;
    private List<Connection> adjecentConnections;
    private long waitingTime;
    private final int maxPassengers;
    private int actualPassengers;

    public Station(String name, int maxPassengers) {
        this.name = name;
        this.maxPassengers = maxPassengers;
    }

    public String getName() { return name; }

    public List<Connection> getAdjecentConnections() {
        return adjecentConnections;
    }
    public void setAdjecentConnections(List<Connection> adjecentConnections) { this.adjecentConnections = adjecentConnections; }

    public long getWaitingTime() {
        return waitingTime;
    }
    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }

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
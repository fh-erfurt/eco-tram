package de.fhe.ai.model;

public interface ITraversable {

    int getTraversionTime(int tramSpeed);

    boolean isTramAllowed(Tram tram);

    int getTrafficFactor();

    void setTrafficFactor(int trafficFactor);
}
package de.fhe.ai.model;

public interface ITraversable {

    int getTraversionTime(int tramSpeed);

    boolean isTramAllowed(Tram tram);

    float getTrafficFactor();

    void setTrafficFactor(float trafficFactor);
}
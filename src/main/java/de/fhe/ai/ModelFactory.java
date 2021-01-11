package de.fhe.ai;



import de.fhe.ai.manager.EventManager;
import de.fhe.ai.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModelFactory {

    private static ModelFactory INSTANCE;

    public static ModelFactory getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ModelFactory();
        return INSTANCE;
    }

    private ModelFactory() {}

    public Connection createConnection(/*int id,*/int length, int maximumWeight, Station sourceStation, Station destinationStation, int traversionTime, int trafficFactor) {
        // TODO Eventmanager einbinden

        return new Connection(length, maximumWeight, sourceStation, destinationStation, traversionTime, trafficFactor);
    }

    public Line createLine(int id, String name, ArrayList<ITraversable> route){

        return new Line(id, EventManager.getInstance(), name, route);
    }

    //TODO passengerTram, Station

    public Station createStation(String name, int maxPassengers) {

        return new Station(name,maxPassengers);
    }

    public PassengerTram createPassengerTram(int id, int maxPassengers, int weight, int speed, String tramType) {
        if (maxPassengers < 0) {
            throw new IllegalArgumentException("Cannot declare maxPassengers to be negative.");
        }

        return new PassengerTram(id, EventManager.getInstance(), maxPassengers,weight, speed, tramType);
    }

}

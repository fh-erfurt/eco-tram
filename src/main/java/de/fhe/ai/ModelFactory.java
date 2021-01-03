package de.fhe.ai;



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
        Connection connection = new Connection(length, maximumWeight, sourceStation, destinationStation, traversionTime, trafficFactor);
        // TODO Eventmanager einbinden

        return connection;
    }

    public Line createLine(String name, ArrayList<ITraversable> route){
        Line line = new Line(name, route);
        //TODO Eventmanager einbinden

        return line;
    }

    //TODO passengerTram, Station

    public Station createStation(String name, int maxPassengers) {
        Station station = new Station(name,maxPassengers);

        return station;
    }

    public PassengerTram createPassengerTram(int id, int maxPassengers, int weight, int speed, String tramType) {
        PassengerTram passengerTram = new PassengerTram(id, maxPassengers,weight, speed, tramType);

        return passengerTram;
    }

}

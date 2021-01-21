package de.fhe.ai;


import de.fhe.ai.storage.*;

public class ModelFactory {

    private static  ModelFactory INSTANCE; // Instance of SingletonPattern
    private final IConnectionRepository CONNECTION_REPOSITORY;
    private final ILineRepository LINE_REPOSITORY;
    private final IStationRepository STATION_REPOSITORY;
    private final ITramRepository TRAM_REPOSITORY;

    //SingletonPattern
    public static ModelFactory getInstance() {
        synchronized (ModelFactory.class) {
            if (INSTANCE == null)
                INSTANCE = new ModelFactory();
        }
        return INSTANCE;
    }

    private ModelFactory() {
        CONNECTION_REPOSITORY = new ConnectionRepository();
        LINE_REPOSITORY = new LineRepository();
        STATION_REPOSITORY = new StationRepository();
        TRAM_REPOSITORY = new TramRepository();
    }
}
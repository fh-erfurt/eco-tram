package de.fhe.ai;


import de.fhe.ai.storage.*;

public class RepositoryFactory {

    private static RepositoryFactory INSTANCE; // Instance of SingletonPattern
    private final IConnectionRepository CONNECTION_REPOSITORY;
    private final ILineRepository LINE_REPOSITORY;
    private final IStationRepository STATION_REPOSITORY;
    private final ITramRepository TRAM_REPOSITORY;

    //SingletonPattern
    public static RepositoryFactory getInstance() {
        synchronized (RepositoryFactory.class) {
            if (INSTANCE == null)
                INSTANCE = new RepositoryFactory();
        }
        return INSTANCE;
    }

    private RepositoryFactory() {
        CONNECTION_REPOSITORY = new ConnectionRepository(this);
        LINE_REPOSITORY = new LineRepository(this);
        STATION_REPOSITORY = new StationRepository(this);
        TRAM_REPOSITORY = new TramRepository(this);
    }

    public IConnectionRepository getConnectionRepository() { return CONNECTION_REPOSITORY; }
    public ILineRepository getLineRepository() { return LINE_REPOSITORY; }
    public IStationRepository getStationRepository() { return STATION_REPOSITORY; }
    public ITramRepository getTramRepository() { return TRAM_REPOSITORY; }
}
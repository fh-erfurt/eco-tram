package de.fhe.ai;

import de.fhe.ai.manager.*;
import de.fhe.ai.model.*;

import de.fhe.ai.storage.IConnectionRepository;
import de.fhe.ai.storage.ILineRepository;
import de.fhe.ai.storage.IStationRepository;
import de.fhe.ai.storage.ITramRepository;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class RepositoryFactoryTest {

    private Connection createFakeConnection(int id) {
        Station sourceStation = new Station(-1, EventManager.getInstance(), TrafficManager.getInstance(), "Uniplatz", 0, 500, 0.5f, 5000, 1.0f);
        Station destinationStation = new Station(-1, EventManager.getInstance(), TrafficManager.getInstance(), "Anger", 0, 500, 0.5f, 5000, 1.0f);

        return new Connection(id, EventManager.getInstance(), TrafficManager.getInstance(), sourceStation, destinationStation, 666, 1, 1);
    }

    private Station createFakeStation(int id) {
        return new Station(id, EventManager.getInstance(), TrafficManager.getInstance(), "Anger", 10, 100, 200, 1, 1);
    }

    @Test
    public void test_create_connection() {
        Connection connection = createFakeConnection(-1);
        IConnectionRepository connectionRepository = RepositoryFactory.getInstance().getConnectionRepository();

        connectionRepository.insert(connection, null);

        Connection toCompare = (Connection) connectionRepository.getEntityById(connection.getId());

        Assert.assertEquals("length should be equal to connection length", connection.getLength(), toCompare.getLength(), 0);
        Assert.assertEquals("maximumWeight should be equal to connection maximumWeight", connection.getMaximumWeight(), toCompare.getMaximumWeight());
        Assert.assertEquals("sourceStation should be equal to connection sourceStation", connection.getSourceStation(), toCompare.getSourceStation());
        Assert.assertEquals("destinationStation should be equal to connection destinationStation", connection.getDestinationStation(), toCompare.getDestinationStation());
    }

    @Test
    public void test_create_line() {
        ArrayList<Traversable> route = new ArrayList<>();

        route.add(createFakeStation(1));
        route.add(createFakeConnection(2));
        route.add(createFakeStation(3));

        Line line = new Line(-1, EventManager.getInstance(), TrafficManager.getInstance(), "Linie2 Ringelberg", route);
        ILineRepository lineRepository = RepositoryFactory.getInstance().getLineRepository();

        lineRepository.insert(line, null);

        Line toCompare = (Line) lineRepository.getEntityById(line.getId());

        Assert.assertEquals("id should be equal to line id", line.getId(), toCompare.getId());
        Assert.assertEquals("name should be equal to line name", line.getName(), toCompare.getName());
        Assert.assertEquals("route should be equal to line route", line.getRoute(), toCompare.getRoute());
    }

    @Test
    public void test_create_station() {
        Station station = createFakeStation(-1);
        IStationRepository stationRepository = RepositoryFactory.getInstance().getStationRepository();

        stationRepository.insert(station, null);

        Station toCompare = (Station) stationRepository.getEntityById(station.getId());

        Assert.assertEquals("id should be equal to station id", station.getId(), toCompare.getId());
        Assert.assertEquals("name should be equal to station name", station.getName(), toCompare.getName());
        Assert.assertEquals("max passengers should be equal to station max passengers", station.getMaxPassengers(), toCompare.getMaxPassengers());
    }

    @Test
    public void test_create_tram() {
        Tram tram = new PassengerTram(-1, EventManager.getInstance(), TrafficManager.getInstance(), 100, 5, 0, "tram-type");
        ITramRepository tramRepository = RepositoryFactory.getInstance().getTramRepository();

        tramRepository.insert(tram, null);

        Tram toCompare = (Tram) tramRepository.getEntityById(tram.getId());

        Assert.assertEquals("id should be equal to tram id", tram.getId(), toCompare.getId());
        Assert.assertEquals("tram-type should be equal to tram tram-type", tram.getTramType(), toCompare.getTramType());
        Assert.assertEquals("weight should be equal to tram weight", tram.getWeight(), toCompare.getWeight());
    }
}


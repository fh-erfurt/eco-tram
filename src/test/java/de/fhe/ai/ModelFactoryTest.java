package de.fhe.ai;

import de.fhe.ai.manager.*;
import de.fhe.ai.model.*;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

//TODO: An neue Factory anpassen
public class ModelFactoryTest {
/*
    @Test
    public void test_create_connection() {
        int length = 666666;
        int maximumWeight = 64;
        Station sourceStation = new Station(-1, EventManager.getInstance(), TrafficManager.getInstance(), "Uniplatz", 0l, 500, 0.5f, 5000, 1.0f);
        Station destinationStation = new Station(-1, EventManager.getInstance(), TrafficManager.getInstance(), "Anger", 0l, 500, 0.5f, 5000, 1.0f);
        Tram tram = new PassengerTram(-1, EventManager.getInstance(), TrafficManager.getInstance(), 0, 0, 0, "testTram");
        int traversionTime = 1;
        int trafficFactor = 1;


        Connection connection = ModelFactory.getInstance().createConnection(length,maximumWeight,sourceStation,destinationStation,traversionTime,trafficFactor);

        Assert.assertEquals("length should be equal to connection length", length, connection.getLength(), 0);
        Assert.assertEquals("maximumWeight should be equal to connection maximumWeight", maximumWeight, connection.getMaximumWeight());
        Assert.assertEquals("sourceStation should be equal to connection sourceStation", sourceStation, connection.getSourceStation());
        Assert.assertEquals("destinationStation should be equal to connection destinationStation",destinationStation, connection.getDestinationStation());
        Assert.assertEquals("traversionTime should be equal to connection traversionTime",traversionTime, connection.getTraversionTime(tram), 0);
        //Assert.assertEquals("trafficFactor should be equal to connection trafficFactor", trafficFactor,connection.getTrafficFactor());
    }

    @Test
    public void test_create_line() {
        int id = -1;
        String name = "Linie2 Ringelberg";
        ArrayList<Traversable> route = new ArrayList<>();

        Line line = ModelFactory.getInstance().createLine(id, name, route);

        Assert.assertEquals("id should be equal to line id", id, line.getId());
        Assert.assertEquals("name should be equal to line name", name, line.getName());
        Assert.assertEquals("route should be equal to line route", route, line.getRoute());
    }

 */
}


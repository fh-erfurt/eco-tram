package de.fhe.ai;

import de.fhe.ai.model.Connection;
import de.fhe.ai.model.ITraversable;
import de.fhe.ai.model.Line;
import de.fhe.ai.model.Station;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class ModelFactoryTest {

    @Test
    public void test_create_connection() {
        int length = 666666;
        int maximumWeight = 64;
        Station sourceStation = new Station("Uniplatz", 47);
        Station destinationStation = new Station("Anger", 47);
        int traversionTime = 1;
        int trafficFactor = 1;

        Connection connection = ModelFactory.getInstance().createConnection(length,maximumWeight,sourceStation,destinationStation,traversionTime,trafficFactor);

        Assert.assertEquals("length should be equal to connection length", length, connection.getLength());
        Assert.assertEquals("maximumWeight should be equal to connection maximumWeight", maximumWeight, connection.getMaximumWeight());
        Assert.assertEquals("sourceStation should be equal to connection sourceStation", sourceStation, connection.getSourceStation());
        Assert.assertEquals("destinationStation should be equal to connection destinationStation",destinationStation, connection.getDestinationStation());
        Assert.assertEquals("traversionTime should be equal to connection traversionTime",traversionTime, connection.getTraversionTime(-1));
        //Assert.assertEquals("trafficFactor should be equal to connection trafficFactor", trafficFactor,connection.getTrafficFactor());
    }

    @Test
    public void test_create_line() {
        int id = -1;
        String name = "Linie2 Ringelberg";
        ArrayList<ITraversable> route = new ArrayList<>();

        Line line = ModelFactory.getInstance().createLine(id, name, route);

        Assert.assertEquals("id should be equal to line id", id, line.getId());
        Assert.assertEquals("name should be equal to line name", name, line.getName());
        Assert.assertEquals("route should be equal to line route", route, line.getRoute());
    }
}


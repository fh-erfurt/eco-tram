package de.fhe.ai.model;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

public class TramTest {
    // overly verbose set up can be fixed by implementing factory methods
    @Test
    public void move_forward_when_possible() {
        PassengerTram tram = new PassengerTram(0, null, 0, 0, 0, "testTram");
        Station station1 = new Station("testStation1", 0);
        Station station2 = new Station("testStation2", 0);
        Connection connection1 = new Connection(0, 0, station1, station2, 0, 0);
        Line line = new Line("testLine", new ArrayList<>() {
            {
                add(station1);
                add(connection1);
                add(station2);
            }
        });

        tram.addLine(line);

        var expectedTrue = tram.moveForward();
        Assert.assertEquals("The moveForwad should have returned true", true, expectedTrue);

        var expectedTrue2 = tram.moveForward();
        Assert.assertEquals("The moveForwad should have returned true", true, expectedTrue2);

        var expectedFalse = tram.moveForward();
        Assert.assertEquals("The moveForwad should have returned false", false, expectedFalse);
    }

    @Test
    public void move_along_path() {
        PassengerTram tram = new PassengerTram(0, null, 0, 0, 0, "testTram");
        Station station1 = new Station("testStation1", 0);
        Station station2 = new Station("testStation2", 0);
        Connection connection12 = new Connection(0, 0, station1, station2, 0, 0);
        Line line = new Line("testLine", new ArrayList<>() {
            {
                add(station1);
                add(connection12);
                add(station2);
            }
        });

        tram.addLine(line);

        var expectedStation1 = tram.getCurrentPosition();
        Assert.assertEquals("The Position should have returned Station1", station1, expectedStation1);

        tram.moveForward();
        var expectedConnection12 = tram.getCurrentPosition();
        Assert.assertEquals("The Position should have returned Connection12", connection12, expectedConnection12);

        tram.moveForward();
        var expectedStation2 = tram.getCurrentPosition();
        Assert.assertEquals("The Position should have returned Station2", station2, expectedStation2);

        tram.moveForward();
        var expectedDestination = tram.getNextPosition();
        Assert.assertEquals("The Destination should have returned null", null, expectedDestination);
    }

    @Test
    public void skip_A_to_A_movement() {
        PassengerTram tram = new PassengerTram(0, null, 0, 0, 0, "testTram");
        Station station1 = new Station("testStation1", 0);
        Station station2 = new Station("testStation2", 0);
        Connection connection12 = new Connection(0, 0, station1, station2, 0, 0);
        Connection connection21 = new Connection(0, 0, station2, station1, 0, 0);
        station1.setAdjecentConnections(new ArrayList<>() {
            {
                add(connection12);
                add(connection21);
            }
        });
        station2.setAdjecentConnections(new ArrayList<>() {
            {
                add(connection12);
                add(connection21);
            }
        });
        Line line1 = new Line("testLineNormal", new ArrayList<>() {
            {
                add(station1);
                add(connection12);
                add(station2);
            }
        });
        Line line2 = new Line("testLineReturn", new ArrayList<>() {
            {
                add(station2);
                add(connection21);
                add(station1);
            }
        });

        tram.addLine(line1);
        tram.addLine(line2);

        ITraversable expectedStation1 = tram.getCurrentPosition();
        Assert.assertEquals("The Position should have returned Station1", station1, expectedStation1);

        tram.moveForward();
        tram.moveForward();
        ITraversable expectedStation2 = tram.getCurrentPosition();
        Assert.assertEquals("The Position should have returned Station2", station2, expectedStation2);

        ITraversable expectedNextPosition = tram.getNextPosition();
        Assert.assertEquals("The NextPosition should have returned Connection21", connection21, expectedNextPosition);

        tram.moveForward();
        ITraversable expectedConnection21 = tram.getCurrentPosition();
        Assert.assertEquals("The Position should have returned Connection21", connection21, expectedConnection21);

        tram.moveForward();
        ITraversable expectedStation1Again = tram.getCurrentPosition();
        Assert.assertEquals("The Position should have returned Station1 again", station1, expectedStation1Again);
    }
}
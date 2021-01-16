package de.fhe.ai.model;

import java.util.ArrayList;

import de.fhe.ai.manager.EventManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TramTest {
    // testing tram
    private PassengerTram tram;

    // testing stations
    private Station station1;
    private Station station2;

    // testng connections
    private Connection connection12;
    private Connection connection21;

    // testin lines
    private Line line1;
    private Line line2;

    @Before
    public void before_each() {
        this.tram = new PassengerTram(-1, null, 0, 0, 0, "testTram");
        this.station1 = new Station(-1, null, "testStation1", 0l, 500, 0.5f, 5000, 1.0f);
        this.station2 = new Station(-1, null, "testStation2", 0l, 500, 0.5f, 5000, 1.0f);
        this.connection12 = new Connection(-1, null, station1, station2, 2.5f, 5000, 1.0f);
        this.connection21 = new Connection(-1, null, station2, station1, 2.5f, 5000, 1.0f);

        station1.addAdjacentConnection(connection12);
        station2.addAdjacentConnection(connection21);
        this.line1 = new Line(-1, EventManager.getInstance(), "testLine", new ArrayList<>() {
            {
                add(station1);
                add(connection12);
                add(station2);
            }
        });
        this.line2 = new Line(-1, EventManager.getInstance(), "testLineReturn", new ArrayList<>() {
            {
                add(station2);
                add(connection21);
                add(station1);
            }
        });
    }

    @Test
    public void move_forward_when_possible() {
        tram.addLine(line1);

        Assert.assertTrue("The moveForwad should have returned true", tram.moveForward());
        Assert.assertTrue("The moveForwad should have returned true", tram.moveForward());
        Assert.assertFalse("The moveForwad should have returned false", tram.moveForward());
    }

    @Test
    public void move_along_path() {
        tram.addLine(line1);

        Assert.assertEquals("The Position should have returned Station1", station1, tram.getCurrentPosition());

        tram.moveForward();
        Assert.assertEquals("The Position should have returned Connection12", connection12, tram.getCurrentPosition());

        tram.moveForward();
        Assert.assertEquals("The Position should have returned Station2", station2, tram.getCurrentPosition());

        tram.moveForward();
        Assert.assertEquals("The Destination should have returned null", null, tram.getNextPosition());
    }

    @Test
    public void skip_A_to_A_movement() {
        tram.addLine(line1);
        tram.addLine(line2);

        Assert.assertEquals("The Position should have returned Station1", station1, tram.getCurrentPosition());

        tram.moveForward();
        tram.moveForward();
        Assert.assertEquals("The Position should have returned Station2", station2, tram.getCurrentPosition());

        Assert.assertEquals("The NextPosition should have returned Connection21", connection21, tram.getNextPosition());

        tram.moveForward();
        Assert.assertEquals("The Position should have returned Connection21", connection21, tram.getCurrentPosition());

        tram.moveForward();
        Assert.assertEquals("The Position should have returned Station1 again", station1, tram.getCurrentPosition());
    }
}
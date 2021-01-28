package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;
import de.fhe.ai.manager.TrafficManager;
import org.junit.Assert;
import org.junit.Test;

public class StationTest {

    private Station createFakeStation(int id) {
        return new Station(id, EventManager.getInstance(), TrafficManager.getInstance(), "Anger", 10, 100, 200, 1, 1);
    }

    @Test
    public void add_passengers() {
        Station station = createFakeStation(-1);

        int currentPassengers = 30;
        station.setCurrentPassengers(currentPassengers);

        int newPassengers = 5;
        station.addPassengers(newPassengers);

        currentPassengers += newPassengers;

        Assert.assertEquals("current passengers should be equal to station current passengers", currentPassengers, station.getCurrentPassengers());
    }

    @Test
    public void remove_passengers() {
        Station station = createFakeStation(-1);

        int currentPassengers = 30;
        station.setCurrentPassengers(currentPassengers);

        int subtractPassengers = 5;
        station.removePassengers(subtractPassengers);

        currentPassengers -= subtractPassengers;

        Assert.assertEquals("current passengers should be equal to station current passengers", currentPassengers, station.getCurrentPassengers());
    }

    @Test
    public void get_traversion_time() {
        float trafficFactor = 1f;
        int length = 3;
        int speed = 7;
        int waitingTime = 20;

        Station station = new Station(-1, EventManager.getInstance(), TrafficManager.getInstance(), "Anger", waitingTime, 100, length, 1, trafficFactor);
        Tram tram = new PassengerTram(-1, EventManager.getInstance(), TrafficManager.getInstance(), 100, 5, speed, "tram-type");

        float calculatedTraversionTime = (trafficFactor * ((float) length / speed) + waitingTime);

        Assert.assertEquals("calculated traversion time should be equal to station traversion time", calculatedTraversionTime, station.getTraversionTime(tram), 0f);
    }

}

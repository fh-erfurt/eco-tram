package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;
import de.fhe.ai.manager.TrafficManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class LineTest {

    private Connection createFakeConnection(int id, int maxWeight) {
        Station sourceStation = new Station(-1, EventManager.getInstance(), TrafficManager.getInstance(), "Uniplatz", 0, maxWeight, 0.5f, 5000, 1.0f);
        Station destinationStation = new Station(-1, EventManager.getInstance(), TrafficManager.getInstance(), "Anger", 0, maxWeight, 0.5f, 5000, 1.0f);

        return new Connection(id, EventManager.getInstance(), TrafficManager.getInstance(), sourceStation, destinationStation, 666, maxWeight, 1);
    }

    private Station createFakeStation(int id, int maxWeight) {
        return new Station(id, EventManager.getInstance(), TrafficManager.getInstance(), "Anger", 10, 100, 200, maxWeight, 1);
    }

    @Test
    public void is_tram_allowed() {
        ArrayList<Traversable> route = new ArrayList<>();

        int maxWeight = 10000;

        route.add(createFakeStation(1, maxWeight));
        route.add(createFakeConnection(2, maxWeight));
        route.add(createFakeStation(3, maxWeight));

        Line line = new Line(-1, EventManager.getInstance(), TrafficManager.getInstance(), "Linie2 Ringelberg", route);

        Tram lightTram = new PassengerTram(-1, EventManager.getInstance(), TrafficManager.getInstance(), 100, 8000, 0, "tram-type");
        Tram heavyTram = new PassengerTram(-1, EventManager.getInstance(), TrafficManager.getInstance(), 100, 12000, 0, "tram-type");

        boolean isLightTramAllowed = line.isTramAllowed(lightTram);
        boolean isHeavyTramAllowed = line.isTramAllowed(heavyTram);

        Assert.assertTrue("light weight tram should be allowed", isLightTramAllowed);
        Assert.assertFalse("heavy weight tram should be allowed", isHeavyTramAllowed);
    }

}

package de.ecotram.backend.entity.network;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class StationTests {
    private final Station stationA = new Station();
    private final Station stationB = new Station();

    @Test
    public void connects_uniDirectional() {
        Connection connection = stationA.connectTo(stationB);

        Assertions.assertTrue(stationA.getConnectionTo(stationB).isPresent(), "A connection from A to B should be present.");
        Assertions.assertTrue(stationB.getConnectionTo(stationA).isEmpty(), "A connection from B to A should not be present.");

        Assertions.assertEquals(connection, stationA.getConnectionTo(stationB).get(), "The returned connection should be the same instance.");
    }

    @Test
    public void connects_multiDirectional() {
        Connection.Pair connectionPair = stationA.connectToAndFrom(stationB);

        Assertions.assertTrue(stationA.getConnectionTo(stationB).isPresent(), "A connection from A to B should be present.");
        Assertions.assertTrue(stationB.getConnectionTo(stationA).isPresent(), "A connection from B to A should not be present.");

        Assertions.assertEquals(connectionPair.getSourceDestination(), stationA.getConnectionTo(stationB).get(), "The returned connection should be the same instance.");
        Assertions.assertEquals(connectionPair.getDestinationSource(), stationB.getConnectionTo(stationA).get(), "The returned connection should be the same instance.");
    }
}
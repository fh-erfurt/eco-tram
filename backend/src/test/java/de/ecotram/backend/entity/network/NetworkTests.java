package de.ecotram.backend.entity.network;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

public final class NetworkTests {
    private static Network network;
    private static final Station stationA = new Station();
    private static final Station stationB = new Station();
    private static final Station stationC = new Station();
    private static final Station stationD = new Station();
    private static final Station stationE = new Station();
    private static final Station stationF = new Station();
    private static final Station stationG = new Station();

    @BeforeAll
    static void setUp() {
        // the graph
        //    b -- c   // 7
        //  /  \  / \  // 4 | 12 | 1 | 12
        // a -- g    d // 5
        //  \  / \  /  // 10 | 4 | 8 | 4
        //   f -- e    // 3

        // should result in this spanning tree with root a
        //    b    c   //
        //  /     /    // 4 | 1
        // a -- g    d // 5
        //     /    /  // 4 | 4
        //   f -- e    // 3

        stationA.connectToAndFrom(stationB, c -> c.length(4));
        stationA.connectToAndFrom(stationF, c -> c.length(10));
        stationA.connectToAndFrom(stationG, c -> c.length(5));

        stationB.connectToAndFrom(stationC, c -> c.length(7));
        stationB.connectToAndFrom(stationG, c -> c.length(12));

        stationC.connectToAndFrom(stationD, c -> c.length(12));
        stationC.connectToAndFrom(stationG, c -> c.length(1));

        stationD.connectToAndFrom(stationE, c -> c.length(4));

        stationE.connectToAndFrom(stationF, c -> c.length(3));
        stationE.connectToAndFrom(stationG, c -> c.length(8));

        stationF.connectToAndFrom(stationG, c -> c.length(4));

        var stations = new HashSet<Station>() {{
            add(stationA);
            add(stationB);
            add(stationC);
            add(stationD);
            add(stationE);
            add(stationF);
            add(stationG);
        }};

        network = Network.fromStations(stations);
    }

    @Test
    public void minimalSpanningTree_distances() {
        Assertions.assertEquals(0, network.getDistance(stationA, stationA), "StationA's distance to StationA should be 0.");
        Assertions.assertEquals(4, network.getDistance(stationA, stationB), "StationA's distance to StationB should be 4.");
        Assertions.assertEquals(6, network.getDistance(stationA, stationC), "StationA's distance to StationC should be 6.");
        Assertions.assertEquals(16, network.getDistance(stationA, stationD), "StationA's distance to StationD should be 16.");
        Assertions.assertEquals(12, network.getDistance(stationA, stationE), "StationA's distance to StationE should be 12.");
        Assertions.assertEquals(9, network.getDistance(stationA, stationF), "StationA's distance to StationF should be 9.");
        Assertions.assertEquals(5, network.getDistance(stationA, stationG), "StationA's distance to StationG should be 5.");
    }

    @Test
    public void minimalSpanningTree_hops() {
        Assertions.assertEquals(0, network.getHops(stationA, stationA), "StationA's hops to stationA should be 0.");
        Assertions.assertEquals(1, network.getHops(stationA, stationB), "StationA's hops to stationB should be 1.");
        Assertions.assertEquals(2, network.getHops(stationA, stationC), "StationA's hops to stationC should be 2.");
        Assertions.assertEquals(4, network.getHops(stationA, stationD), "StationA's hops to stationD should be 3.");
        Assertions.assertEquals(3, network.getHops(stationA, stationE), "StationA's hops to stationE should be 4.");
        Assertions.assertEquals(2, network.getHops(stationA, stationF), "StationA's hops to stationF should be 2.");
        Assertions.assertEquals(1, network.getHops(stationA, stationG), "StationA's hops to stationG should be 1.");
    }

    @Test
    public void minimalSpanningTree_previous() {
        Assertions.assertNull(network.getPrevious(stationA, stationA), "StationA's previous should be null.");
        Assertions.assertEquals(stationA, network.getPrevious(stationA, stationB), "StationB's previous should be StationA.");
        Assertions.assertEquals(stationG, network.getPrevious(stationA, stationC), "StationC's previous should be StationG.");
        Assertions.assertEquals(stationE, network.getPrevious(stationA, stationD), "StationD's previous should be StationE.");
        Assertions.assertEquals(stationF, network.getPrevious(stationA, stationE), "StationE's previous should be StationF.");
        Assertions.assertEquals(stationG, network.getPrevious(stationA, stationF), "StationF's previous should be StationG.");
        Assertions.assertEquals(stationA, network.getPrevious(stationA, stationG), "StationG's previous should be StationA.");
    }

    @Test
    public void minimalSpanningTree_path() {
        Assertions.assertArrayEquals(
                new Station[]{stationA, stationG, stationF, stationE, stationD},
                network.getMinimalSpanningTrees().get(stationA).getPathTo(stationD).toArray()
        );
    }
}
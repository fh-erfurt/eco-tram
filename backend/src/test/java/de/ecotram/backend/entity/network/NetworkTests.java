package de.ecotram.backend.entity.network;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class NetworkTests {

    private static Network network;
    private static final Station stationA = new Station();
    private static final Station stationB = new Station();
    private static final Station stationC = new Station();
    private static final Station stationD = new Station();
    private static final Station stationE = new Station();
    private static final Station stationF = new Station();
    private static final Station stationG = new Station();

    //    @BeforeAll
//    static void setUp() {
//        // the graph
//        //    b -- c    // 7
//        //  /  \  / \   // 4 | 12 | 1 | 12
//        // a -- g    d  // 5
//        //  \  / \  /   // 10 | 4 | 8 | 4
//        //   f -- e     // 3
//
//        // should result in this spanning tree with root a
//        //    b    c
//        //  /     /
//        // a -- g    d
//        //     /    /
//        //   f -- e
//
//        stationA.setName("stationA");
//        stationB.setName("stationB");
//        stationC.setName("stationC");
//        stationD.setName("stationD");
//        stationE.setName("stationE");
//        stationF.setName("stationF");
//        stationG.setName("stationG");
//
//        var connectionAB = new Connection(stationA, stationB);
//        connectionAB.setLength(4);
//        var connectionAF = new Connection(stationA, stationF);
//        connectionAF.setLength(10);
//        var connectionAG = new Connection(stationA, stationG);
//        connectionAG.setLength(5);
//        stationA.getSourceConnections().add(connectionAB);
//        stationA.getSourceConnections().add(connectionAF);
//        stationA.getSourceConnections().add(connectionAG);
//        stationB.getDestinationConnections().add(connectionAB);
//        stationF.getDestinationConnections().add(connectionAF);
//        stationG.getDestinationConnections().add(connectionAG);
//
//        var connectionBA = new Connection(stationB, stationA);
//        connectionBA.setLength(4);
//        var connectionBC = new Connection(stationB, stationC);
//        connectionBC.setLength(7);
//        var connectionBG = new Connection(stationB, stationG);
//        connectionBG.setLength(10);
//        stationB.getSourceConnections().add(connectionBA);
//        stationB.getSourceConnections().add(connectionBC);
//        stationB.getSourceConnections().add(connectionBG);
//        stationA.getDestinationConnections().add(connectionBA);
//        stationC.getDestinationConnections().add(connectionBC);
//        stationG.getDestinationConnections().add(connectionBG);
//
//        var connectionCB = new Connection(stationC, stationB);
//        connectionCB.setLength(7);
//        var connectionCD = new Connection(stationC, stationD);
//        connectionCD.setLength(12);
//        var connectionCG = new Connection(stationC, stationG);
//        connectionCG.setLength(1);
//        stationC.getSourceConnections().add(connectionCB);
//        stationC.getSourceConnections().add(connectionCD);
//        stationC.getSourceConnections().add(connectionCG);
//        stationB.getDestinationConnections().add(connectionCB);
//        stationD.getDestinationConnections().add(connectionCD);
//        stationG.getDestinationConnections().add(connectionCG);
//
//        var connectionDC = new Connection(stationD, stationC);
//        connectionDC.setLength(12);
//        var connectionDE = new Connection(stationD, stationE);
//        connectionDE.setLength(4);
//        stationD.getSourceConnections().add(connectionDC);
//        stationD.getSourceConnections().add(connectionDE);
//        stationC.getDestinationConnections().add(connectionDC);
//        stationE.getDestinationConnections().add(connectionDE);
//
//        var connectionED = new Connection(stationE, stationD);
//        connectionED.setLength(4);
//        var connectionEF = new Connection(stationE, stationF);
//        connectionEF.setLength(3);
//        var connectionEG = new Connection(stationE, stationG);
//        connectionEG.setLength(8);
//        stationE.getSourceConnections().add(connectionED);
//        stationE.getSourceConnections().add(connectionEF);
//        stationE.getSourceConnections().add(connectionEG);
//        stationD.getDestinationConnections().add(connectionED);
//        stationF.getDestinationConnections().add(connectionEF);
//        stationG.getDestinationConnections().add(connectionEG);
//
//        var connectionFA = new Connection(stationF, stationA);
//        connectionFA.setLength(10);
//        var connectionFE = new Connection(stationF, stationE);
//        connectionFE.setLength(3);
//        var connectionFG = new Connection(stationF, stationG);
//        connectionFG.setLength(4);
//        stationF.getSourceConnections().add(connectionFA);
//        stationF.getSourceConnections().add(connectionFE);
//        stationF.getSourceConnections().add(connectionFG);
//        stationA.getDestinationConnections().add(connectionFA);
//        stationE.getDestinationConnections().add(connectionFE);
//        stationG.getDestinationConnections().add(connectionFG);
//
//        var connectionGA = new Connection(stationG, stationA);
//        connectionGA.setLength(5);
//        var connectionGB = new Connection(stationG, stationB);
//        connectionGB.setLength(10);
//        var connectionGC = new Connection(stationG, stationC);
//        connectionGC.setLength(1);
//        var connectionGE = new Connection(stationG, stationE);
//        connectionGE.setLength(8);
//        var connectionGF = new Connection(stationG, stationF);
//        connectionGF.setLength(4);
//        stationG.getSourceConnections().add(connectionGA);
//        stationG.getSourceConnections().add(connectionGB);
//        stationG.getSourceConnections().add(connectionGC);
//        stationG.getSourceConnections().add(connectionGE);
//        stationG.getSourceConnections().add(connectionGF);
//        stationG.getDestinationConnections().add(connectionGA);
//        stationB.getDestinationConnections().add(connectionGB);
//        stationC.getDestinationConnections().add(connectionGC);
//        stationE.getDestinationConnections().add(connectionGE);
//        stationF.getDestinationConnections().add(connectionGF);
//
//        var stations = new ArrayList<Station>() {{
//            add(stationA);
//            add(stationB);
//            add(stationC);
//            add(stationD);
//            add(stationE);
//            add(stationF);
//            add(stationG);
//        }};
//
//        network = Network.fromStations(stations);
//    }
    @BeforeAll
    static void setUp() {
        // the graph
        //    b -- c    // 7
        //  /  \  / \   // 4 | 12 | 1 | 12
        // a -- g    d  // 5
        //  \  / \  /   // 10 | 4 | 8 | 4
        //   f -- e     // 3

        // should result in this spanning tree with root a
        //    b    c
        //  /     /
        // a -- g    d
        //     /    /
        //   f -- e

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

        var stations = new ArrayList<Station>() {{
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

        var minimalTree = network.getMinimalSpanningTrees().get(stationA);
        Assertions.assertArrayEquals(new Station[]{stationA, stationG, stationF, stationE, stationD}, minimalTree.getPathTo(stationD).toArray());
    }
}
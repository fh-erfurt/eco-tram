package de.ecotram.backend.network;

import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Network;
import de.ecotram.backend.entity.network.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class NetworkTests {

    @Test
    public void createsMinimalSpanningTree() {

        //    b -- c    // 7
        //  /  \  / \   // 4 | 12 | 1 | 12
        // a -- g    f  // 5
        //  \  / \  /   // 10 | 4 | 8 | 4
        //   f -- d     // 3

        var stationA = new Station();
        stationA.setName("stationA");
        var stationB = new Station();
        stationB.setName("stationB");
        var stationC = new Station();
        stationC.setName("stationC");
        var stationD = new Station();
        stationD.setName("stationD");
        var stationE = new Station();
        stationE.setName("stationE");
        var stationF = new Station();
        stationF.setName("stationF");
        var stationG = new Station();
        stationG.setName("stationG");

        var connectionAB = new Connection(stationA, stationB);
        connectionAB.setLength(4);
        var connectionAF = new Connection(stationA, stationF);
        connectionAF.setLength(10);
        var connectionAG = new Connection(stationA, stationG);
        connectionAG.setLength(5);
        stationA.getSourceConnections().add(connectionAB);
        stationA.getSourceConnections().add(connectionAF);
        stationA.getSourceConnections().add(connectionAG);
        stationB.getDestinationConnections().add(connectionAB);
        stationF.getDestinationConnections().add(connectionAF);
        stationG.getDestinationConnections().add(connectionAG);

        var connectionBA = new Connection(stationB, stationA);
        connectionBA.setLength(4);
        var connectionBC = new Connection(stationB, stationC);
        connectionBC.setLength(7);
        var connectionBG = new Connection(stationB, stationG);
        connectionBG.setLength(10);
        stationB.getSourceConnections().add(connectionBA);
        stationB.getSourceConnections().add(connectionBC);
        stationB.getSourceConnections().add(connectionBG);
        stationA.getDestinationConnections().add(connectionBA);
        stationC.getDestinationConnections().add(connectionBC);
        stationG.getDestinationConnections().add(connectionBG);

        var connectionCB = new Connection(stationC, stationB);
        connectionCB.setLength(7);
        var connectionCD = new Connection(stationC, stationD);
        connectionCD.setLength(12);
        var connectionCG = new Connection(stationC, stationG);
        connectionCG.setLength(1);
        stationC.getSourceConnections().add(connectionCB);
        stationC.getSourceConnections().add(connectionCD);
        stationC.getSourceConnections().add(connectionCG);
        stationB.getDestinationConnections().add(connectionCB);
        stationD.getDestinationConnections().add(connectionCD);
        stationG.getDestinationConnections().add(connectionCG);

        var connectionDC = new Connection(stationD, stationC);
        connectionDC.setLength(12);
        var connectionDE = new Connection(stationD, stationE);
        connectionDE.setLength(4);
        stationD.getSourceConnections().add(connectionDC);
        stationD.getSourceConnections().add(connectionDE);
        stationC.getDestinationConnections().add(connectionDC);
        stationE.getDestinationConnections().add(connectionDE);

        var connectionED = new Connection(stationE, stationD);
        connectionED.setLength(4);
        var connectionEF = new Connection(stationE, stationF);
        connectionEF.setLength(3);
        var connectionEG = new Connection(stationE, stationG);
        connectionEG.setLength(8);
        stationE.getSourceConnections().add(connectionED);
        stationE.getSourceConnections().add(connectionEF);
        stationE.getSourceConnections().add(connectionEG);
        stationD.getDestinationConnections().add(connectionED);
        stationF.getDestinationConnections().add(connectionEF);
        stationG.getDestinationConnections().add(connectionEG);

        var connectionFA = new Connection(stationF, stationA);
        connectionFA.setLength(10);
        var connectionFE = new Connection(stationF, stationE);
        connectionFE.setLength(3);
        var connectionFG = new Connection(stationF, stationG);
        connectionFG.setLength(4);
        stationF.getSourceConnections().add(connectionFA);
        stationF.getSourceConnections().add(connectionFE);
        stationF.getSourceConnections().add(connectionFG);
        stationA.getDestinationConnections().add(connectionFA);
        stationE.getDestinationConnections().add(connectionFE);
        stationG.getDestinationConnections().add(connectionFG);

        var connectionGA = new Connection(stationG, stationA);
        connectionGA.setLength(5);
        var connectionGB = new Connection(stationG, stationB);
        connectionGB.setLength(10);
        var connectionGC = new Connection(stationG, stationC);
        connectionGC.setLength(1);
        var connectionGE = new Connection(stationG, stationE);
        connectionGE.setLength(8);
        var connectionGF = new Connection(stationG, stationF);
        connectionGF.setLength(4);
        stationG.getSourceConnections().add(connectionGA);
        stationG.getSourceConnections().add(connectionGB);
        stationG.getSourceConnections().add(connectionGC);
        stationG.getSourceConnections().add(connectionGE);
        stationG.getSourceConnections().add(connectionGF);
        stationG.getDestinationConnections().add(connectionGA);
        stationB.getDestinationConnections().add(connectionGB);
        stationC.getDestinationConnections().add(connectionGC);
        stationE.getDestinationConnections().add(connectionGE);
        stationF.getDestinationConnections().add(connectionGF);

        var stations = new ArrayList<Station>() {{
            add(stationA);
            add(stationB);
            add(stationC);
            add(stationD);
            add(stationE);
            add(stationF);
            add(stationG);
        }};

        var network = new Network(stations);
        var minimalTree = network.pathDijkstra(stationA);

        Assertions.assertEquals(0, minimalTree.get(stationA).getDistance(), "StationA should have a distance of 0.");
        Assertions.assertEquals(4, minimalTree.get(stationB).getDistance(), "StationB should have a distance of 4.");
        Assertions.assertEquals(6, minimalTree.get(stationC).getDistance(), "StationC should have a distance of 6.");
        Assertions.assertEquals(16, minimalTree.get(stationD).getDistance(), "StationD should have a distance of 16.");
        Assertions.assertEquals(12, minimalTree.get(stationE).getDistance(), "StationE should have a distance of 12.");
        Assertions.assertEquals(9, minimalTree.get(stationF).getDistance(), "StationF should have a distance of 9.");
        Assertions.assertEquals(5, minimalTree.get(stationG).getDistance(), "StationG should have a distance of 5.");

        Assertions.assertNull(minimalTree.get(stationA).getPrevious(), "StationA's previous should be null.");
        Assertions.assertEquals(stationA, minimalTree.get(stationB).getPrevious(), "StationB's previous should be StationA.");
        Assertions.assertEquals(stationG, minimalTree.get(stationC).getPrevious(), "StationC's previous should be StationG.");
        Assertions.assertEquals(stationE, minimalTree.get(stationD).getPrevious(), "StationD's previous should be StationE.");
        Assertions.assertEquals(stationF, minimalTree.get(stationE).getPrevious(), "StationE's previous should be StationF.");
        Assertions.assertEquals(stationG, minimalTree.get(stationF).getPrevious(), "StationF's previous should be StationG.");
        Assertions.assertEquals(stationA, minimalTree.get(stationG).getPrevious(), "StationG's previous should be StationA.");
    }
}
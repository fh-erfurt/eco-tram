package de.ecotram.backend.entity;

import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public final class TramTests {
    private final Station station1 = new Station();
    private final Station station2 = new Station();
    private final Station station3 = new Station();

    private final Connection connection12;
    private final Connection connection23;
    private final Connection connection31;

    private final List<LineEntry> testRoute = new ArrayList<>() {{
        add(new LineEntry(0, null, station1));
        add(new LineEntry(1, null, station2));
        add(new LineEntry(2, null, station3));
        add(new LineEntry(4, null, station1));
    }};

    public TramTests() {
        this.connection12 = station1.connectTo(station2);
        this.connection23 = station2.connectTo(station3);
        this.connection31 = station3.connectTo(station1);
    }

    @Test
    public void moves_to_correct_station() {
        Tram tram = new Tram(testRoute);

        Assertions.assertEquals(connection12, tram.nextStation());
        Assertions.assertEquals(connection23, tram.nextStation());
        Assertions.assertEquals(connection31, tram.nextStation());
    }
}
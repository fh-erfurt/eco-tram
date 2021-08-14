package de.ecotram.backend.handler;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.PassengerTram;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.ConnectionRepository;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.repository.PassengerTramRepository;
import de.ecotram.backend.repository.StationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.profiles.active = test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StatisticsHandlerTests {

    private final Connection connection1 = new Connection();
    private final Connection connection2 = new Connection();
    private final Line line1 = new Line();
    private final Line line2 = new Line();
    private final Line line3 = new Line();
    private final PassengerTram passengerTram1 = new PassengerTram();
    private final PassengerTram passengerTram2 = new PassengerTram();
    private final PassengerTram passengerTram3 = new PassengerTram();
    private final PassengerTram passengerTram4 = new PassengerTram();
    private final Station station1 = new Station();
    private final Station station2 = new Station();
    private final Station station3 = new Station();
    private final Station station4 = new Station();
    private final Station station5 = new Station();
    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private PassengerTramRepository passengerTramRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private StatisticsHandler statisticsHandler;

    @Test
    public void testGetConnectionCount() {
        connectionRepository.save(connection1);
        connectionRepository.save(connection2);

        assertEquals(2, statisticsHandler.getConnectionCount().getResults());
    }

    @Test
    public void testGetLineCount() {
        lineRepository.save(line1);
        lineRepository.save(line2);
        lineRepository.save(line3);

        assertEquals(3, statisticsHandler.getLineCount().getResults());
    }

    @Test
    public void testGetPassengerTramCount() {
        passengerTramRepository.save(passengerTram1);
        passengerTramRepository.save(passengerTram2);
        passengerTramRepository.save(passengerTram3);
        passengerTramRepository.save(passengerTram4);

        assertEquals(4, statisticsHandler.getPassengerTramCount().getResults());
    }

    @Test
    public void testGetStationCount() {
        stationRepository.save(station1);
        stationRepository.save(station2);
        stationRepository.save(station3);
        stationRepository.save(station4);
        stationRepository.save(station5);

        assertEquals(5, statisticsHandler.getStationCount().getResults());
    }

}

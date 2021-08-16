package de.ecotram.backend.handler;

import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.ConnectionRepository;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active = test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ConnectionHandlerTests {
    public final Station station1 = new Station();
    public final Station station2 = new Station();
    public final Station station3 = new Station();
    public final Station station4 = new Station();

    public final Connection connection1 = new Connection();
    private final Random random = new Random();

    @Autowired
    public ConnectionHandler connectionHandler;

    @Autowired
    public StationRepository stationRepository;

    @Autowired
    public ConnectionRepository connectionRepository;

    @Test
    public void testValidateConnectionBody() {
        stationRepository.save(station1);
        stationRepository.save(station2);

        ConnectionHandler.ConnectionBody successfulConnectionBody = new ConnectionHandler.ConnectionBody(
                station1.getId(),
                station2.getId());

        ConnectionHandler.ConnectionBody failedConnectionBody = new ConnectionHandler.ConnectionBody(
                random.nextInt(1000) + 1000,
                random.nextInt(1000) + 1000);

        assertDoesNotThrow(() -> connectionHandler.validateConnectionBody(successfulConnectionBody));
        assertThrows(ErrorResponseException.class, () -> connectionHandler.validateConnectionBody(failedConnectionBody));
    }

    @Test
    public void testAppendSourceAndDestinationStation() {
        ConnectionHandler.ConnectionStations connectionStations = new ConnectionHandler.ConnectionStations(
                station1,
                station2);

        connectionHandler.appendSourceAndDestinationStation(connection1, connectionStations);

        assertEquals(station1, connection1.getSourceStation());
        assertEquals(station2, connection1.getDestinationStation());
    }

    @Test
    public void testCreateConnectionFromRequest() {
        stationRepository.save(station1);
        stationRepository.save(station2);

        ConnectionHandler.ConnectionBody connectionBody = new ConnectionHandler.ConnectionBody(
                station1.getId(),
                station2.getId());

        try {
            Connection connection = connectionHandler.createConnectionFromRequest(connectionBody);
            assertTrue(connectionRepository.existsById(connection.getId()));
        } catch (ErrorResponseException exception) {
            fail("Did not expect exception " + exception.getErrorResponse().getMessage());
        }
    }

    @Test
    public void testUpdateConnectionFromRequest() {
        stationRepository.save(station1);
        stationRepository.save(station2);
        stationRepository.save(station3);
        stationRepository.save(station4);

        ConnectionHandler.ConnectionBody previousConnectionBody = new ConnectionHandler.ConnectionBody(
                station1.getId(),
                station2.getId());

        try {
            Connection connection = connectionHandler.createConnectionFromRequest(previousConnectionBody);

            assertEquals(station1.getId(), connection.getSourceStation().getId());
            assertEquals(station2.getId(), connection.getDestinationStation().getId());

            ConnectionHandler.ConnectionBody newConnectionBody = new ConnectionHandler.ConnectionBody(
                    station3.getId(),
                    station4.getId());

            connectionHandler.updateConnectionFromRequest(connection, newConnectionBody);

            assertTrue(connectionRepository.existsById(connection.getId()));
            assertEquals(station3.getId(), connection.getSourceStation().getId());
            assertEquals(station4.getId(), connection.getDestinationStation().getId());
        } catch (ErrorResponseException exception) {
            fail("Did not expect exception " + exception.getErrorResponse().getMessage());
        }
    }
}
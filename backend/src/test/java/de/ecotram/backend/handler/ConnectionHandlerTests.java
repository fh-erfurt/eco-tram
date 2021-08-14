package de.ecotram.backend.handler;

import de.ecotram.backend.BackendServerNewApplication;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.ConnectionRepository;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponseException;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(properties = "spring.profiles.active = test")
@AutoConfigureMockMvc
public class ConnectionHandlerTests {

    @Autowired
    public ConnectionHandler connectionHandler;

    @Autowired
    public StationRepository stationRepository;

    @Autowired
    public ConnectionRepository connectionRepository;

    @Autowired
    private MockMvc mvc;

    public final Station station1 = new Station();
    public final Station station2 = new Station();
    public final Station station3 = new Station();
    public final Station station4 = new Station();
    public final Station station5 = new Station();

    public final Connection connection1 = new Connection();
    public final Connection connection2 = new Connection();
    public final Connection connection3 = new Connection();

    @Test
    public void testRepository() {
        stationRepository.save(station1);
        stationRepository.save(station2);
        stationRepository.save(station3);
        stationRepository.save(station4);
        stationRepository.save(station5);

        assertEquals(5, stationRepository.findAll().size(), "Test if 5 stations has been stored in repository");
    }

    @Test
    public void testValidateConnectionBody() {
        System.out.println("Adding two valid stations");

        stationRepository.save(station1);
        stationRepository.save(station2);

        ConnectionHandler.ConnectionBody successfulConnectionBody = new ConnectionHandler.ConnectionBody(1, 2);
        ConnectionHandler.ConnectionBody failedConnectionBody = new ConnectionHandler.ConnectionBody(22, 42);

        assertDoesNotThrow(() -> connectionHandler.validateConnectionBody(successfulConnectionBody));
        assertThrows(ErrorResponseException.class, () -> connectionHandler.validateConnectionBody(failedConnectionBody));
    }

    @Test
    public void testAppendSourceAndDestinationStation() {
        ConnectionHandler.ConnectionStations connectionStations = new ConnectionHandler.ConnectionStations(station1, station2);

        connectionHandler.appendSourceAndDestinationStation(connection1, connectionStations);

        assertEquals(connection1.getSourceStation(), station1);
        assertEquals(connection1.getDestinationStation(), station2);
    }

    @Test
    public void testCreateConnectionFromRequest() {
        System.out.println("Adding two valid stations");

        stationRepository.save(station1);
        stationRepository.save(station2);

        ConnectionHandler.ConnectionBody connectionBody = new ConnectionHandler.ConnectionBody(1, 2);

        try {
            Connection connection = connectionHandler.createConnectionFromRequest(connectionBody);
            assertTrue(connectionRepository.existsById(connection.getId()));
        } catch(ErrorResponseException exception) {
            fail("Did not expect exception " + exception.getErrorResponse().getMessage());
        }
    }

    @Test
    public void testUpdateConnectionFromRequest() {
        System.out.println("Adding four valid stations");

        stationRepository.save(station1);
        stationRepository.save(station2);
        stationRepository.save(station3);
        stationRepository.save(station4);

        ConnectionHandler.ConnectionBody previousConnectionBody = new ConnectionHandler.ConnectionBody(1, 2);

        try {
            Connection connection = connectionHandler.createConnectionFromRequest(previousConnectionBody);

            assertEquals(connection.getSourceStation().getId(), station1.getId());
            assertEquals(connection.getDestinationStation().getId(), station2.getId());

            ConnectionHandler.ConnectionBody newConnectionBody = new ConnectionHandler.ConnectionBody(3, 4);

            connectionHandler.updateConnectionFromRequest(connection, newConnectionBody);

            assertTrue(connectionRepository.existsById(connection.getId()));
            assertEquals(connection.getSourceStation().getId(), station3.getId());
            assertEquals(connection.getDestinationStation().getId(), station4.getId());
        } catch(ErrorResponseException exception) {
            fail("Did not expect exception " + exception.getErrorResponse().getMessage());
        }
    }

}

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
	public void validate_connection_body() {
		stationRepository.save(station1);
		stationRepository.save(station2);

		ConnectionHandler.ConnectionBody successfulConnectionBody = new ConnectionHandler.ConnectionBody(
				station1.getId(),
				station2.getId());

		ConnectionHandler.ConnectionBody failedConnectionBody = new ConnectionHandler.ConnectionBody(
				random.nextInt(1000) + 1000,
				random.nextInt(1000) + 1000);

		assertDoesNotThrow(() -> connectionHandler.validateConnectionBody(successfulConnectionBody), "correct validation should not throw an error");
		assertThrows(ErrorResponseException.class, () -> connectionHandler.validateConnectionBody(failedConnectionBody), "invalid validation should throw an error");
	}

	@Test
	public void append_source_and_destination_station() {
		ConnectionHandler.ConnectionStations connectionStations = new ConnectionHandler.ConnectionStations(
				station1,
				station2);

		connectionHandler.appendSourceAndDestinationStation(connection1, connectionStations);

		assertEquals(station1, connection1.getSourceStation(), "source station should be station 1");
		assertEquals(station2, connection1.getDestinationStation(), "destination station should be station 2");
	}

	@Test
	public void create_connection_from_request() {
		stationRepository.save(station1);
		stationRepository.save(station2);

		ConnectionHandler.ConnectionBody connectionBody = new ConnectionHandler.ConnectionBody(
				station1.getId(),
				station2.getId());

		try {
			Connection connection = connectionHandler.createConnectionFromRequest(connectionBody);
			assertTrue(connectionRepository.existsById(connection.getId()), "connection repository should have stored the connection");
		} catch (ErrorResponseException exception) {
			fail("Did not expect exception " + exception.getErrorResponse().getMessage());
		}
	}

	@Test
	public void update_connection_from_request() {
		stationRepository.save(station1);
		stationRepository.save(station2);
		stationRepository.save(station3);
		stationRepository.save(station4);

		ConnectionHandler.ConnectionBody previousConnectionBody = new ConnectionHandler.ConnectionBody(
				station1.getId(),
				station2.getId());

		try {
			Connection connection = connectionHandler.createConnectionFromRequest(previousConnectionBody);

			assertEquals(station1.getId(), connection.getSourceStation().getId(), "source station should be station 1");
			assertEquals(station2.getId(), connection.getDestinationStation().getId(), "destination station should be station 2");

			ConnectionHandler.ConnectionBody newConnectionBody = new ConnectionHandler.ConnectionBody(
					station3.getId(),
					station4.getId());

			connectionHandler.updateConnectionFromRequest(connection, newConnectionBody);

			assertTrue(connectionRepository.existsById(connection.getId()), "connection repository should have stored the connection");
			assertEquals(station3.getId(), connection.getSourceStation().getId(), "updated source station should be station 3");
			assertEquals(station4.getId(), connection.getDestinationStation().getId(), "updated destination station should be station 4");
		} catch (ErrorResponseException exception) {
			fail("Did not expect exception " + exception.getErrorResponse().getMessage());
		}
	}
}
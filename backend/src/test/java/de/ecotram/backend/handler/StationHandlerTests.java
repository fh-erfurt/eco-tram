package de.ecotram.backend.handler;

import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active = test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StationHandlerTests {
	private final Random random = new Random();

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private StationHandler stationHandler;

	@Test
	public void validate_station_body() {
		StationHandler.StationBody successfulStationBody = new StationHandler.StationBody(
				"Random" + random.nextInt(150) + 10,
				random.nextInt(100) + 100
		);

		StationHandler.StationBody failedStationBody = new StationHandler.StationBody("", -1);

		assertDoesNotThrow(() -> stationHandler.validateStationBody(successfulStationBody), "correct station validation should not throw an exception");
		assertThrows(ErrorResponseException.class, () -> stationHandler.validateStationBody(failedStationBody), "invalid station validation should throw an exception");
	}

	@Test
	@Transactional
	public void create_station_from_request() {
		StationHandler.StationBody stationBody = new StationHandler.StationBody(
				"Random" + random.nextInt(150) + 10,
				random.nextInt(100) + 100
		);

		try {
			Station station = stationHandler.createStationFromRequest(stationBody);
			assertTrue(stationRepository.existsById(station.getId()), "station repository should have stored a station");
		} catch (ErrorResponseException exception) {
			fail("Did not expect exception " + exception.getErrorResponse().getMessage());
		}
	}

	@Test
	@Transactional
	public void update_station_from_request() {
		int originalMaxPassengers = random.nextInt(100) + 100;
		String originalName = "Originalstation";

		StationHandler.StationBody previousStationBody = new StationHandler.StationBody(
				originalName,
				originalMaxPassengers
		);

		try {
			Station station = stationHandler.createStationFromRequest(previousStationBody);

			assertEquals(originalName, station.getName(), "station should have the name " + originalName);
			assertEquals(originalMaxPassengers, station.getMaxPassengers(), "station should have maxPassengers " + originalMaxPassengers);

			String newName = "Neustation";
			int newMaxPassengers = random.nextInt(100) + 100;

			StationHandler.StationBody newStationBody = new StationHandler.StationBody(
					newName,
					newMaxPassengers
			);

			stationHandler.updateStationFromRequest(station, newStationBody);

			assertEquals(newName, station.getName(), "updated station should have the name " + newName);
			assertEquals(newMaxPassengers, station.getMaxPassengers(), "updated station should have maxPassengers " + newMaxPassengers);
			assertTrue(stationRepository.existsById(station.getId()), "station repository should have stored the station");
		} catch (ErrorResponseException exception) {
			fail("Did not expect exception " + exception.getErrorResponse().getMessage());
		}
	}
}
package de.ecotram.backend.handler;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active = test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LineHandlerTests {
	public final Station station1 = new Station();
	public final Station station2 = new Station();
	public final Station station3 = new Station();
	public final Station station4 = new Station();
	public final Station station5 = new Station();
	private final Random random = new Random();

	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private LineHandler lineHandler;

	private void addStations() {
		stationRepository.save(station1);
		stationRepository.save(station2);
		stationRepository.save(station3);
		stationRepository.save(station4);
		stationRepository.save(station5);
	}

	@Test
	@Transactional
	public void validate_station_ids() {
		this.addStations();

		ArrayList<Long> inputIds = new ArrayList<>(Arrays.asList(
				station1.getId(),
				station2.getId(),
				station3.getId(),
				station4.getId(),
				station5.getId()
		));

		LineHandler.ValidationResult validationResult = lineHandler.validateStationIds(inputIds);

		assertEquals(inputIds.size(), validationResult.getStations().size(), "validated stations should have a size of " + inputIds.size());
		assertEquals(inputIds.size() - 1, validationResult.getConnections().size(), "validated connections should have a size of " + (inputIds.size() - 1));

		ArrayList<Long> invalidInputIds = new ArrayList<>(Arrays.asList(
				station1.getId(),
				station3.getId(),
				station2.getId(),
				random.nextInt(1000) + 1000L,
				random.nextInt(1000) + 1000L));

		int invalidExpectedStations = 3;
		int invalidExpectedConnections = 2;

		LineHandler.ValidationResult invalidValidationResult = lineHandler.validateStationIds(invalidInputIds);

		assertEquals(invalidExpectedStations, invalidValidationResult.getStations().size(), "invalid stations should have a size of " + invalidExpectedStations);
		assertEquals(invalidExpectedConnections, invalidValidationResult.getConnections().size(), "invalid connections should have a size of " + invalidExpectedConnections);
	}

	@Test
	public void validate_line_body() {
		this.addStations();

		LineHandler.LineBody successfulLineBody = new LineHandler.LineBody(
				"Testline",
				station1.getId() + "," +
						station2.getId() + "," +
						station3.getId() + "," +
						station4.getId()
		);

		LineHandler.LineBody failedLineBody = new LineHandler.LineBody("", "");

		assertDoesNotThrow(() -> lineHandler.validateLineBody(successfulLineBody), "correct validation should not throw an exception");
		assertThrows(ErrorResponseException.class, () -> lineHandler.validateLineBody(failedLineBody), "incorrect validation should throw an exception");
	}

	@Test
	@Transactional
	public void create_line_from_request() {
		this.addStations();

		LineHandler.LineBody lineBody = new LineHandler.LineBody(
				"Testline",
				station1.getId() + "," +
						station2.getId() + "," +
						station3.getId() + "," +
						station4.getId()
		);

		try {
			Line line = lineHandler.createLineFromRequest(lineBody);
			assertTrue(lineRepository.existsById(line.getId()), "line repository should have stored the line");
		} catch (ErrorResponseException exception) {
			fail("Did not expect exception " + exception.getErrorResponse().getMessage());
		}
	}

	@Test
	@Transactional
	public void update_line_from_request() {
		this.addStations();

		String originalName = "Originallinie";
		LineHandler.LineBody previousLineBody = new LineHandler.LineBody(
				originalName,
				station1.getId() + "," +
						station2.getId()
		);

		try {
			Line line = lineHandler.createLineFromRequest(previousLineBody);

			assertEquals(originalName, line.getName(), "line name should be " + originalName);
			assertEquals(2, line.getRoute().size(), "line route should have 2 items");

			String newName = "Neulinie";
			LineHandler.LineBody newLineBody = new LineHandler.LineBody(newName,
					station3.getId() + "," +
							station4.getId() + "," +
							station5.getId()
			);

			lineHandler.updateLineFromRequest(line, newLineBody);

			assertEquals(newName, line.getName(), "updated line name should be " + newName);
			assertEquals(3, line.getRoute().size(), "updated line should have 3 items");
			assertTrue(lineRepository.existsById(line.getId()), "line repository should have stored the line");
		} catch (ErrorResponseException exception) {
			fail("Did not expect exception " + exception.getErrorResponse().getMessage());
		}
	}

	@Test
	@Transactional
	public void delete_line() {
		this.addStations();

		LineHandler.LineBody lineBody = new LineHandler.LineBody(
				"Testline",
				station1.getId() + "," +
						station2.getId() + "," +
						station3.getId() + "," +
						station4.getId() + "," +
						station5.getId()
		);

		try {
			Line line = lineHandler.createLineFromRequest(lineBody);
			assertTrue(lineRepository.existsById(line.getId()), "line repository should have stored the line");

			lineHandler.deleteLine(line);
			assertFalse(lineRepository.existsById(line.getId()), "line repository should not have stored the line");
		} catch (ErrorResponseException exception) {
			fail("Did not expect exception " + exception.getErrorResponse().getMessage());
		}
	}

}

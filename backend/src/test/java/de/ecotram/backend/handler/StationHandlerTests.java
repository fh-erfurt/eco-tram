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
    public void testValidateStationBody() {
        StationHandler.StationBody successfulStationBody = new StationHandler.StationBody(
                "Random" + random.nextInt(150) + 10,
                random.nextInt(100) + 100
        );

        StationHandler.StationBody failedStationBody = new StationHandler.StationBody("", -1);

        assertDoesNotThrow(() -> stationHandler.validateStationBody(successfulStationBody));
        assertThrows(ErrorResponseException.class, () -> stationHandler.validateStationBody(failedStationBody));
    }

    @Test
    @Transactional
    public void testCreateStationFromRequest() {
        StationHandler.StationBody stationBody = new StationHandler.StationBody(
                "Random" + random.nextInt(150) + 10,
                random.nextInt(100) + 100
        );

        try {
            Station station = stationHandler.createStationFromRequest(stationBody);
            assertTrue(stationRepository.existsById(station.getId()));
        } catch (ErrorResponseException exception) {
            fail("Did not expect exception " + exception.getErrorResponse().getMessage());
        }
    }

    @Test
    @Transactional
    public void testUpdateStationFromRequest() {
        int originalMaxPassengers = random.nextInt(100) + 100;
        String originalName = "Originalstation";

        StationHandler.StationBody previousStationBody = new StationHandler.StationBody(
                originalName,
                originalMaxPassengers
        );

        try {
            Station station = stationHandler.createStationFromRequest(previousStationBody);

            assertEquals(originalName, station.getName());
            assertEquals(originalMaxPassengers, station.getMaxPassengers());

            String newName = "Neustation";
            int newMaxPassengers = random.nextInt(100) + 100;

            StationHandler.StationBody newStationBody = new StationHandler.StationBody(
                    newName,
                    newMaxPassengers
            );

            stationHandler.updateStationFromRequest(station, newStationBody);

            assertEquals(newName, station.getName());
            assertEquals(newMaxPassengers, station.getMaxPassengers());
            assertTrue(stationRepository.existsById(station.getId()));
        } catch (ErrorResponseException exception) {
            fail("Did not expect exception " + exception.getErrorResponse().getMessage());
        }
    }

}

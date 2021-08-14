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
                random.nextInt(100) + 100,
                random.nextInt(100) + 100,
                random.nextFloat(),
                "Random" + random.nextInt(150) + 10,
                0
        );

        StationHandler.StationBody failedStationBody = new StationHandler.StationBody(-1, -1, -1, "", -1);

        assertDoesNotThrow(() -> stationHandler.validateStationBody(successfulStationBody));
        assertThrows(ErrorResponseException.class, () -> stationHandler.validateStationBody(failedStationBody));
    }

    @Test
    @Transactional
    public void testCreateStationFromRequest() {
        StationHandler.StationBody stationBody = new StationHandler.StationBody(
                random.nextInt(100) + 100,
                random.nextInt(100) + 100,
                random.nextFloat(),
                "Random" + random.nextInt(150) + 10,
                0
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
        int originalLength = random.nextInt(100) + 100;
        String originalName = "Originalstation";

        StationHandler.StationBody previousStationBody = new StationHandler.StationBody(
                originalLength,
                random.nextInt(100) + 100,
                random.nextFloat(),
                originalName,
                0
        );

        try {
            Station station = stationHandler.createStationFromRequest(previousStationBody);

            assertEquals(originalName, station.getName());
            assertEquals(originalLength, station.getLength());

            int newLength = random.nextInt(100) + 100;
            String newName = "Neustation";

            StationHandler.StationBody newStationBody = new StationHandler.StationBody(
                    newLength,
                    random.nextInt(100) + 100,
                    random.nextFloat(),
                    newName,
                    0
            );

            stationHandler.updateStationFromRequest(station, newStationBody);

            assertEquals(newName, station.getName());
            assertEquals(newLength, station.getLength());
            assertTrue(stationRepository.existsById(station.getId()));
        } catch (ErrorResponseException exception) {
            fail("Did not expect exception " + exception.getErrorResponse().getMessage());
        }
    }

}

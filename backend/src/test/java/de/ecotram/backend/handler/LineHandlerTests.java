package de.ecotram.backend.handler;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.ConnectionRepository;
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
    private ConnectionRepository connectionRepository;
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
    public void testValidateStationIds() {
        this.addStations();

        ArrayList<Long> inputIds = new ArrayList<>(Arrays.asList(
                station1.getId(),
                station2.getId(),
                station3.getId(),
                station4.getId(),
                station5.getId()
        ));

        LineHandler.ValidationResult validationResult = lineHandler.validateStationIds(inputIds);

        assertEquals(inputIds.size(), validationResult.getStations().size());
        assertEquals(inputIds.size() - 1, validationResult.getConnections().size());

        ArrayList<Long> invalidInputIds = new ArrayList<>(Arrays.asList(
                station1.getId(),
                station3.getId(),
                station2.getId(),
                random.nextInt(1000) + 1000L,
                random.nextInt(1000) + 1000L));

        int invalidExpectedStations = 3;
        int invalidExpectedConnections = 2;

        LineHandler.ValidationResult invalidValidationResult = lineHandler.validateStationIds(invalidInputIds);

        assertEquals(invalidExpectedStations, invalidValidationResult.getStations().size());
        assertEquals(invalidExpectedConnections, invalidValidationResult.getConnections().size());
    }

    @Test
    public void testValidateLineBody() {
        this.addStations();

        LineHandler.LineBody successfulLineBody = new LineHandler.LineBody(
                "Testline",
                station1.getId() + "," +
                        station2.getId() + "," +
                        station3.getId() + "," +
                        station4.getId()
        );

        LineHandler.LineBody failedLineBody = new LineHandler.LineBody("", "");

        assertDoesNotThrow(() -> lineHandler.validateLineBody(successfulLineBody));
        assertThrows(ErrorResponseException.class, () -> lineHandler.validateLineBody(failedLineBody));
    }

    @Test
    @Transactional
    public void testCreateLineFromRequest() {
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
            assertTrue(lineRepository.existsById(line.getId()));
        } catch (ErrorResponseException exception) {
            fail("Did not expect exception " + exception.getErrorResponse().getMessage());
        }
    }

    @Test
    @Transactional
    public void testUpdateLineFromRequest() {
        this.addStations();

        String originalName = "Originallinie";
        LineHandler.LineBody previousLineBody = new LineHandler.LineBody(
                originalName,
                station1.getId() + "," +
                        station2.getId()
        );

        try {
            Line line = lineHandler.createLineFromRequest(previousLineBody);

            assertEquals(originalName, line.getName());
            assertEquals(2, line.getRoute().size());

            String newName = "Neulinie";
            LineHandler.LineBody newLineBody = new LineHandler.LineBody(newName,
                    station3.getId() + "," +
                            station4.getId() + "," +
                            station5.getId()
            );

            lineHandler.updateLineFromRequest(line, newLineBody);

            assertEquals(newName, line.getName());
            assertEquals(3, line.getRoute().size());
            assertTrue(lineRepository.existsById(line.getId()));
        } catch (ErrorResponseException exception) {
            fail("Did not expect exception " + exception.getErrorResponse().getMessage());
        }
    }

    @Test
    @Transactional
    public void testDeleteLine() {
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
            assertTrue(lineRepository.existsById(line.getId()));

            lineHandler.deleteLine(line);
            assertFalse(lineRepository.existsById(line.getId()));
        } catch (ErrorResponseException exception) {
            fail("Did not expect exception " + exception.getErrorResponse().getMessage());
        }
    }

}

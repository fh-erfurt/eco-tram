package de.ecotram.backend.handler;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.ConnectionRepository;
import de.ecotram.backend.repository.LineEntryRepository;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active = test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LineHandlerTests {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private LineEntryRepository lineEntryRepository;

    @Autowired
    private LineHandler lineHandler;

    @Autowired
    private EntityManager entityManager;

    public final Station station1 = new Station();
    public final Station station2 = new Station();
    public final Station station3 = new Station();
    public final Station station4 = new Station();
    public final Station station5 = new Station();

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

        ArrayList<Long> inputIds = new ArrayList<>(Arrays.asList(1L,2L,3L,4L,5L));

        LineHandler.ValidationResult validationResult = lineHandler.validateStationIds(inputIds);

        assertEquals(validationResult.getStations().size(), inputIds.size());
        assertEquals(validationResult.getConnections().size(), inputIds.size() - 1);

        ArrayList<Long> invalidInputIds = new ArrayList<>(Arrays.asList(1L,3L,5L,9L,32L));

        int invalidExpectedStations = 3;
        int invalidExpectedConnections = 2;

        LineHandler.ValidationResult invalidValidationResult = lineHandler.validateStationIds(invalidInputIds);

        assertEquals(invalidValidationResult.getStations().size(), invalidExpectedStations);
        assertEquals(invalidValidationResult.getConnections().size(), invalidExpectedConnections);
    }

//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class LineBody {
//        @Getter
//        private String name;
//
//        @Getter
//        private String stationIds;
//
//        public Line applyToLine(Line line) {
//            line.setName(name);
//
//            return line;
//        }
//
//        public Line applyToLine() {
//            return applyToLine(new Line());
//        }
//    }

    @Test
    public void testValidateLineBody() {
        this.addStations();

        LineHandler.LineBody successfulLineBody = new LineHandler.LineBody("Testline", "1,2,3,4,5");
        LineHandler.LineBody failedLineBody = new LineHandler.LineBody("", "");

        assertDoesNotThrow(() -> lineHandler.validateLineBody(successfulLineBody));
        assertThrows(ErrorResponseException.class, () -> lineHandler.validateLineBody(failedLineBody));
    }

    @Test
    @Transactional
    public void testCreateLineFromRequest() {
        this.addStations();

        LineHandler.LineBody lineBody = new LineHandler.LineBody("Testline", "1,2,3,4,5");

        try {
            Line line = lineHandler.createLineFromRequest(lineBody);
            assertTrue(lineRepository.existsById(line.getId()));
        } catch(ErrorResponseException exception) {
            fail("Did not expect exception " + exception.getErrorResponse().getMessage());
        }
    }

    @Test
    @Transactional
    public void testUpdateLineFromRequest() {
        this.addStations();

        String originalName = "Originallinie";
        LineHandler.LineBody previousLineBody = new LineHandler.LineBody(originalName, "1,2");

        try {
            Line line = lineHandler.createLineFromRequest(previousLineBody);

            assertEquals(line.getName(), originalName);
            assertEquals(line.getRoute().size(), 2);

            String newName = "Neulinie";
            LineHandler.LineBody newLineBody = new LineHandler.LineBody(newName, "3,4,5");

            lineHandler.updateLineFromRequest(line, newLineBody);

            assertEquals(line.getName(), newName);
            assertEquals(line.getRoute().size(), 3);
            assertTrue(lineRepository.existsById(line.getId()));
        } catch(ErrorResponseException exception) {
            fail("Did not expect exception " + exception.getErrorResponse().getMessage());
        }
    }

    @Test
    @Transactional
    public void testDeleteLine() {
        this.addStations();

        LineHandler.LineBody lineBody = new LineHandler.LineBody("Testline", "1,2,3,4,5");

        try {
            Line line = lineHandler.createLineFromRequest(lineBody);
            assertTrue(lineRepository.existsById(line.getId()));

            lineHandler.deleteLine(line);
            assertFalse(lineRepository.existsById(line.getId()));
        } catch(ErrorResponseException exception) {
            fail("Did not expect exception " + exception.getErrorResponse().getMessage());
        }
    }

}

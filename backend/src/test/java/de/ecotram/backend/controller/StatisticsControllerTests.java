package de.ecotram.backend.controller;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.Tram;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.ConnectionRepository;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.repository.PassengerTramRepository;
import de.ecotram.backend.repository.StationRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.profiles.active = test", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StatisticsControllerTests {
    private final Connection connection1 = new Connection();
    private final Connection connection2 = new Connection();
    private final Line line1 = new Line();
    private final Line line2 = new Line();
    private final Line line3 = new Line();
    private final Tram tram1 = new Tram();
    private final Tram tram2 = new Tram();
    private final Tram tram3 = new Tram();
    private final Tram tram4 = new Tram();
    private final Station station1 = new Station();
    private final Station station2 = new Station();
    private final Station station3 = new Station();
    private final Station station4 = new Station();
    private final Station station5 = new Station();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private PassengerTramRepository passengerTramRepository;

    @Autowired
    private StationRepository stationRepository;

    private String getHostnameWithPort() {
        return "http://localhost:" + port;
    }

    @Test
    public void testStatisticsConnections() throws JSONException {
        connectionRepository.save(connection1);
        connectionRepository.save(connection2);

        String response = restTemplate.getForObject(getHostnameWithPort() + "/statistics/connections", String.class);
        JSONObject jsonObject = new JSONObject(response);

        assertTrue(jsonObject.has("type"));
        assertTrue(jsonObject.has("results"));

        assertEquals("connection", jsonObject.getString("type"));
        assertEquals(2, jsonObject.getLong("results"));
    }

    @Test
    public void testStatisticsLines() throws JSONException {
        lineRepository.save(line1);
        lineRepository.save(line2);
        lineRepository.save(line3);

        String response = restTemplate.getForObject(getHostnameWithPort() + "/statistics/line", String.class);
        JSONObject jsonObject = new JSONObject(response);

        assertTrue(jsonObject.has("type"));
        assertTrue(jsonObject.has("results"));

        assertEquals("line", jsonObject.getString("type"));
        assertEquals(3, jsonObject.getLong("results"));
    }

    @Test
    public void testStatisticsPassengerTrams() throws JSONException {
        passengerTramRepository.save(tram1);
        passengerTramRepository.save(tram2);
        passengerTramRepository.save(tram3);
        passengerTramRepository.save(tram4);

        String response = restTemplate.getForObject(
                getHostnameWithPort() + "/statistics/passenger-tram",
                String.class);

        JSONObject jsonObject = new JSONObject(response);

        assertTrue(jsonObject.has("type"));
        assertTrue(jsonObject.has("results"));

        assertEquals("passengerTram", jsonObject.getString("type"));
        assertEquals(4, jsonObject.getLong("results"));
    }

    @Test
    public void testStatisticsStations() throws JSONException {
        stationRepository.save(station1);
        stationRepository.save(station2);
        stationRepository.save(station3);
        stationRepository.save(station4);
        stationRepository.save(station5);

        String response = restTemplate.getForObject(getHostnameWithPort() + "/statistics/station", String.class);
        JSONObject jsonObject = new JSONObject(response);

        assertTrue(jsonObject.has("type"));
        assertTrue(jsonObject.has("results"));

        assertEquals("station", jsonObject.getString("type"));
        assertEquals(5, jsonObject.getLong("results"));
    }

    @Test
    public void testStatistics() throws JSONException {
        connectionRepository.save(connection1);
        connectionRepository.save(connection2);

        lineRepository.save(line1);
        lineRepository.save(line2);
        lineRepository.save(line3);

        passengerTramRepository.save(tram1);
        passengerTramRepository.save(tram2);
        passengerTramRepository.save(tram3);
        passengerTramRepository.save(tram4);

        stationRepository.save(station1);
        stationRepository.save(station2);
        stationRepository.save(station3);
        stationRepository.save(station4);
        stationRepository.save(station5);

        String response = restTemplate.getForObject(getHostnameWithPort() + "/statistics", String.class);
        JSONObject jsonObject = new JSONObject(response);

        assertTrue(jsonObject.has("connections"));
        assertTrue(jsonObject.has("lines"));
        assertTrue(jsonObject.has("passengerTrams"));
        assertTrue(jsonObject.has("stations"));

        assertEquals(2, jsonObject.getJSONObject("connections").getLong("results"));
        assertEquals(3, jsonObject.getJSONObject("lines").getLong("results"));
        assertEquals(4, jsonObject.getJSONObject("passengerTrams").getLong("results"));
        assertEquals(5, jsonObject.getJSONObject("stations").getLong("results"));
    }

}

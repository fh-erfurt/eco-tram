package de.ecotram.backend.controller;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.repository.StationRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active = test", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LineControllerTests {

    public final Line line1 = new Line();
    public final Line line2 = new Line();
    public final Line line3 = new Line();
    public final Line line4 = new Line();
    public final Line line5 = new Line();
    public final Station station1 = new Station();
    public final Station station2 = new Station();
    public final Station station3 = new Station();
    public final Station station4 = new Station();
    public final Station station5 = new Station();
    private final Random random = new Random();
    @Autowired
    public LineRepository lineRepository;
    @Autowired
    public StationRepository stationRepository;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    private String getHostnameWithPort() {
        return "http://localhost:" + port;
    }

    private void addLines() {
        lineRepository.save(line1);
        lineRepository.save(line2);
        lineRepository.save(line3);
        lineRepository.save(line4);
        lineRepository.save(line5);
    }

    private void addStations() {
        stationRepository.save(station1);
        stationRepository.save(station2);
        stationRepository.save(station3);
        stationRepository.save(station4);
        stationRepository.save(station5);
    }

    @Test
    public void testLinesList() throws JSONException {
        String testNameValue = "Random" + random.nextInt(150) + 10;
        line1.setName(testNameValue);

        addLines();

        String response = restTemplate.getForObject(getHostnameWithPort() + "/lines/list", String.class);
        JSONObject jsonObject = new JSONObject(response);

        assertTrue(jsonObject.has("results"));
        assertTrue(jsonObject.has("count"));
        assertEquals(5, jsonObject.getInt("count"));

        JSONArray results = jsonObject.getJSONArray("results");
        boolean itemFound = false;

        for (int i = 0; i < results.length(); i++) {
            JSONObject item = results.getJSONObject(i);
            int id = item.getInt("id");
            String name = item.getString("name");

            if (id == line1.getId()) {
                assertEquals(testNameValue, name);
                itemFound = true;
            }
        }

        assertTrue(itemFound, "Check that first line item is listed");
    }

    @Test
    public void testLinesGet() throws JSONException {
        addLines();

        List<Line> lines = lineRepository.findAll();
        Line randomLine = lines.get(random.nextInt(lines.size()));

        randomLine.setName("Random" + random.nextInt(150) + 10);
        lineRepository.save(randomLine);

        ResponseEntity<String> response = restTemplate.exchange(getHostnameWithPort() + "/lines/get/" + randomLine.getId(), HttpMethod.GET, null, String.class);
        assertEquals(200, response.getStatusCodeValue());

        JSONObject jsonObject = new JSONObject(response.getBody());

        int id = jsonObject.getInt("id");
        String name = jsonObject.getString("name");

        assertEquals(id, randomLine.getId());
        assertEquals(name, randomLine.getName());

        ResponseEntity<String> response404 = restTemplate.exchange(getHostnameWithPort() + "/lines/get/" + random.nextInt(1000) + 1000, HttpMethod.GET, null, String.class);
        assertEquals(404, response404.getStatusCodeValue(), "Check if invalid value returns 404");
    }

    @Test
    public void testLinesNew() throws JSONException {
        addStations();

        String testNameValue = "Random" + random.nextInt(150) + 10;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", testNameValue);
        jsonObject.put("stationIds", station1.getId() + "," + station2.getId() + "," + station3.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(getHostnameWithPort() + "/lines/new", HttpMethod.POST, entity, String.class);

        assertEquals(200, response.getStatusCodeValue());

        JSONObject lineObject = new JSONObject(response.getBody());

        assertTrue(lineObject.has("name"));
        assertTrue(lineObject.has("route"));
        assertEquals(testNameValue, lineObject.getString("name"));
        assertEquals(3, lineObject.getJSONArray("route").length());
    }

    @Test
    public void testConnectionsUpdate() throws JSONException {
        addLines();
        addStations();

        List<Line> lines = lineRepository.findAll();
        Line randomLine = lines.get(random.nextInt(lines.size()));

        randomLine.setName("Random" + random.nextInt(150) + 10);
        lineRepository.save(randomLine);

        String testNameValue = "Random" + random.nextInt(150) + 10;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", testNameValue);
        jsonObject.put("stationIds", station1.getId() + "," + station2.getId() + "," + station3.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(getHostnameWithPort() + "/lines/update/" + randomLine.getId(), HttpMethod.POST, entity, String.class);

        assertEquals(200, response.getStatusCodeValue());

        JSONObject lineObject = new JSONObject(response.getBody());

        assertEquals(randomLine.getId(), lineObject.getInt("id"));

        assertTrue(lineObject.has("name"));
        assertTrue(lineObject.has("route"));
        assertEquals(testNameValue, lineObject.getString("name"));
        assertEquals(3, lineObject.getJSONArray("route").length());
    }

    @Test
    public void testLinesDelete() {
        addLines();

        List<Line> lines = lineRepository.findAll();
        Line randomLine = lines.get(random.nextInt(lines.size()));

        ResponseEntity<String> response = restTemplate.exchange(getHostnameWithPort() + "/lines/delete/" + randomLine.getId(), HttpMethod.POST, null, String.class);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("OK", response.getBody());
        assertFalse(lineRepository.existsById(randomLine.getId()));
    }
}

package de.ecotram.backend.controller;

import de.ecotram.backend.entity.network.Station;
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
public class StationControllerTests {
    public final Station station1 = new Station();
    public final Station station2 = new Station();
    public final Station station3 = new Station();
    public final Station station4 = new Station();
    public final Station station5 = new Station();
    private final Random random = new Random();

    @Autowired
    public StationRepository stationRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getHostnameWithPort() {
        return "http://localhost:" + port;
    }

    private void addStations() {
        stationRepository.save(station1);
        stationRepository.save(station2);
        stationRepository.save(station3);
        stationRepository.save(station4);
        stationRepository.save(station5);
    }

    @Test
    public void testStationsList() throws JSONException {
        String testNameValue = "Random" + random.nextInt(150) + 10;
        station1.setName(testNameValue);

        addStations();

        String response = restTemplate.getForObject(getHostnameWithPort() + "/stations/list", String.class);
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

            if (id == station1.getId()) {
                assertEquals(testNameValue, name);
                itemFound = true;
            }
        }

        assertTrue(itemFound, "Check that first station item is listed");
    }

    @Test
    public void testStationsGet() throws JSONException {
        addStations();

        List<Station> stations = stationRepository.findAll();
        Station randomStation = stations.get(random.nextInt(stations.size()));

        randomStation.setName("Random" + random.nextInt(150) + 10);
        stationRepository.save(randomStation);

        ResponseEntity<String> response = restTemplate.exchange(
                getHostnameWithPort() + "/stations/get/" + randomStation.getId(),
                HttpMethod.GET,
                null,
                String.class);

        assertEquals(200, response.getStatusCodeValue());

        JSONObject jsonObject = new JSONObject(response.getBody());

        int id = jsonObject.getInt("id");
        String name = jsonObject.getString("name");

        assertEquals(id, randomStation.getId());
        assertEquals(name, randomStation.getName());

        ResponseEntity<String> response404 = restTemplate.exchange(
                getHostnameWithPort() + "/stations/get/" + random.nextInt(1000) + 1000,
                HttpMethod.GET,
                null,
                String.class);

        assertEquals(404, response404.getStatusCodeValue(), "Check if invalid value returns 404");
    }

    @Test
    public void testStationsNew() throws JSONException {
        addStations();

        String testNameValue = "Random" + random.nextInt(150) + 10;
        int testMaxPassengersValue = random.nextInt(150) + 10;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", testNameValue);
        jsonObject.put("maxPassengers", testMaxPassengersValue);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getHostnameWithPort() + "/stations/new",
                HttpMethod.POST,
                entity,
                String.class);

        assertEquals(200, response.getStatusCodeValue());

        JSONObject stationObject = new JSONObject(response.getBody());

        assertTrue(stationObject.has("name"));
        assertTrue(stationObject.has("maxPassengers"));
        assertEquals(testNameValue, stationObject.getString("name"));
        assertEquals(testMaxPassengersValue, stationObject.getInt("maxPassengers"));
    }

    @Test
    public void testStationsUpdate() throws JSONException {
        addStations();

        List<Station> stations = stationRepository.findAll();
        Station randomStation = stations.get(random.nextInt(stations.size()));

        randomStation.setName("Random" + random.nextInt(150) + 10);
        stationRepository.save(randomStation);

        String testNameValue = "Random" + random.nextInt(150) + 10;
        int testMaxPassengersValue = random.nextInt(150) + 10;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", testNameValue);
        jsonObject.put("maxPassengers", testMaxPassengersValue);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getHostnameWithPort() + "/stations/update/" + randomStation.getId(),
                HttpMethod.POST,
                entity,
                String.class);

        assertEquals(200, response.getStatusCodeValue());

        JSONObject stationObject = new JSONObject(response.getBody());

        assertEquals(randomStation.getId(), stationObject.getInt("id"));

        assertTrue(stationObject.has("name"));
        assertTrue(stationObject.has("maxPassengers"));
        assertEquals(testNameValue, stationObject.getString("name"));
        assertEquals(testMaxPassengersValue, stationObject.getInt("maxPassengers"));
    }

    @Test
    public void testStationsDelete() {
        addStations();

        List<Station> stations = stationRepository.findAll();
        Station randomStation = stations.get(random.nextInt(stations.size()));

        ResponseEntity<String> response = restTemplate.exchange(
                getHostnameWithPort() + "/stations/delete/" + randomStation.getId(),
                HttpMethod.POST,
                null,
                String.class);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("OK", response.getBody());
        assertFalse(stationRepository.existsById(randomStation.getId()));
    }
}
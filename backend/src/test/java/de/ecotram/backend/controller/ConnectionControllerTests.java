package de.ecotram.backend.controller;

import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.ConnectionRepository;
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
public class ConnectionControllerTests {

    public final Connection connection1 = new Connection();
    public final Connection connection2 = new Connection();
    public final Connection connection3 = new Connection();
    public final Connection connection4 = new Connection();
    public final Connection connection5 = new Connection();
    public final Station station1 = new Station();
    public final Station station2 = new Station();
    public final Station station3 = new Station();
    public final Station station4 = new Station();
    public final Station station5 = new Station();
    private final Random random = new Random();
    @Autowired
    public ConnectionRepository connectionRepository;
    @Autowired
    public StationRepository stationRepository;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    private String getHostnameWithPort() {
        return "http://localhost:" + port;
    }

    private void addConnections() {
        connectionRepository.save(connection1);
        connectionRepository.save(connection2);
        connectionRepository.save(connection3);
        connectionRepository.save(connection4);
        connectionRepository.save(connection5);
    }

    private void addStations() {
        stationRepository.save(station1);
        stationRepository.save(station2);
        stationRepository.save(station3);
        stationRepository.save(station4);
        stationRepository.save(station5);
    }

    @Test
    public void testConnectionsList() throws JSONException {
        int testLengthValue = random.nextInt(150) + 10;
        connection1.setLength(testLengthValue);

        addConnections();

        String response = restTemplate.getForObject(getHostnameWithPort() + "/connections/list", String.class);
        JSONObject jsonObject = new JSONObject(response);

        assertTrue(jsonObject.has("results"));
        assertTrue(jsonObject.has("count"));
        assertEquals(5, jsonObject.getInt("count"));

        JSONArray results = jsonObject.getJSONArray("results");
        boolean itemFound = false;

        for (int i = 0; i < results.length(); i++) {
            JSONObject item = results.getJSONObject(i);
            int id = item.getInt("id");
            int length = item.getInt("length");

            if (id == connection1.getId()) {
                assertEquals(testLengthValue, length);
                itemFound = true;
            }
        }

        assertTrue(itemFound, "Check that first connection item is listed");
    }

    @Test
    public void testConnectionsGet() throws JSONException {
        addConnections();

        List<Connection> connections = connectionRepository.findAll();
        Connection randomConnection = connections.get(random.nextInt(connections.size()));

        ResponseEntity<String> response = restTemplate.exchange(getHostnameWithPort() + "/connections/get/" + randomConnection.getId(), HttpMethod.GET, null, String.class);
        assertEquals(200, response.getStatusCodeValue());

        JSONObject jsonObject = new JSONObject(response.getBody());

        int id = jsonObject.getInt("id");
        int length = jsonObject.getInt("length");

        assertEquals(id, randomConnection.getId());
        assertEquals(length, randomConnection.getLength());

        ResponseEntity<String> response404 = restTemplate.exchange(getHostnameWithPort() + "/connections/get/" + random.nextInt(1000) + 1000, HttpMethod.GET, null, String.class);
        assertEquals(404, response404.getStatusCodeValue(), "Check if invalid value returns 404");
    }

    @Test
    public void testConnectionsNew() throws JSONException {
        addStations();

        List<Station> stations = stationRepository.findAll();
        Station randomStation1 = stations.get(random.nextInt(stations.size()));
        Station randomStation2 = stations.get(random.nextInt(stations.size()));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sourceStationId", randomStation1.getId());
        jsonObject.put("destinationStationId", randomStation2.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(getHostnameWithPort() + "/connections/new", HttpMethod.POST, entity, String.class);

        assertEquals(200, response.getStatusCodeValue());

        JSONObject stationObject = new JSONObject(response.getBody());

        assertTrue(stationObject.has("sourceStation"));
        assertTrue(stationObject.has("destinationStation"));

        assertEquals(randomStation1.getId(), stationObject.getJSONObject("sourceStation").getInt("id"));
        assertEquals(randomStation2.getId(), stationObject.getJSONObject("destinationStation").getInt("id"));
    }

    @Test
    public void testConnectionsUpdate() throws JSONException {
        addStations();

        List<Station> stations = stationRepository.findAll();
        Station randomStation1 = stations.get(random.nextInt(stations.size()));
        Station randomStation2 = stations.get(random.nextInt(stations.size()));

        connection1.setSourceStation(randomStation1);
        connection1.setDestinationStation(randomStation2);

        addConnections();

        Station newRandomStation1 = stations.get(random.nextInt(stations.size()));
        Station newRandomStation2 = stations.get(random.nextInt(stations.size()));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sourceStationId", newRandomStation1.getId());
        jsonObject.put("destinationStationId", newRandomStation2.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(getHostnameWithPort() + "/connections/update/" + connection1.getId(), HttpMethod.POST, entity, String.class);

        assertEquals(200, response.getStatusCodeValue());

        JSONObject stationObject = new JSONObject(response.getBody());

        assertEquals(connection1.getId(), stationObject.getInt("id"));

        assertTrue(stationObject.has("sourceStation"));
        assertTrue(stationObject.has("destinationStation"));

        assertEquals(newRandomStation1.getId(), stationObject.getJSONObject("sourceStation").getInt("id"));
        assertEquals(newRandomStation2.getId(), stationObject.getJSONObject("destinationStation").getInt("id"));
    }

    @Test
    public void testConnectionsDelete() {
        addConnections();

        List<Connection> connections = connectionRepository.findAll();
        Connection randomConnection = connections.get(random.nextInt(connections.size()));

        ResponseEntity<String> response = restTemplate.exchange(getHostnameWithPort() + "/connections/delete/" + randomConnection.getId(), HttpMethod.POST, null, String.class);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("OK", response.getBody());
        assertFalse(connectionRepository.existsById(randomConnection.getId()));
    }

}

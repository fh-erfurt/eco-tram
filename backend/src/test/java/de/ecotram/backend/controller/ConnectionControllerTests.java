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
	public void connections_list() throws JSONException {
		int testLengthValue = random.nextInt(150) + 10;
		connection1.setLength(testLengthValue);

		addConnections();

		String response = restTemplate.getForObject(getHostnameWithPort() + "/connections/list", String.class);
		JSONObject jsonObject = new JSONObject(response);

		assertTrue(jsonObject.has("results"), "result should have a results entry");
		assertTrue(jsonObject.has("count"), "result should have a count entry");
		assertEquals(5, jsonObject.getInt("count"), "count entry should have the value 5");

		JSONArray results = jsonObject.getJSONArray("results");
		boolean itemFound = false;

		for(int i = 0; i < results.length(); i++) {
			JSONObject item = results.getJSONObject(i);
			int id = item.getInt("id");
			int length = item.getInt("length");

			if(id == connection1.getId()) {
				assertEquals(testLengthValue, length, "Length of the found connection 1 entry should be " + testLengthValue);
				itemFound = true;
			}
		}

		assertTrue(itemFound, "Check that first connection item is listed");
	}

	@Test
	public void connections_get() throws JSONException {
		addConnections();

		List<Connection> connections = connectionRepository.findAll();
		Connection randomConnection = connections.get(random.nextInt(connections.size()));

		ResponseEntity<String> response = restTemplate.exchange(
				getHostnameWithPort() + "/connections/get/" + randomConnection.getId(),
				HttpMethod.GET,
				null,
				String.class);

		assertEquals(200, response.getStatusCodeValue(), "response's status code should be 200");

		JSONObject jsonObject = new JSONObject(response.getBody());

		int id = jsonObject.getInt("id");
		int length = jsonObject.getInt("length");

		assertEquals(id, randomConnection.getId(), "random connections id should be " + id);
		assertEquals(length, randomConnection.getLength(), "random connections length should be " + length);

		ResponseEntity<String> response404 = restTemplate.exchange(
				getHostnameWithPort() + "/connections/get/" + random.nextInt(1000) + 1000,
				HttpMethod.GET,
				null,
				String.class);

		assertEquals(404, response404.getStatusCodeValue(), "Invalid requests response status code should be 404");
	}

	@Test
	public void connections_new() throws JSONException {
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

		ResponseEntity<String> response = restTemplate.exchange(
				getHostnameWithPort() + "/connections/new",
				HttpMethod.POST,
				entity, String.class);

		assertEquals(200, response.getStatusCodeValue(), "response's status code should be 200");

		JSONObject stationObject = new JSONObject(response.getBody());

		assertTrue(stationObject.has("sourceStation"), "response should have an sourceStation entry");
		assertTrue(stationObject.has("destinationStation"), "response should have an destinationStation entry");

		assertEquals(randomStation1.getId(), stationObject.getJSONObject("sourceStation").getInt("id"), "sourceStation should have the id " + randomStation1.getId());
		assertEquals(randomStation2.getId(), stationObject.getJSONObject("destinationStation").getInt("id"), "destinationStation should have the id " + randomStation2.getId());
	}

	@Test
	public void connections_update() throws JSONException {
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

		ResponseEntity<String> response = restTemplate.exchange(
				getHostnameWithPort() + "/connections/update/" + connection1.getId(),
				HttpMethod.POST, entity,
				String.class);

		assertEquals(200, response.getStatusCodeValue(), "response's status code should be 200");

		JSONObject stationObject = new JSONObject(response.getBody());

		assertEquals(connection1.getId(), stationObject.getInt("id"), "objects id should be " + connection1.getId());

		assertTrue(stationObject.has("sourceStation"), "response should have a sourceStation entry");
		assertTrue(stationObject.has("destinationStation"), "response should have a destinationStation entry");

		assertEquals(newRandomStation1.getId(), stationObject.getJSONObject("sourceStation").getInt("id"), "sourceStation id should be " + newRandomStation1.getId());
		assertEquals(newRandomStation2.getId(), stationObject.getJSONObject("destinationStation").getInt("id"), "destinationStation id should be " + newRandomStation2.getId());
	}

	@Test
	public void connections_delete() {
		addConnections();

		List<Connection> connections = connectionRepository.findAll();
		Connection randomConnection = connections.get(random.nextInt(connections.size()));

		ResponseEntity<String> response = restTemplate.exchange(
				getHostnameWithPort() + "/connections/delete/" + randomConnection.getId(),
				HttpMethod.POST,
				null,
				String.class);

		assertEquals(200, response.getStatusCodeValue(), "response's status code should be 200");
		assertEquals("OK", response.getBody(), "response's body should be 'OK'");
		assertFalse(connectionRepository.existsById(randomConnection.getId()), "Connection " + randomConnection.getId() + " should be deleted from database");
	}
}
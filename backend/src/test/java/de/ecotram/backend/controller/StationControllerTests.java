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
	public void stations_list() throws JSONException {
		String testNameValue = "Random" + random.nextInt(150) + 10;
		station1.setName(testNameValue);

		addStations();

		String response = restTemplate.getForObject(getHostnameWithPort() + "/stations/list", String.class);
		JSONObject jsonObject = new JSONObject(response);

		assertTrue(jsonObject.has("results"), "response should have the entry results");
		assertTrue(jsonObject.has("count"), "response should have the entry count");
		assertEquals(5, jsonObject.getInt("count"), "response's entry count should have the value 5");

		JSONArray results = jsonObject.getJSONArray("results");
		boolean itemFound = false;

		for(int i = 0; i < results.length(); i++) {
			JSONObject item = results.getJSONObject(i);
			int id = item.getInt("id");
			String name = item.getString("name");

			if(id == station1.getId()) {
				assertEquals(testNameValue, name, "found station1 should have the name " + testNameValue);
				itemFound = true;
			}
		}

		assertTrue(itemFound, "Check that first station item is listed");
	}

	@Test
	public void stations_get() throws JSONException {
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

		assertEquals(200, response.getStatusCodeValue(), "response's status code should be 200");

		JSONObject jsonObject = new JSONObject(response.getBody());

		int id = jsonObject.getInt("id");
		String name = jsonObject.getString("name");

		assertEquals(id, randomStation.getId(), "response's id should be " + id);
		assertEquals(name, randomStation.getName(), "response's name should be " + name);

		ResponseEntity<String> response404 = restTemplate.exchange(
				getHostnameWithPort() + "/stations/get/" + random.nextInt(1000) + 1000,
				HttpMethod.GET,
				null,
				String.class);

		assertEquals(404, response404.getStatusCodeValue(), "Check if invalid value returns 404");
	}

	@Test
	public void stations_new() throws JSONException {
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

		assertEquals(200, response.getStatusCodeValue(), "response's status code should be 200");

		JSONObject stationObject = new JSONObject(response.getBody());

		assertTrue(stationObject.has("name"), "response should have the entry name");
		assertTrue(stationObject.has("maxPassengers"), "response should have the entry maxPassengers");
		assertEquals(testNameValue, stationObject.getString("name"), "response's entry name should have the value " + testNameValue);
		assertEquals(testMaxPassengersValue, stationObject.getInt("maxPassengers"), "response's entry maxPassengers should the value " + testMaxPassengersValue);
	}

	@Test
	public void stations_update() throws JSONException {
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

		assertEquals(200, response.getStatusCodeValue(), "response's status code should be 200");

		JSONObject stationObject = new JSONObject(response.getBody());

		assertEquals(randomStation.getId(), stationObject.getInt("id"), "response should have the id " + randomStation.getId());

		assertTrue(stationObject.has("name"), "response should have the entry name");
		assertTrue(stationObject.has("maxPassengers"), "response should have the entry maxPassengers");
		assertEquals(testNameValue, stationObject.getString("name"), "response's entry should have the value " + testNameValue);
		assertEquals(testMaxPassengersValue, stationObject.getInt("maxPassengers"), "response's entry should have the value " + testMaxPassengersValue);
	}

	@Test
	public void stations_delete() {
		addStations();

		List<Station> stations = stationRepository.findAll();
		Station randomStation = stations.get(random.nextInt(stations.size()));

		ResponseEntity<String> response = restTemplate.exchange(
				getHostnameWithPort() + "/stations/delete/" + randomStation.getId(),
				HttpMethod.POST,
				null,
				String.class);

		assertEquals(200, response.getStatusCodeValue(), "response's status code should be 200");
		assertEquals("OK", response.getBody(), "response's body should be 'OK'");
		assertFalse(stationRepository.existsById(randomStation.getId()), "random station should have been deleted from database");
	}
}
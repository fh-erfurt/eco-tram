package de.ecotram.backend.controller;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.ConnectionRepository;
import de.ecotram.backend.repository.LineRepository;
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
	private final Station station1 = new Station();
	private final Station station2 = new Station();
	private final Station station3 = new Station();
	private final Station station4 = new Station();

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ConnectionRepository connectionRepository;

	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private StationRepository stationRepository;

	private String getHostnameWithPort() {
		return "http://localhost:" + port;
	}

	@Test
	public void statistics_connections() throws JSONException {
		connectionRepository.save(connection1);
		connectionRepository.save(connection2);

		String response = restTemplate.getForObject(getHostnameWithPort() + "/statistics/connections", String.class);
		JSONObject jsonObject = new JSONObject(response);

		assertTrue(jsonObject.has("type"), "response should have the entry type");
		assertTrue(jsonObject.has("results"), "response should have the entry results");

		assertEquals("connection", jsonObject.getString("type"), "response's entry type should have the value connection");
		assertEquals(2, jsonObject.getLong("results"), "response's entry results should have 2 items");
	}

	@Test
	public void statistics_lines() throws JSONException {
		lineRepository.save(line1);
		lineRepository.save(line2);
		lineRepository.save(line3);

		String response = restTemplate.getForObject(getHostnameWithPort() + "/statistics/line", String.class);
		JSONObject jsonObject = new JSONObject(response);

		assertTrue(jsonObject.has("type"), "response should have the entry type");
		assertTrue(jsonObject.has("results"), "response should have the entry results");

		assertEquals("line", jsonObject.getString("type"), "response's entry type should have the value line");
		assertEquals(3, jsonObject.getLong("results"), "response's entry results should have 3 items");
	}

	@Test
	public void statistics_stations() throws JSONException {
		stationRepository.save(station1);
		stationRepository.save(station2);
		stationRepository.save(station3);
		stationRepository.save(station4);

		String response = restTemplate.getForObject(getHostnameWithPort() + "/statistics/station", String.class);
		JSONObject jsonObject = new JSONObject(response);

		assertTrue(jsonObject.has("type"), "response should have the entry type");
		assertTrue(jsonObject.has("results"), "response should have the entry results");

		assertEquals("station", jsonObject.getString("type"), "response's entry type should have the value station");
		assertEquals(4, jsonObject.getLong("results"), "response's entry results should have 4 items");
	}

	@Test
	public void statistics() throws JSONException {
		connectionRepository.save(connection1);
		connectionRepository.save(connection2);

		lineRepository.save(line1);
		lineRepository.save(line2);
		lineRepository.save(line3);

		stationRepository.save(station1);
		stationRepository.save(station2);
		stationRepository.save(station3);
		stationRepository.save(station4);

		String response = restTemplate.getForObject(getHostnameWithPort() + "/statistics", String.class);
		JSONObject jsonObject = new JSONObject(response);

		assertTrue(jsonObject.has("connections"), "response should have entry connections");
		assertTrue(jsonObject.has("lines"), "response should have entry lines");
		assertTrue(jsonObject.has("stations"), "response should have entry stations");

		assertEquals(2, jsonObject.getJSONObject("connections").getLong("results"), "response's entry connections > results should have 2 items");
		assertEquals(3, jsonObject.getJSONObject("lines").getLong("results"), "response's entry lines > results should have 3 items");
		assertEquals(4, jsonObject.getJSONObject("stations").getLong("results"), "response's entry stations > results should have 4 items");
	}

}

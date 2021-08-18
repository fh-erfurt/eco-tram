package de.ecotram.backend.handler;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.LineEntry;
import de.ecotram.backend.entity.PassengerTram;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Network;
import de.ecotram.backend.handler.socketEntity.*;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.repository.NetworkRepository;
import de.ecotram.backend.simulation.LineSchedule;
import de.ecotram.backend.simulation.Schedule;
import de.ecotram.backend.simulation.SimulationRunner;
import de.ecotram.backend.simulation.event.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Handler for starting simulations and emitting events via socket
 */
@Transactional
@Log(topic = "simulation handler")
@Component("socketHandler")
public class SimulationHandler {

	private final LineRepository lineRepository;
	private final NetworkRepository networkRepository;
	private final SimpMessageSendingOperations messagingTemplate;

	private SimulationRunner simulationRunner;

	@Autowired
	public SimulationHandler(LineRepository lineRepository, NetworkRepository networkRepository, SimpMessageSendingOperations messagingTemplate) {
		this.lineRepository = lineRepository;
		this.networkRepository = networkRepository;
		this.messagingTemplate = messagingTemplate;
	}

	public void startSimulation(SimulationSettings simulationSettings) {
		if(simulationRunner != null) return;

		this.log.info("Starting simulation");

		var lines = this.lineRepository.findAll();
		var stations = lines.stream()
				.map(Line::getRoute)
				.flatMap(Set::stream).map(LineEntry::getStation)
				.collect(Collectors.toSet());

		var network = Network.fromStations(stations);

		networkRepository.save(network);

		var builder = Schedule.forNetwork(network);

		lines.forEach(l -> {
			AtomicInteger waitingTime = new AtomicInteger(50);
			AtomicInteger tramCount = new AtomicInteger(10);

			simulationSettings.lineSettings.forEach(lineSettings -> {
				if(lineSettings.id == l.getId()) {
					waitingTime.set(lineSettings.getWaitingTime());
					tramCount.set(lineSettings.getTramCount());
				}
			});

			builder.withLineSchedule(LineSchedule.fromWaitingTime(l, waitingTime.get(), tramCount.get()));
		});

		simulationRunner = new SimulationRunner(builder.build(), simulationSettings.getTickInterval(), simulationSettings.getDispatchInterval(), simulationSettings.getThreadCount());

		var progressReporter = simulationRunner.start();

		progressReporter.onTramStarted().add(this::onTramStarted);
		progressReporter.onTramStopped().add(this::onTramStopped);
		progressReporter.onRunnerStarted().add(this::onRunnerStarted);
		progressReporter.onRunnerStopped().add(this::onRunnerStopped);
		progressReporter.onRunnerTicks().add(this::onRunnerTicks);
	}

	public void stopSimulation() {
		if(simulationRunner == null || !simulationRunner.isRunning()) return;

		this.log.info("Stopping simulation");

		simulationRunner.stop();
		simulationRunner = null;
	}

	public SocketStatus getStatus() {
		return new SocketStatus(
				simulationRunner != null && simulationRunner.isRunning(),
				simulationRunner != null ? simulationRunner.getTicks() : -1
		);
	}

	public List<SocketStation> getStationsFromSimulation() {
		if(simulationRunner == null || !simulationRunner.isRunning())
			throw new IllegalStateException("simulation is not running");
		return simulationRunner.getNetwork().getStations().stream()
				.map(SocketStation::fromStation)
				.collect(Collectors.toList());
	}

	public List<SocketLine> getLinesFromSimulation() {
		if(simulationRunner == null || !simulationRunner.isRunning())
			throw new IllegalStateException("simulation is not running");
		return simulationRunner.getNetwork().getLines()
				.map(SocketLine::fromLine)
				.collect(Collectors.toList());
	}

	public List<SocketLineSchedule> getLinesSchedulesFromSimulation() {
		if(simulationRunner == null || !simulationRunner.isRunning())
			throw new IllegalStateException("simulation is not running");
		return simulationRunner.getSchedule().getLineSchedules().values()
				.stream().map(SocketLineSchedule::fromLineSchedule)
				.collect(Collectors.toList());
	}

	private void onTramStarted(TramStartedArgs consumer) {
		messagingTemplate.convertAndSend("/simulation/events/tram-start", getSocketPassengerTram(consumer.passengerTram, consumer.connection));
	}

	private void onTramStopped(TramStoppedArgs consumer) {
		messagingTemplate.convertAndSend("/simulation/events/tram-stop", getSocketPassengerTram(consumer.passengerTram, consumer.connection));
	}

	private void onRunnerStarted(RunnerStartedArgs args) {
		this.log.info("Runner started");
		messagingTemplate.convertAndSend("/simulation/events/start", getStatus());
	}

	private void onRunnerStopped(RunnerStoppedArgs args) {
		this.log.info("Runner stopped " + args.getReason() + " - " + args.getCause().name());
		if(args.getException().isPresent()) args.getException().get().printStackTrace();
		messagingTemplate.convertAndSend("/simulation/events/stop", getStatus());

		simulationRunner = null;
	}

	private void onRunnerTicks(RunnerTicksUpdatedArgs args) {
		messagingTemplate.convertAndSend("/simulation/events/status", getStatus());
	}

	private SocketPassengerTram getSocketPassengerTram(PassengerTram passengerTram, Connection connection) {
		var sourceStation = connection.getSourceStation();
		var destinationStation = connection.getDestinationStation();

		return new SocketPassengerTram(
				Integer.toHexString(passengerTram.hashCode()),
				passengerTram.getCurrentIndex(),
				new SocketStation(Integer.toHexString(sourceStation.hashCode()), sourceStation.getName()),
				new SocketStation(Integer.toHexString(destinationStation.hashCode()), destinationStation.getName())
		);
	}

	@Getter
	@AllArgsConstructor
	public static class LineSettings {
		private final long id;
		private final int tramCount;
		private final int waitingTime;

		public static LineSettings fromJson(long id, JSONObject jsonObject) {
			return new LineSettings(id, jsonObject.getInt("tramCount"), jsonObject.getInt("waitingTime"));
		}
	}

	@Getter
	@AllArgsConstructor
	public static class SimulationSettings {
		private final int tickInterval;
		private final int threadCount;
		private final int dispatchInterval;
		private final Set<LineSettings> lineSettings;

		public static SimulationSettings fromJson(JSONObject jsonObject) {
			Set<LineSettings> lineSettings = new HashSet<>();

			JSONObject jsonLineSettings = jsonObject.getJSONObject("settings");
			jsonLineSettings.keySet().forEach(k -> lineSettings.add(LineSettings.fromJson(Long.parseLong(k), jsonLineSettings.getJSONObject(k))));

			return new SimulationSettings(jsonObject.getInt("tickInterval"), jsonObject.getInt("threadCount"), jsonObject.getInt("dispatchInterval"), lineSettings);
		}
	}
}
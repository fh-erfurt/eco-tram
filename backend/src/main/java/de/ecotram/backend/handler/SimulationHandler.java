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
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
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

	public void startSimulation() {
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

		lines.forEach(l -> builder.withLineSchedule(LineSchedule.fromWaitingTime(l, 50)));

		simulationRunner = new SimulationRunner(builder.build(), 50, 1);

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
		messagingTemplate.convertAndSend("/simulation/events/start", getStatus());
	}

	private void onRunnerStopped(RunnerStoppedArgs args) {
		messagingTemplate.convertAndSend("/simulation/events/stop", getStatus());
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
}
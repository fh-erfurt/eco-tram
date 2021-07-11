package de.ecotram.backend.handler;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.namespace.Namespace;
import de.ecotram.backend.controller.LineController;
import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.LineEntry;
import de.ecotram.backend.entity.PassengerTram;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Network;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.handler.socketEntity.*;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.simulation.LineSchedule;
import de.ecotram.backend.simulation.Schedule;
import de.ecotram.backend.simulation.SimulationRunner;
import de.ecotram.backend.simulation.event.*;
import de.ecotram.backend.utilities.NetworkUtilities;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Transactional
@Log(topic = "simulation handler")
@Component("socketHandler")
public class SimulationHandler {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    private SimulationRunner simulationRunner;

    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public SimulationHandler(SimpMessageSendingOperations messagingTemplate, LineRepository lineRepository, StationRepository stationRepository) {
        this.messagingTemplate = messagingTemplate;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public void startSimulation() {
        if(simulationRunner != null) return;

        this.log.info("Starting simulation");

        var lines = this.lineRepository.findAll();
        var stations = lines.stream().map(Line::getRoute).flatMap(Set::stream).map(LineEntry::getStation).collect(Collectors.toSet());
        var network = Network.fromStations(stations);

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
        if(simulationRunner == null || !simulationRunner.isRunning()) throw new IllegalStateException("simulation is not running");
        return simulationRunner.getNetwork().getStations().stream().map(SocketStation::fromStation).collect(Collectors.toList());
    }

    public List<SocketLine> getLinesFromSimulation() {
        if(simulationRunner == null || !simulationRunner.isRunning()) throw new IllegalStateException("simulation is not running");
        return simulationRunner.getNetwork().getLines().map(SocketLine::fromLine).collect(Collectors.toList());
    }

    public List<SocketLineSchedule> getLinesSchedulesFromSimulation() {
        if(simulationRunner == null || !simulationRunner.isRunning()) throw new IllegalStateException("simulation is not running");
        return simulationRunner.getSchedule().getLineSchedules().values().stream().map(SocketLineSchedule::fromLineSchedule).collect(Collectors.toList());
    }

    private void onTramStarted(TramStartedArgs consumer) {
        messagingTemplate.convertAndSend("/simulation/events/tram-start", getSocketPassengerTram(consumer.tram, consumer.connection));
    }

    private void onTramStopped(TramStoppedArgs consumer) {
        messagingTemplate.convertAndSend("/simulation/events/tram-stop", getSocketPassengerTram(consumer.tram, consumer.connection));
    }

    private void onRunnerStarted(RunnerStartedArgs args) {
        messagingTemplate.convertAndSend("/simulation/events/start", getStatus());
    }

    private void onRunnerStopped(RunnerStoppedArgs args) {
        messagingTemplate.convertAndSend("/simulation/events/stop", getStatus());
    }

    private void onRunnerTicks(RunnerTicksArgs args) {
        messagingTemplate.convertAndSend("/simulation/events/status", getStatus());
    }

    private SocketPassengerTram getSocketPassengerTram(PassengerTram tram, Connection connection) {
        var sourceStation = connection.getSourceStation();
        var destinationStation = connection.getDestinationStation();

        return new SocketPassengerTram(
                Integer.toHexString(tram.hashCode()),
                tram.getCurrentIndex(),
                new SocketStation(Integer.toHexString(sourceStation.hashCode()), sourceStation.getName()),
                new SocketStation(Integer.toHexString(destinationStation.hashCode()), destinationStation.getName())
        );

    }
}

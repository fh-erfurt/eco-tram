package de.ecotram.backend.handler;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import de.ecotram.backend.controller.LineController;
import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.LineEntry;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Network;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.simulation.LineSchedule;
import de.ecotram.backend.simulation.Schedule;
import de.ecotram.backend.simulation.SimulationRunner;
import de.ecotram.backend.simulation.event.TramStartedArgs;
import de.ecotram.backend.simulation.event.TramStoppedArgs;
import de.ecotram.backend.utilities.NetworkUtilities;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Log(topic = "simulation socket")
@Component("socketHandler")
public class SimulationHandler {

    private final SocketIOServer socketIOServer;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    private SimulationRunner simulationRunner;

    @Autowired
    public SimulationHandler(SocketIOServer socketIOServer, LineRepository lineRepository, StationRepository stationRepository) {
        this.socketIOServer = socketIOServer;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;

        enableSocketNamespace();
    }

    private void enableSocketNamespace() {
        socketIOServer.addNamespace("/simulation");
        socketIOServer.addConnectListener(onConnected());
        socketIOServer.addDisconnectListener(onDisconnected());
    }

    public void startSimulation() {
        if(simulationRunner != null) return;

        var lines = this.lineRepository.findAll();
        var stations = lines.stream().map(Line::getRoute).flatMap(Set::stream).map(LineEntry::getStation).collect(Collectors.toSet());
        var network = Network.fromStations(stations);

        var builder = Schedule.forNetwork(network);

        lines.forEach(l -> builder.withLineSchedule(LineSchedule.fromWaitingTime(l, 500)));

        simulationRunner = new SimulationRunner(builder.build());

        var progressReporter = simulationRunner.start();

        progressReporter.onTramStarted().add(this::onTramStarted);
        progressReporter.onTramStopped().add(this::onTramStopped);
    }

    private void onTramStarted(TramStartedArgs consumer) {
        this.log.info("Tram[" + consumer.tram + "] - started " + consumer.connection);
    }

    private void onTramStopped(TramStoppedArgs consumer) {
        this.log.info("Tram[" + consumer.tram + "] - stopped " + consumer.connection);
    }

    private ConnectListener onConnected() {
        return client -> {
            this.log.info("Client[" + client.getSessionId().toString() + "] - Connected to simulation socket");
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            this.log.info("Client[" + client.getSessionId().toString() + "] - disconnected from simulation socket");
        };
    }
}

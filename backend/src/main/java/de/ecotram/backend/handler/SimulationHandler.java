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

    private void startSimulation() {
        if(simulationRunner != null) return;

        simulationRunner = new SimulationRunner(Schedule.forNetwork(NetworkUtilities.getTestingNetwork()).build());

        var progressReporter = simulationRunner.start();

        progressReporter.onTramStarted().add(this::onTramStarted);
        progressReporter.onTramStopped().add(this::onTramStopped);
    }

    public void createNetwork() {
        System.out.println("creating network");
        try {
            var lines = this.lineRepository.findAll();
            var stations = new HashSet<Station>();

            lines.forEach(l -> {
                l.getRoute().forEach(e -> {
                    stations.add(e.getStation());
                });
            });

            var network = Network.fromStations(stations);

            var ringelberg = this.stationRepository.findByName("Ringelberg");
            var europaplatz = this.stationRepository.findByName("Europaplatz");

            var stations1 = ringelberg.getDestinationConnections()
                    .stream()
                    .map(Connection::getDestinationStation)
                    .collect(Collectors.toSet());

            System.out.println(stations1.size());

            var map = network.getAdjacencyMap();

            map.keySet().forEach(s -> map.get(s).forEach(s1 -> System.out.println(s.getName() + " - " + s1.getName())));
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    private void onTramStarted(TramStartedArgs consumer) {

    }

    private void onTramStopped(TramStoppedArgs consumer) {

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

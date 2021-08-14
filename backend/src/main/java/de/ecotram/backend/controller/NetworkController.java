package de.ecotram.backend.controller;

import de.ecotram.backend.entity.network.Network;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.ConnectionRepository;
import de.ecotram.backend.repository.NetworkRepository;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponse;
import de.ecotram.backend.utilities.NetworkUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// purely for testing/migrating database entries from NetworkUtilities
@RestController
public final class NetworkController {

    @Autowired
    private NetworkRepository networkRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @CrossOrigin
    @GetMapping(value = "/networks/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> list(@PathVariable("id") Long id) {
        Optional<Network> connection = networkRepository.findById(id);
        return connection.<ResponseEntity<Object>>map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-network", "No network with id found")));
    }

    @CrossOrigin
    @PostMapping(value = "/networks/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> newNetwork() {
        var network = NetworkUtilities.getTestingNetwork();
        networkRepository.save(network);
        stationRepository.saveAll(network.getStations());
        connectionRepository.saveAll(
                network.getStations().stream()
                        .map(Station::getSourceConnections)
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet()));

        return ResponseEntity.ok().body(network);
    }

    @CrossOrigin
    @PostMapping(value = "/networks/delete/{id}")
    public ResponseEntity<Object> deleteNetwork(@PathVariable("id") Long id) {
        Optional<Network> networkEntry = networkRepository.findById(id);

        if (networkEntry.isPresent()) {
            networkRepository.delete(networkEntry.get());
            return ResponseEntity.ok().body("OK");
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-connection", "No connection with id found"));
    }
}
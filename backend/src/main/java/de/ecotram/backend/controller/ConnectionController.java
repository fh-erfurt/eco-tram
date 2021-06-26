package de.ecotram.backend.controller;

import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.handler.ConnectionHandler;
import de.ecotram.backend.pagination.PaginationRequest;
import de.ecotram.backend.repository.ConnectionRepository;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponse;
import de.ecotram.backend.utilities.ErrorResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public final class ConnectionController {
    private final ConnectionHandler connectionHandler = new ConnectionHandler();

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private StationRepository stationRepository;

    @CrossOrigin
    @GetMapping("/connections/list")
    public ResponseEntity<PaginationRequest<Connection>> list(@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok().body(connectionRepository.getAsPaginationRequest(Connection.class, limit, page));
    }

    @CrossOrigin
    @GetMapping(value = "/connections/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> list(@PathVariable("id") Long id) {
        Optional<Connection> connection = connectionRepository.findById(id);
        return connection.<ResponseEntity<Object>>map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-connection", "No connection with id found")));
    }

    @CrossOrigin
    @PostMapping(value = "/connections/new", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> newConnection(@RequestBody ConnectionHandler.ConnectionBody connectionBody) {
        try {
            Connection connection = connectionHandler.createConnectionFromRequest(stationRepository, connectionRepository, connectionBody);
            return ResponseEntity.ok().body(connection);
        } catch (ErrorResponseException errorResponseException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseException.getErrorResponse());
        }
    }

    @CrossOrigin
    @PostMapping(value = "/connections/update/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateConnection(@RequestBody ConnectionHandler.ConnectionBody connectionBody, @PathVariable("id") Long id) {
        Optional<Connection> connectionEntry = connectionRepository.findById(id);

        if (connectionEntry.isPresent())
            try {
                Connection connection = connectionHandler.updateConnectionFromRequest(connectionEntry.get(), stationRepository, connectionRepository, connectionBody);
                return ResponseEntity.ok().body(connection);
            } catch (ErrorResponseException errorResponseException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseException.getErrorResponse());
            }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-connection", "No connection with id found"));
    }

    @CrossOrigin
    @PostMapping(value = "/connections/delete/{id}")
    public ResponseEntity<Object> deleteConnection(@PathVariable("id") Long id) {
        Optional<Connection> connectionEntry = connectionRepository.findById(id);

        if (connectionEntry.isPresent()) {
            connectionRepository.delete(connectionEntry.get());
            return ResponseEntity.ok().body("OK");
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-connection", "No connection with id found"));
    }
}
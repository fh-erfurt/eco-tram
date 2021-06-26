package de.ecotram.backend.controller;

import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.handler.StationHandler;
import de.ecotram.backend.pagination.PaginationRequest;
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
public final class StationController {
    private final StationHandler stationHandler = new StationHandler();

    @Autowired
    private StationRepository stationRepository;

    @CrossOrigin
    @GetMapping("/stations/list")
    public ResponseEntity<PaginationRequest<Station>> list(@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok().body(stationRepository.getAsPaginationRequest(Station.class, limit, page));
    }

    @CrossOrigin
    @GetMapping(value = "/stations/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> list(@PathVariable("id") Long id) {
        Optional<Station> station = stationRepository.findById(id);
        return station.<ResponseEntity<Object>>map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-station", "No station with id found")));
    }

    @CrossOrigin
    @PostMapping(value = "/stations/new", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> newStation(@RequestBody StationHandler.StationBody stationBody) {
        try {
            Station station = stationHandler.createStationFromRequest(stationRepository, stationBody);
            return ResponseEntity.ok().body(station);
        } catch (ErrorResponseException errorResponseException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseException.getErrorResponse());
        }
    }

    @CrossOrigin
    @PostMapping(value = "/stations/update/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateStation(@RequestBody StationHandler.StationBody stationBody, @PathVariable("id") Long id) {
        Optional<Station> stationEntry = stationRepository.findById(id);

        if (stationEntry.isPresent())
            try {
                Station station = stationHandler.updateStationFromRequest(stationEntry.get(), stationRepository, stationBody);
                return ResponseEntity.ok().body(station);
            } catch (ErrorResponseException errorResponseException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseException.getErrorResponse());
            }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-station", "No station with id found"));
    }

    @CrossOrigin
    @PostMapping(value = "/stations/delete/{id}")
    public ResponseEntity<Object> deleteStation(@RequestBody StationHandler.StationBody stationBody, @PathVariable("id") Long id) {
        Optional<Station> stationEntry = stationRepository.findById(id);

        if (stationEntry.isPresent()) {
            stationRepository.delete(stationEntry.get());
            return ResponseEntity.ok().body("OK");
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-station", "No station with id found"));
    }
}
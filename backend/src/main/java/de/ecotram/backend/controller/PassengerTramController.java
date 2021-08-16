package de.ecotram.backend.controller;

import de.ecotram.backend.entity.PassengerTram;
import de.ecotram.backend.handler.PassengerTramHandler;
import de.ecotram.backend.pagination.PaginationRequest;
import de.ecotram.backend.repository.PassengerTramRepository;
import de.ecotram.backend.utilities.ErrorResponse;
import de.ecotram.backend.utilities.ErrorResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public final class PassengerTramController {
    @Autowired
    private PassengerTramHandler passengerTramHandler;

    @Autowired
    private PassengerTramRepository passengerTramRepository;

    @CrossOrigin
    @GetMapping("/passenger-trams/list")
    public ResponseEntity<PaginationRequest<PassengerTram>> list(@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok().body(passengerTramRepository.getAsPaginationRequest(PassengerTram.class, limit, page));
    }

    @CrossOrigin
    @GetMapping(value = "/passenger-trams/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> list(@PathVariable("id") Long id) {
        Optional<PassengerTram> passengerTram = passengerTramRepository.findById(id);
        return passengerTram.<ResponseEntity<Object>>map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-passenger-tram", "No passenger tram with id found")));
    }

    @CrossOrigin
    @PostMapping(value = "/passenger-trams/new", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> newPassengerTram(@RequestBody PassengerTramHandler.PassengerTramBody passengerTramBody) {
        try {
            PassengerTram passengerTram = passengerTramHandler.createPassengerTramFromRequest(passengerTramBody);
            return ResponseEntity.ok().body(passengerTram);
        } catch (ErrorResponseException errorResponseException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseException.getErrorResponse());
        }
    }

    @CrossOrigin
    @PostMapping(value = "/passenger-trams/update/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePassengerTram(@RequestBody PassengerTramHandler.PassengerTramBody passengerTramBody, @PathVariable("id") Long id) {
        Optional<PassengerTram> passengerTramEntry = passengerTramRepository.findById(id);

        if (passengerTramEntry.isPresent())
            try {
                PassengerTram passengerTram = passengerTramHandler.updatePassengerTramFromRequest(passengerTramEntry.get(), passengerTramBody);
                return ResponseEntity.ok().body(passengerTram);
            } catch (ErrorResponseException errorResponseException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseException.getErrorResponse());
            }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-passenger-tram", "No passenger tram with id found"));
    }

    @CrossOrigin
    @PostMapping(value = "/passenger-trams/delete/{id}")
    public ResponseEntity<Object> deletePassengerTram(@PathVariable("id") Long id) {
        Optional<PassengerTram> passengerTramEntry = passengerTramRepository.findById(id);

        if (passengerTramEntry.isPresent()) {
            passengerTramRepository.delete(passengerTramEntry.get());
            return ResponseEntity.ok().body("OK");
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-passenger-tram", "No passenger tram with id found"));
    }
}
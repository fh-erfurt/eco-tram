package de.ecotram.backend.controller;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.handler.LineHandler;
import de.ecotram.backend.pagination.PaginationRequest;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.utilities.ErrorResponse;
import de.ecotram.backend.utilities.ErrorResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for handling lines containing line entries, stations and connections
 */
@RestController
public class LineController {

	private final LineHandler lineHandler;
	private final LineRepository lineRepository;

	@Autowired
	public LineController(LineHandler lineHandler, LineRepository lineRepository) {
		this.lineHandler = lineHandler;
		this.lineRepository = lineRepository;
	}

	@CrossOrigin
	@GetMapping("/lines/list")
	public ResponseEntity<PaginationRequest<Line>> list(@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "0") int page) {
		return ResponseEntity.ok().body(lineRepository.getAsPaginationRequest(Line.class, limit, page));
	}

	@CrossOrigin
	@GetMapping(value = "/lines/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> list(@PathVariable("id") Long id) {
		Optional<Line> line = lineRepository.findById(id);
		return line.<ResponseEntity<Object>>map(value -> ResponseEntity.ok().body(value))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-line", "No line with id found")));
	}

	@CrossOrigin
	@PostMapping(value = "/lines/new", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> newLine(@RequestBody LineHandler.LineBody lineBody) {
		try {
			Line line = lineHandler.createLineFromRequest(lineBody);
			return ResponseEntity.ok().body(line);
		} catch (ErrorResponseException errorResponseException) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseException.getErrorResponse());
		}
	}

	@CrossOrigin
	@PostMapping(value = "/lines/update/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateLine(@RequestBody LineHandler.LineBody lineBody, @PathVariable("id") Long id) {
		Optional<Line> lineEntry = lineRepository.findById(id);

		if(lineEntry.isPresent())
			try {
				Line line = lineHandler.updateLineFromRequest(lineEntry.get(), lineBody);
				return ResponseEntity.ok().body(line);
			} catch (ErrorResponseException errorResponseException) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseException.getErrorResponse());
			}
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-line", "No line with id found"));
	}

	@CrossOrigin
	@PostMapping(value = "/lines/delete/{id}")
	public ResponseEntity<Object> deleteLine(@PathVariable("id") Long id) {
		Optional<Line> line = lineRepository.findById(id);

		if(line.isPresent()) {
			lineHandler.deleteLine(line.get());
			return ResponseEntity.ok().body("OK");
		} else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("invalid-line", "No line with id found"));
	}
}
package de.ecotram.backend.controller;

import de.ecotram.backend.handler.StatisticsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class StatisticsController {
    @Autowired
    private StatisticsHandler statisticsHandler;

    @CrossOrigin
    @GetMapping("/statistics/connections")
    public ResponseEntity<StatisticsHandler.StatisticsCountResult> getConnectionStatistics() {
        return ResponseEntity.ok(statisticsHandler.getConnectionCount());
    }

    @CrossOrigin
    @GetMapping("/statistics/line")
    public ResponseEntity<StatisticsHandler.StatisticsCountResult> getLineStatistics() {
        return ResponseEntity.ok(statisticsHandler.getLineCount());
    }

    @CrossOrigin
    @GetMapping("/statistics/passenger-tram")
    public ResponseEntity<StatisticsHandler.StatisticsCountResult> getPassengerTramStatistics() {
        return ResponseEntity.ok(statisticsHandler.getPassengerTramCount());
    }

    @CrossOrigin
    @GetMapping("/statistics/station")
    public ResponseEntity<StatisticsHandler.StatisticsCountResult> getStationStatistics() {
        return ResponseEntity.ok(statisticsHandler.getStationsCount());
    }

    @CrossOrigin
    @GetMapping("/statistics")
    public ResponseEntity<StatisticsHandler.StatisticsOverallCountResult> getStatistics() {
        return ResponseEntity.ok(new StatisticsHandler.StatisticsOverallCountResult(
                statisticsHandler.getConnectionCount(),
                statisticsHandler.getLineCount(),
                statisticsHandler.getPassengerTramCount(),
                statisticsHandler.getStationsCount()
        ));
    }
}
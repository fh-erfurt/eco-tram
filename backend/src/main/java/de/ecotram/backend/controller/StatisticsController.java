package de.ecotram.backend.controller;

import de.ecotram.backend.handler.StatisticsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class StatisticsController {
    @Autowired
    private StatisticsHandler statisticsHandler;

    @GetMapping("/statistics/connections")
    public ResponseEntity<StatisticsHandler.StatisticsCountResult> getConnectionStatistics() {
        return ResponseEntity.ok(statisticsHandler.getConnectionCount());
    }

    @GetMapping("/statistics/line")
    public ResponseEntity<StatisticsHandler.StatisticsCountResult> getLineStatistics() {
        return ResponseEntity.ok(statisticsHandler.getLineCount());
    }

    @GetMapping("/statistics/passenger-tram")
    public ResponseEntity<StatisticsHandler.StatisticsCountResult> getPassengerTramStatistics() {
        return ResponseEntity.ok(statisticsHandler.getPassengerTramCount());
    }

    @GetMapping("/statistics/station")
    public ResponseEntity<StatisticsHandler.StatisticsCountResult> getStationStatistics() {
        return ResponseEntity.ok(statisticsHandler.getStationsCount());
    }

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
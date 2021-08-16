package de.ecotram.backend.controller;

import de.ecotram.backend.handler.SimulationHandler;
import de.ecotram.backend.handler.socketEntity.SocketLine;
import de.ecotram.backend.handler.socketEntity.SocketLineSchedule;
import de.ecotram.backend.handler.socketEntity.SocketStation;
import de.ecotram.backend.handler.socketEntity.SocketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SocketController {
    @Autowired
    private SimulationHandler simulationHandler;

    @MessageMapping("/simulation/start")
    public void simulationStart() {
        simulationHandler.startSimulation();
    }

    @MessageMapping("/simulation/stop")
    public void simulationStop() {
        simulationHandler.stopSimulation();
    }

    @MessageMapping("/simulation/status")
    @SendToUser("/simulation/status")
    public SocketStatus simulationStatus() {
        return this.simulationHandler.getStatus();
    }

    @MessageMapping("/simulation/stations")
    @SendToUser("/simulation/stations")
    public List<SocketStation> simulationStations() {
        return this.simulationHandler.getStationsFromSimulation();
    }

    @MessageMapping("/simulation/lines")
    @SendToUser("/simulation/lines")
    public List<SocketLine> simulationLines() {
        return this.simulationHandler.getLinesFromSimulation();
    }

    @MessageMapping("/simulation/line-schedules")
    @SendToUser("/simulation/line-schedules")
    public List<SocketLineSchedule> simulationLineSchedules() {
        return this.simulationHandler.getLinesSchedulesFromSimulation();
    }
}
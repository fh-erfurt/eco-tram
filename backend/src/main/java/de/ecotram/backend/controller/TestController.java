package de.ecotram.backend.controller;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

// TODO(luca): query params for properties
@RestController
public class TestController {
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @GetMapping("/create")
    public String create() {
        Line line = new Line();

        Station station = new Station();
        station.setName("Teststation");
        station.setCurrentPassengers(10);
        station.setMaxPassengers(200);

        line.getRoute().add(station);

        station.getLines().add(line);

        lineRepository.save(line);
        stationRepository.save(station);

        System.out.println(line.getId());

        return "ID Station " + station.getId() + ", ID Line " + line.getId();
    }

    @GetMapping("/new-station")
    public String newStation() {
        Station station = new Station();
        station.setName("Teststation");
        station.setCurrentPassengers(10);
        station.setMaxPassengers(200);

        stationRepository.save(station);

        return "Station erstellt, ID: " + station.getId();
    }

    @GetMapping("/get-station/{id}")
    public String getStation(@PathVariable("id") Long id) {
        Optional<Station> station = stationRepository.findById(id);

        return "Station vorhanden " + (station.isPresent() ? "Ja" : "Nein") + ", Name: " + (station.isPresent() ? station.get().getName() : "-");
    }

    @GetMapping("/delete-station/{id}")
    public String deleteStation(@PathVariable("id") Long id) {
        Optional<Station> station = stationRepository.findById(id);

        if(station.isPresent()) {
            stationRepository.delete(station.get());
            return "Station existiert und wurde gelöscht";
        } else
            return "Station existiert nicht";
    }
}
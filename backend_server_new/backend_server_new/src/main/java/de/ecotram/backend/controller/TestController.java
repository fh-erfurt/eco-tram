package de.ecotram.backend.controller;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.Station;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@RestController
public class TestController {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @GetMapping("/create")
    public String create() {
        Line line = new Line();
        line.setName("Testlinie");

        Station station = new Station();
        station.setName("Teststation");
        station.setCurrentPassengers(10);
        station.setMaxPassengers(200);
        station.setWaitingTime(0.5f);

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
        station.setWaitingTime(0.5f);

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

package de.ecotram.backend.handler;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.LineEntry;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.ConnectionRepository;
import de.ecotram.backend.repository.LineEntryRepository;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponseException;
import de.ecotram.backend.utilities.ValidationUtilities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component("lineHandler")
public final class LineHandler {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private LineEntryRepository lineEntryRepository;

    public ValidationResult validateStationIds(ArrayList<Long> ids) {
        List<Station> stations = stationRepository.findAllById(ids);

        ArrayList<Station> addedStations = new ArrayList<>();
        ArrayList<Connection> addedConnections = new ArrayList<>();

        AtomicReference<Station> priorStation = new AtomicReference<>();

        ids.forEach(id -> {
            var optionalStation = stations.stream().filter(station -> station.getId() == id).findFirst();
            if(optionalStation.isPresent()) {
                addedStations.add(optionalStation.get());
                if(priorStation.get() != null)
                    addedConnections.add(priorStation.get().connectTo(optionalStation.get(), c -> c));
                priorStation.set(optionalStation.get());
            }
        });

        return new ValidationResult(addedStations, addedConnections);
    }

    public ArrayList<Long> validateLineBody(LineBody lineBody) throws ErrorResponseException {
        if (!ValidationUtilities.isStringValid(lineBody.name, 1, 100))
            throw new ErrorResponseException("invalid-name", "name is invalid");

        ArrayList<Long> ids = new ArrayList<>();

        Arrays.asList(lineBody.stationIds.split(",")).forEach(id -> ids.add(Long.parseLong(id)));

        if (ids.size() == 0)
            throw new ErrorResponseException("invalid-ids", "at least one id is required");

        return ids;
    }

    public Line createLineFromRequest(LineBody lineBody) throws ErrorResponseException {
        ArrayList<Long> ids = validateLineBody(lineBody);

        Line line = lineBody.applyToLine();
        ValidationResult validationResult = validateStationIds(ids);

        List<LineEntry> lineEntries = new ArrayList<>();
        AtomicInteger index = new AtomicInteger();

        validationResult.stations.forEach(station -> {
            LineEntry lineEntry = new LineEntry();
            lineEntry.setLine(line);
            lineEntry.setStation(station);
            lineEntry.setOrderValue(index.intValue());

            lineEntries.add(lineEntry);

            index.getAndIncrement();
        });

        line.getRoute().clear();
        line.getRoute().addAll(lineEntries);

        lineRepository.save(line);
        lineEntryRepository.saveAll(lineEntries);
        connectionRepository.saveAll(validationResult.connections);

        return line;
    }

    public Line updateLineFromRequest(Line line, LineBody lineBody) throws ErrorResponseException {
        ArrayList<Long> ids = validateLineBody(lineBody);
        Line updatedLine = lineBody.applyToLine(line);
        ValidationResult validationResult = validateStationIds(ids);

        var sortedRoute = line.getRoute().stream().sorted(Comparator.comparing(LineEntry::getOrderValue)).collect(Collectors.toList());

        AtomicBoolean changesFound = new AtomicBoolean(sortedRoute.size() != validationResult.stations.size());

        List<LineEntry> lineEntries = new ArrayList<>();

        if(!changesFound.get()) {
            AtomicInteger index = new AtomicInteger();

            validationResult.stations.forEach(station -> {
                if(changesFound.get()) return;

                if(station != sortedRoute.get(index.get()).getStation())
                    changesFound.set(true);

                index.getAndIncrement();
            });
        }

        if(changesFound.get()) {
            lineEntryRepository.deleteAll(sortedRoute);
            connectionRepository.deleteAll(sortedRoute.stream().map(LineEntry::getStation).map(Station::getDestinationConnections).flatMap(Set::stream).collect(Collectors.toList()));
            connectionRepository.deleteAll(sortedRoute.stream().map(LineEntry::getStation).map(Station::getSourceConnections).flatMap(Set::stream).collect(Collectors.toList()));

            AtomicInteger index = new AtomicInteger();

            validationResult.stations.forEach(station -> {
                LineEntry lineEntry = new LineEntry();
                lineEntry.setLine(line);
                lineEntry.setStation(station);
                lineEntry.setOrderValue(index.intValue());

                lineEntries.add(lineEntry);

                index.getAndIncrement();
            });
        }

        if(changesFound.get()) {
            line.getRoute().clear();
            line.getRoute().addAll(lineEntries);
        }

        lineRepository.save(updatedLine);

        if(changesFound.get()) {
            lineEntryRepository.saveAll(lineEntries);
            connectionRepository.saveAll(validationResult.connections);
        }

        return updatedLine;
    }

    public void deleteLine(Line line) {
        var route = line.getRoute();

        lineEntryRepository.deleteAll(route);
        connectionRepository.deleteAll(route.stream().map(LineEntry::getStation).map(Station::getDestinationConnections).flatMap(Set::stream).collect(Collectors.toList()));
        connectionRepository.deleteAll(route.stream().map(LineEntry::getStation).map(Station::getSourceConnections).flatMap(Set::stream).collect(Collectors.toList()));
        lineRepository.delete(line);
    }

    public static class LineBody {
        @Getter
        private String name;

        @Getter
        private String stationIds;

        public Line applyToLine(Line line) {
            line.setName(name);

            return line;
        }

        public Line applyToLine() {
            return applyToLine(new Line());
        }
    }

    @Data
    @AllArgsConstructor
    private final class ValidationResult {
        private ArrayList<Station> stations;
        private ArrayList<Connection> connections;
    }
}
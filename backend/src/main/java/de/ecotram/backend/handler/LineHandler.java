package de.ecotram.backend.handler;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.LineEntry;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.entity.network.Traversable;
import de.ecotram.backend.repository.ConnectionRepository;
import de.ecotram.backend.repository.LineEntryRepository;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponseException;
import de.ecotram.backend.utilities.ValidationUtilities;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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

    public List<Traversable> validateTraversableIds(List<Long> ids) {
        List<Station> stations = stationRepository.findAllById(ids);
        List<Connection> connections = connectionRepository.findAllById(ids);

        List<Traversable> traversables = new ArrayList<>();
        final Traversable[] previousItem = {null};

        ids.forEach(id -> {
            Optional<Station> station = stations.stream().filter(filterStation -> filterStation.getId() == id).findFirst();
            if (station.isPresent()) {
                if(previousItem[0] == null) previousItem[0] = station.get();
                else if(previousItem[0] instanceof Station) {
                    traversables.add(getConnectionInBetween((Station) previousItem[0], station.get()));
                    previousItem[0] = station.get();
                }

                traversables.add(station.get());
            } else {
                Optional<Connection> connection = connections.stream().filter(filterConnection -> filterConnection.getId() == id).findFirst();

                if(connection.isPresent()) {
                    traversables.add(connection.get());
                    if(previousItem[0] == null) previousItem[0] = connection.get();
                }
            }
        });

        return traversables;
    }

    public Connection getConnectionInBetween(Station station1, Station station2) {
        Connection connection = connectionRepository.findByLineAndTraversable(station1, station2);
        if(connection != null) return connection;

        connection = new Connection();
        connection.setSourceStation(station1);
        connection.setDestinationStation(station2);

        connectionRepository.save(connection);

        return connection;
    }

    public List<Long> validateLineBody(LineBody lineBody) throws ErrorResponseException {
        if (!ValidationUtilities.isStringValid(lineBody.name, 1, 100))
            throw new ErrorResponseException("invalid-name", "name is invalid");

        ArrayList<Long> ids = new ArrayList<>();

        Arrays.asList(lineBody.traversableIds.split(",")).forEach(id -> ids.add(Long.parseLong(id)));

        if (ids.size() == 0)
            throw new ErrorResponseException("invalid-ids", "at least one id is required");

        return ids;
    }

    public Line createLineFromRequest(LineBody lineBody) throws ErrorResponseException {
        List<Long> ids = validateLineBody(lineBody);

        Line line = lineBody.applyToLine();
        List<Traversable> traversableList = validateTraversableIds(ids);

        List<LineEntry> lineEntries = new ArrayList<>();
        AtomicInteger index = new AtomicInteger();

        traversableList.forEach(traversable -> {
            LineEntry lineEntry = new LineEntry();
            lineEntry.setLine(line);
            lineEntry.setTraversable(traversable);
            lineEntry.setOrderValue(index.intValue());

            lineEntries.add(lineEntry);

            index.getAndIncrement();
        });

        line.getRoute().clear();
        line.getRoute().addAll(lineEntries);

        lineRepository.save(line);

        lineEntries.forEach(lineEntry -> lineEntryRepository.save(lineEntry));

        return line;
    }

    public Line updateLineFromRequest(Line line, LineBody lineBody) throws ErrorResponseException {
        List<Long> ids = validateLineBody(lineBody);
        Line updatedLine = lineBody.applyToLine(line);
        List<Traversable> traversableList = validateTraversableIds(ids);

        List<LineEntry> lineEntries = new ArrayList<>();
        AtomicInteger index = new AtomicInteger();

        traversableList.forEach(traversable -> {
            LineEntry lineEntry = lineEntryRepository.findByLineAndTraversable(line, traversable);

            if(lineEntry == null) {
                lineEntry = new LineEntry();

                lineEntry.setLine(line);
                lineEntry.setTraversable(traversable);
            }

            lineEntry.setOrderValue(index.intValue());

            lineEntries.add(lineEntry);

            index.getAndIncrement();
        });

        line.getRoute().clear();
        line.getRoute().addAll(lineEntries);

        lineRepository.save(updatedLine);
        lineEntries.forEach(lineEntryRepository::save);

        return updatedLine;
    }

    public static class LineBody {
        @Getter
        private String name;

        @Getter
        private String traversableIds;

        public Line applyToLine(Line line) {
            line.setName(name);

            return line;
        }

        public Line applyToLine() {
            return applyToLine(new Line());
        }
    }
}
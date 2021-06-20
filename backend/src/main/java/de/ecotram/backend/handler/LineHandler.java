package de.ecotram.backend.handler;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.entity.network.Traversable;
import de.ecotram.backend.repository.ConnectionRepository;
import de.ecotram.backend.repository.LineRepository;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponseException;
import de.ecotram.backend.utilities.Utilities;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LineHandler {

    public List<Traversable> validateTraversableIds(List<Long> ids, StationRepository stationRepository, ConnectionRepository connectionRepository) throws ErrorResponseException {
        List<Station> stations = stationRepository.findAllById(ids);
        List<Connection> connections = connectionRepository.findAllById(ids);

        List<Traversable> traversables = new ArrayList<>();

        ids.forEach(id -> {
            Optional<Station> station = stations.stream().filter(filterStation -> filterStation.getId().equals(id)).findFirst();
            if (station.isPresent())
                traversables.add(station.get());
            else {
                Optional<Connection> connection = connections.stream().filter(filterConnection -> filterConnection.getId().equals(id)).findFirst();

                connection.ifPresent(traversables::add);
            }
        });

        if (ids.size() != traversables.size())
            throw new ErrorResponseException("invalid-traversable-ids", "Some id were not found");

        return traversables;
    }

    public List<Long> validateLineBody(LineBody lineBody) throws ErrorResponseException {
        if (!Utilities.isStringValid(lineBody.name, 1, 100))
            throw new ErrorResponseException("invalid-name", "name is invalid");

        ArrayList<Long> ids = new ArrayList<>();

        Arrays.asList(lineBody.traversableIds.split(",")).forEach(id -> ids.add(Long.parseLong(id)));

        if (ids.size() == 0)
            throw new ErrorResponseException("invalid-ids", "at least one id is required");

        return ids;
    }

    public Line createLineFromRequest(LineRepository lineRepository, StationRepository stationRepository, ConnectionRepository connectionRepository, LineBody lineBody) throws ErrorResponseException {
        List<Long> ids = validateLineBody(lineBody);

        Line line = lineBody.applyToLine();
        List<Traversable> traversableList = validateTraversableIds(ids, stationRepository, connectionRepository);

        line.getRoute().clear();
        line.getRoute().addAll(traversableList);

        traversableList.forEach(traversable -> {
            traversable.getLines().add(line);

            if (traversable instanceof Station)
                stationRepository.save((Station) traversable);

            if (traversable instanceof Connection)
                connectionRepository.save((Connection) traversable);
        });

        lineRepository.save(line);

        return line;
    }

    public Line updateLineFromRequest(Line line, LineRepository lineRepository, StationRepository stationRepository, ConnectionRepository connectionRepository, LineBody lineBody) throws ErrorResponseException {
        List<Long> ids = validateLineBody(lineBody);
        Line updatedLine = lineBody.applyToLine(line);
        List<Traversable> traversableList = validateTraversableIds(ids, stationRepository, connectionRepository);

        line.getRoute().stream().filter(traversable -> !traversableList.contains(traversable)).forEach(
                traversable -> {
                    traversable.getLines().remove(line);

                    if (traversable instanceof Station)
                        stationRepository.save((Station) traversable);

                    if (traversable instanceof Connection)
                        connectionRepository.save((Connection) traversable);
                }
        );

        line.getRoute().clear();
        line.getRoute().addAll(traversableList);

        traversableList.forEach(traversable -> {
            traversable.getLines().add(line);

            if (traversable instanceof Station)
                stationRepository.save((Station) traversable);

            if (traversable instanceof Connection)
                connectionRepository.save((Connection) traversable);
        });

        lineRepository.save(updatedLine);

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

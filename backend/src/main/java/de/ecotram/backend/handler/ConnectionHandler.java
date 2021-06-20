package de.ecotram.backend.handler;

import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.ConnectionRepository;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

public final class ConnectionHandler {
    public ConnectionStations validateConnectionBody(ConnectionBody connectionBody, StationRepository stationRepository) throws ErrorResponseException {
        Optional<Station> sourceStation = stationRepository.findById(connectionBody.sourceStationId);
        if (sourceStation.isEmpty())
            throw new ErrorResponseException("invalid-source-station", "No station with id found");

        Optional<Station> destinationStation = stationRepository.findById(connectionBody.destinationStationId);
        if (destinationStation.isEmpty())
            throw new ErrorResponseException("invalid-destination-station", "No station with id found");

        return new ConnectionStations(sourceStation.get(), destinationStation.get());
    }

    public void appendSourceAndDestinationStation(Connection connection, ConnectionStations connectionStations) {
        Station sourceStation = connectionStations.getSourceStation();
        Station destinationStation = connectionStations.getDestinationStation();

        connection.setSourceStation(sourceStation);
        connection.setDestinationStation(destinationStation);
    }

    public Connection createConnectionFromRequest(StationRepository stationRepository, ConnectionRepository connectionRepository, ConnectionBody connectionBody) throws ErrorResponseException {
        ConnectionStations connectionStations = validateConnectionBody(connectionBody, stationRepository);

        Connection connection = new Connection();
        appendSourceAndDestinationStation(connection, connectionStations);

        connectionRepository.save(connection);
        stationRepository.save(connection.getSourceStation());
        stationRepository.save(connection.getDestinationStation());

        return connection;
    }

    public Connection updateConnectionFromRequest(Connection connection, StationRepository stationRepository, ConnectionRepository connectionRepository, ConnectionBody connectionBody) throws ErrorResponseException {
        ConnectionStations connectionStations = validateConnectionBody(connectionBody, stationRepository);

        appendSourceAndDestinationStation(connection, connectionStations);

        connectionRepository.save(connection);
        stationRepository.save(connection.getSourceStation());
        stationRepository.save(connection.getDestinationStation());

        return connection;
    }

    public static class ConnectionBody {
        @Getter
        private long sourceStationId;

        @Getter
        private long destinationStationId;
    }

    @AllArgsConstructor
    class ConnectionStations {
        @Getter
        private final Station sourceStation;

        @Getter
        private final Station destinationStation;
    }
}
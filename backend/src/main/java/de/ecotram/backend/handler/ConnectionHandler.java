package de.ecotram.backend.handler;

import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import de.ecotram.backend.repository.ConnectionRepository;
import de.ecotram.backend.repository.StationRepository;
import de.ecotram.backend.utilities.ErrorResponseException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("connectionHandler")
public final class ConnectionHandler {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    public ConnectionStations validateConnectionBody(ConnectionBody connectionBody) throws ErrorResponseException {
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

    public Connection createConnectionFromRequest(ConnectionBody connectionBody) throws ErrorResponseException {
        ConnectionStations connectionStations = validateConnectionBody(connectionBody);

        Connection connection = new Connection();
        appendSourceAndDestinationStation(connection, connectionStations);

        connectionRepository.save(connection);
        stationRepository.save(connection.getSourceStation());
        stationRepository.save(connection.getDestinationStation());

        return connection;
    }

    public Connection updateConnectionFromRequest(Connection connection, ConnectionBody connectionBody) throws ErrorResponseException {
        ConnectionStations connectionStations = validateConnectionBody(connectionBody);

        appendSourceAndDestinationStation(connection, connectionStations);

        connectionRepository.save(connection);
        stationRepository.save(connection.getSourceStation());
        stationRepository.save(connection.getDestinationStation());

        return connection;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConnectionBody {
        @Getter
        private long sourceStationId;

        @Getter
        private long destinationStationId;
    }

    @AllArgsConstructor
    public static class ConnectionStations {
        @Getter
        private final Station sourceStation;

        @Getter
        private final Station destinationStation;
    }
}
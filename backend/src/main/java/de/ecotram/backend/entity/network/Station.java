package de.ecotram.backend.entity.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public final class Station extends Traversable {
    public static final String DEFAULT_NAME = "Station";
    public static final int DEFAULT_MAX_PASSENGERS = 50;
    public static final int DEFAULT_CURRENT_PASSENGERS = 0;

    @Setter
    private String name = DEFAULT_NAME;

    @Setter
    private int maxPassengers = DEFAULT_MAX_PASSENGERS;

    @Setter
    private int currentPassengers = DEFAULT_CURRENT_PASSENGERS;

    @OneToMany(mappedBy = "sourceStation")
    private Set<Connection> sourceConnections = new HashSet<>();

    @OneToMany(mappedBy = "destinationStation")
    private Set<Connection> destinationConnections = new HashSet<>();

    public Stream<Station> getReachableStations() {
        return this.sourceConnections
                .stream()
                .map(Connection::getDestinationStation);
    }

    public Stream<Station> getReachingStations() {
        return this.destinationConnections
                .stream()
                .map(Connection::getSourceStation);
    }

    public Optional<Connection> getConnectionTo(Station destination) {
        return this.sourceConnections
                .stream()
                .filter(c -> c.getDestinationStation() == destination)
                .findFirst();
    }

    public Connection connectTo(Station destination, Connection.Builder.ModifyDelegate modifyBuilder) {
        Connection connection = modifyBuilder.Modify(
                Connection.builder().sourceStation(this).destinationStation(destination)
        ).build();

        this.sourceConnections.add(connection);
        destination.destinationConnections.add(connection);
        return connection;
    }

    // do not set dest or source station inside the lambda
    public ConnectionPair connectToAndFrom(Station destination, Connection.Builder.ModifyDelegate modifyBuilder) {
        Connection connectionTo = modifyBuilder.Modify(
                Connection.builder().sourceStation(this).destinationStation(destination)
        ).build();
        Connection connectionFrom = modifyBuilder.Modify(
                Connection.builder().sourceStation(destination).destinationStation(this)
        ).build();

        this.sourceConnections.add(connectionTo);
        destination.destinationConnections.add(connectionTo);

        this.destinationConnections.add(connectionFrom);
        destination.sourceConnections.add(connectionFrom);
        return new ConnectionPair(connectionTo, connectionFrom);
    }

    // do not set dest or source station inside the lambda
    public ConnectionPair connectToAndFrom(
            Station destination,
            Connection.Builder.ModifyDelegate modifyToBuilder,
            Connection.Builder.ModifyDelegate modifyFromBuilder
    ) {
        Connection connectionTo = modifyToBuilder.Modify(
                Connection.builder().sourceStation(this).destinationStation(destination)
        ).build();

        this.sourceConnections.add(connectionTo);
        destination.destinationConnections.add(connectionTo);

        Connection connectionFrom = modifyFromBuilder.Modify(
                Connection.builder().sourceStation(destination).destinationStation(this)
        ).build();

        this.destinationConnections.add(connectionFrom);
        destination.sourceConnections.add(connectionFrom);

        return new ConnectionPair(connectionTo, connectionFrom);
    }

    public static final record ConnectionPair(Connection connection1, Connection connection2) {
    }
}
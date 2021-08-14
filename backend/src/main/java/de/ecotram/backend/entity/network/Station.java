package de.ecotram.backend.entity.network;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.ecotram.backend.entity.EntityBase;
import de.ecotram.backend.entity.LineEntry;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

@Getter
@Entity
@NoArgsConstructor
public final class Station extends EntityBase {
    public static final String DEFAULT_NAME = "Station";
    public static final int DEFAULT_MAX_PASSENGERS = 50;

    @Setter
    private String name = DEFAULT_NAME;

    @Setter
    private int maxPassengers = DEFAULT_MAX_PASSENGERS;

    @OneToMany(mappedBy = "station")
    @JsonBackReference
    private Set<LineEntry> lines = new HashSet<>();

    @Setter
    @ManyToOne(cascade = {CascadeType.ALL})
    @JsonBackReference
    private Network network;

    @OneToMany(mappedBy = "sourceStation")
    @JsonBackReference
    private Set<Connection> sourceConnections = new HashSet<>();

    @OneToMany(mappedBy = "destinationStation")
    @JsonBackReference
    private Set<Connection> destinationConnections = new HashSet<>();

    @JsonIgnore
    public Stream<Station> getReachableStations() {
        return this.sourceConnections
                .stream()
                .map(Connection::getDestinationStation);
    }

    @JsonIgnore
    public Stream<Station> getReachingStations() {
        return this.destinationConnections
                .stream()
                .map(Connection::getSourceStation);
    }

    @JsonIgnore
    public Optional<Connection> getConnectionTo(Station destination) {
        return this.sourceConnections
                .stream()
                .filter(c -> c.getDestinationStation().getId() == destination.getId())
                .findFirst();
    }

    public Connection connectTo(Station destination, Function<Connection.Builder, Connection.Builder> modifyBuilder) {
        Connection connection = modifyBuilder.apply(Connection.builder())
                .sourceStation(this)
                .destinationStation(destination)
                .build();

        this.sourceConnections.add(connection);
        destination.destinationConnections.add(connection);
        return connection;
    }

    public Connection.Pair connectToAndFrom(Station destination, Function<Connection.Builder, Connection.Builder> modifyBuilder) {
        Connection connectionTo = modifyBuilder.apply(Connection.builder())
                .sourceStation(this)
                .destinationStation(destination)
                .build();

        this.sourceConnections.add(connectionTo);
        destination.destinationConnections.add(connectionTo);

        Connection connectionFrom = modifyBuilder.apply(Connection.builder())
                .sourceStation(destination)
                .destinationStation(this)
                .build();

        this.destinationConnections.add(connectionFrom);
        destination.sourceConnections.add(connectionFrom);
        return new Connection.Pair(connectionTo, connectionFrom);
    }

    @JsonIgnore
    public Connection.Pair connectToAndFrom(
            Station destination,
            Function<Connection.Builder, Connection.Builder> modifyToBuilder,
            Function<Connection.Builder, Connection.Builder> modifyFromBuilder
    ) {
        Connection connectionTo = modifyToBuilder.apply(Connection.builder())
                .sourceStation(this)
                .destinationStation(destination)
                .build();

        this.sourceConnections.add(connectionTo);
        destination.destinationConnections.add(connectionTo);

        Connection connectionFrom = modifyFromBuilder.apply(Connection.builder())
                .sourceStation(destination)
                .destinationStation(this)
                .build();

        this.destinationConnections.add(connectionFrom);
        destination.sourceConnections.add(connectionFrom);

        return new Connection.Pair(connectionTo, connectionFrom);
    }

    private Station(Builder builder) {
        this.name = builder.name;
        this.maxPassengers = builder.maxPassengers;
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static final class Builder {
        private String name = Station.DEFAULT_NAME;
        private int maxPassengers = Station.DEFAULT_MAX_PASSENGERS;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder maxPassengers(int maxPassengers) {
            this.maxPassengers = maxPassengers;
            return this;
        }

        public Station build() {
            return new Station(this);
        }

        @FunctionalInterface
        public interface ModifyDelegate {
            Builder Modify(Builder a);
        }
    }
}
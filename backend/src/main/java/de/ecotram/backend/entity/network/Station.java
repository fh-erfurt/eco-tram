package de.ecotram.backend.entity.network;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Getter
@Entity
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
    @Transient
    private int currentPassengers = DEFAULT_CURRENT_PASSENGERS;

    @ManyToOne
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

    @JsonIgnore
    public Connection connectTo(Station destination, Connection.Builder.ModifyDelegate modifyBuilder) {
        Connection connection = modifyBuilder.Modify(Connection.builder())
                .sourceStation(this)
                .destinationStation(destination)
                .build();

        this.sourceConnections.add(connection);
        destination.destinationConnections.add(connection);
        return connection;
    }

    @JsonIgnore
    public Connection.Pair connectToAndFrom(Station destination, Connection.Builder.ModifyDelegate modifyBuilder) {
        Connection connectionTo = modifyBuilder.Modify(Connection.builder())
                .sourceStation(this)
                .destinationStation(destination)
                .build();

        this.sourceConnections.add(connectionTo);
        destination.destinationConnections.add(connectionTo);

        Connection connectionFrom = modifyBuilder.Modify(Connection.builder())
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
            Connection.Builder.ModifyDelegate modifyToBuilder,
            Connection.Builder.ModifyDelegate modifyFromBuilder
    ) {
        Connection connectionTo = modifyToBuilder.Modify(Connection.builder())
                .sourceStation(this)
                .destinationStation(destination)
                .build();

        this.sourceConnections.add(connectionTo);
        destination.destinationConnections.add(connectionTo);

        Connection connectionFrom = modifyFromBuilder.Modify(Connection.builder())
                .sourceStation(destination)
                .destinationStation(this)
                .build();

        this.destinationConnections.add(connectionFrom);
        destination.sourceConnections.add(connectionFrom);

        return new Connection.Pair(connectionTo, connectionFrom);
    }

    private Station(Builder builder) {
        this.length = builder.length;
        this.maxWeight = builder.maxWeight;
        this.trafficFactor = builder.trafficFactor;
        this.name = builder.name;
        this.maxPassengers = builder.maxPassengers;
        this.currentPassengers = builder.currentPassengers;
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static final class Builder {
        private int length = Traversable.DEFAULT_LENGTH;
        private int maxWeight = Traversable.DEFAULT_MAX_WEIGHT;
        private float trafficFactor = Traversable.DEFAULT_TRAFFIC_FACTOR;
        private String name = Station.DEFAULT_NAME;
        private int maxPassengers = Station.DEFAULT_MAX_PASSENGERS;
        private int currentPassengers = Station.DEFAULT_CURRENT_PASSENGERS;

        public Builder length(int length) {
            this.length = length;
            return this;
        }

        public Builder maxWeight(int maxWeight) {
            this.maxWeight = maxWeight;
            return this;
        }

        public Builder trafficFactor(float trafficFactor) {
            this.trafficFactor = trafficFactor;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder maxPassengers(int maxPassengers) {
            this.maxPassengers = maxPassengers;
            return this;
        }

        public Builder currentPassengers(int currentPassengers) {
            this.currentPassengers = currentPassengers;
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
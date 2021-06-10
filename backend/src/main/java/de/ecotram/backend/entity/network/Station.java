package de.ecotram.backend.entity.network;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Entity
public final class Station extends Traversable {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int maxPassengers;

    @Getter
    @Setter
    private int currentPassengers;

    @Getter
    @OneToMany(mappedBy = "sourceStation")
    private Set<Connection> sourceConnections = new HashSet<>();

    @Getter
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

    @Override
    public String toString() {
        return "Station{name='" + name + "'}";
    }
}
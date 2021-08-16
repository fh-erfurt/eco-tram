package de.ecotram.backend.entity.network;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.ecotram.backend.entity.EntityBase;
import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.LineEntry;
import de.ecotram.backend.utilities.NetworkUtilities;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Entity
@NoArgsConstructor
public final class Network extends EntityBase {
    @OneToMany(mappedBy = "network")
    @JsonManagedReference
    private Set<Station> stations;

    @Transient
    private boolean isInitialized;

    @Transient
    private Map<Station, Set<Station>> adjacencyMap = new HashMap<>();

    @Transient
    private Map<Station, NetworkUtilities.DistanceTree> minimalDistanceTree = new HashMap<>();

    private Network(Set<Station> stations) {
        this.stations = stations;
    }

    public static Network fromStations(Set<Station> stations) {
        Network network = new Network(stations);
        stations.forEach(s -> s.setNetwork(network)); // trash orm requirement

        network.initialize();

        return network;
    }

    public void initialize() {
        if (this.isInitialized)
            return;

        // map all adjacent stations
        for (Station station : this.stations) {
            this.adjacencyMap.put(station, station.getDestinationConnections()
                    .stream()
                    .map(Connection::getDestinationStation)
                    .collect(Collectors.toSet())
            );
        }

        // create the minimal spanning trees for each station
        for (Station station : this.stations) {
            this.minimalDistanceTree.put(station, NetworkUtilities.dijkstra(station, this.adjacencyMap));
        }

        this.isInitialized = true;
    }

    public Stream<Line> getLines() {
        return this.stations.stream()
                .map(Station::getLines)
                .flatMap(Set::stream)
                .map(LineEntry::getLine)
                .distinct();
    }

    public List<Station> getPathTo(Station start, Station destination) {
        if (!this.adjacencyMap.containsKey(start))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.adjacencyMap.containsKey(destination))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.isInitialized)
            throw new IllegalStateException("This network was not initialized.");

        return this.minimalDistanceTree.get(start).getPathTo(destination);
    }

    public int getDistance(Station start, Station destination) {
        if (!this.adjacencyMap.containsKey(start))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.adjacencyMap.containsKey(destination))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!isInitialized)
            throw new IllegalStateException("This network was not initialized.");

        return this.minimalDistanceTree.get(start).getDistanceTo(destination);
    }

    public int getHops(Station start, Station destination) {
        if (!this.adjacencyMap.containsKey(start))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.adjacencyMap.containsKey(destination))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.isInitialized)
            throw new IllegalStateException("This network was not initialized.");

        return this.minimalDistanceTree.get(start).getHopsTo(destination);
    }

    public Station getPrevious(Station start, Station destination) {
        if (!this.adjacencyMap.containsKey(start))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.adjacencyMap.containsKey(destination))
            throw new IllegalArgumentException("The destination was not part of this network.");

        return this.minimalDistanceTree.get(start).getPreviousOf(destination);
    }
}
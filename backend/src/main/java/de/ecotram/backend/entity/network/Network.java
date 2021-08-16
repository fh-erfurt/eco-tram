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

/**
 * A graph of stations representing a city's tram network.
 */
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
    private final Map<Station, Set<Station>> adjacencyMap = new HashMap<>();

    @Transient
    private final Map<Station, NetworkUtilities.MinimalDistanceTree> minimalDistanceTree = new HashMap<>();

    private Network(Set<Station> stations) {
        this.stations = stations;
    }

    /**
     * Returns a new initialized network, that is, a network with a minimal distance tree for each station.
     *
     * @return A new network ready to be used for path finding.
     */
    public static Network fromStations(Set<Station> stations) {
        Network network = new Network(stations);
        stations.forEach(s -> s.setNetwork(network)); // wired up for correct jpa insertion

        network.initialize();

        return network;
    }

    /**
     * Initializes this network for use, that is, creating a minimal distance tree for each station of this network.
     * The algorithm used for finding the shortest paths is an implementation of Dijkstra's Algorithm.
     */
    private void initialize() {
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
                .map(Station::getLineEntries)
                .flatMap(Set::stream)
                .map(LineEntry::getLine)
                .distinct();
    }

    /**
     * Returns an ordered list of stations with the shortest path (measured by distance, not hops) from the start to the
     * destination station.
     *
     * @return The shortest path between 2 stations.
     */
    public List<Station> getPathTo(Station start, Station destination) {
        if (!this.adjacencyMap.containsKey(start))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.adjacencyMap.containsKey(destination))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.isInitialized)
            throw new IllegalStateException("This network was not initialized.");

        return this.minimalDistanceTree.get(start).getPathTo(destination);
    }

    /**
     * Gets the total distance in meters from the start station to the destination station.
     *
     * @return The distance in meters.
     */
    public int getDistance(Station start, Station destination) {
        if (!this.adjacencyMap.containsKey(start))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.adjacencyMap.containsKey(destination))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!isInitialized)
            throw new IllegalStateException("This network was not initialized.");

        return this.minimalDistanceTree.get(start).getDistanceTo(destination);
    }

    /**
     * Returns the amount of connections between the start station and the destination station.
     *
     * @return The number of connections.
     */
    public int getHops(Station start, Station destination) {
        if (!this.adjacencyMap.containsKey(start))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.adjacencyMap.containsKey(destination))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.isInitialized)
            throw new IllegalStateException("This network was not initialized.");

        return this.minimalDistanceTree.get(start).getHopsTo(destination);
    }


    /**
     * Returns the previous station in the minimal path form the start station to the destination station, that is, the
     * station that comes before the destination station.
     *
     * @return The second to last station in the minimal path.
     */
    public Station getPrevious(Station start, Station destination) {
        if (!this.adjacencyMap.containsKey(start))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.adjacencyMap.containsKey(destination))
            throw new IllegalArgumentException("The destination was not part of this network.");

        return this.minimalDistanceTree.get(start).getPreviousOf(destination);
    }
}
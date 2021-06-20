package de.ecotram.backend.entity.network;

import de.ecotram.backend.entity.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
public final class Network extends EntityBase {
    @Getter
    @OneToMany(mappedBy = "network")
    private Set<Station> stations;

    @Getter
    @Transient
    private Map<Station, Set<Station>> adjacencyMap = new HashMap<>();

    @Getter
    @Transient
    private Map<Station, MinimalSpanningTree> minimalSpanningTrees = new HashMap<>();

    private Network(Set<Station> stations) {
        this.stations = stations;
    }

    public static Network fromStations(Set<Station> stations) {
        Network network = new Network(stations);

        // map all adjacent stations
        for (Station station : network.stations) {
            network.adjacencyMap.put(station, station.getDestinationConnections()
                    .stream()
                    .map(Connection::getDestinationStation)
                    .collect(Collectors.toSet())
            );
        }

        // create the minimal spanning trees for each station
        for (Station station : network.stations) {
            network.minimalSpanningTrees.put(station, dijkstra(station, network.adjacencyMap));
        }

        return network;
    }

    // TODO(erik): optimize
    public static MinimalSpanningTree dijkstra(Station start, Map<Station, Set<Station>> adjacencyMap) {
        if (!adjacencyMap.containsKey(start))
            throw new InvalidParameterException("The start station does not exist in this network.");

        // initialize
        Station currentMinimum = start;
        Set<Station> traversed = new HashSet<>(adjacencyMap.size());
        Set<Station> nodeQueue = new HashSet<>() {{
            add(start);
        }};
        Map<Station, DijkstraTuple> distanceMap = new HashMap<>(adjacencyMap.keySet()
                .stream()
                .collect(Collectors.toMap(k -> k, v -> new DijkstraTuple(Integer.MAX_VALUE, 0, null)))
        );

        distanceMap.get(currentMinimum).distance = 0;

        // avoid constant re-allocation of unchanging collection
        Set<Map.Entry<Station, DijkstraTuple>> distanceEntrySet = distanceMap.entrySet();

        while (!nodeQueue.isEmpty()) {
            // compute distances and previous
            for (Map.Entry<Station, DijkstraTuple> entry : distanceEntrySet) {
                Station key = entry.getKey();
                DijkstraTuple value = entry.getValue();

                // get distance min -> entry
                Optional<Connection> minimumToCurrent = currentMinimum.getConnectionTo(key);
                if (minimumToCurrent.isPresent()) {
                    // distance from start to current
                    int distance = currentMinimum == start
                            ? minimumToCurrent.get().getLength()
                            : distanceMap.get(currentMinimum).distance + minimumToCurrent.get().getLength();

                    // hops from start to current
                    int hops = key == start ? 0
                            : currentMinimum == start ? 1
                            : distanceMap.get(currentMinimum).hops + 1;

                    // if smaller assign as new previous etc
                    if (value.distance > distance) {
                        value.distance = distance;
                        value.hops = hops;
                        value.previous = currentMinimum;

                        // add to queue
                        if (!traversed.contains(key))
                            nodeQueue.add(key);
                    }
                }
            }

            // find new minimum
            currentMinimum = nodeQueue
                    .stream()
                    .min(Comparator.comparingInt(entry -> distanceMap.get(entry).distance))
                    .get();

            // mark traversed
            traversed.add(currentMinimum);
            nodeQueue.remove(currentMinimum);
        }

        return new MinimalSpanningTree(start, distanceMap);
    }

    public List<Station> getPathTo(Station start, Station destination) {
        if (!this.adjacencyMap.containsKey(start))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.adjacencyMap.containsKey(destination))
            throw new IllegalArgumentException("The destination was not part of this network.");

        return this.minimalSpanningTrees.get(start).getPathTo(destination);
    }

    public int getDistance(Station start, Station destination) {
        if (!this.adjacencyMap.containsKey(start))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.adjacencyMap.containsKey(destination))
            throw new IllegalArgumentException("The destination was not part of this network.");

        return this.minimalSpanningTrees.get(start).getDistanceTo(destination);
    }

    public int getHops(Station start, Station destination) {
        if (!this.adjacencyMap.containsKey(start))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.adjacencyMap.containsKey(destination))
            throw new IllegalArgumentException("The destination was not part of this network.");

        return this.minimalSpanningTrees.get(start).getHopsTo(destination);
    }

    public Station getPrevious(Station start, Station destination) {
        if (!this.adjacencyMap.containsKey(start))
            throw new IllegalArgumentException("The destination was not part of this network.");

        if (!this.adjacencyMap.containsKey(destination))
            throw new IllegalArgumentException("The destination was not part of this network.");

        return this.minimalSpanningTrees.get(start).getPreviousOf(destination);
    }

    @Data
    @AllArgsConstructor
    protected static final class DijkstraTuple {
        private int distance;
        private int hops;
        private Station previous;
    }

    protected static final record MinimalSpanningTree(Station root, Map<Station, DijkstraTuple> paths) {
        public List<Station> getPathTo(Station destination) {
            LinkedList<Station> list = new LinkedList<>();

            Station previous = destination;
            while (previous != null) {
                list.addFirst(previous);
                previous = paths.get(previous).previous;
            }

            return list;
        }

        public int getDistanceTo(Station destination) {
            return this.paths.get(destination).getDistance();
        }

        public int getHopsTo(Station destination) {
            return this.paths.get(destination).getHops();
        }

        public Station getPreviousOf(Station destination) {
            return this.paths.get(destination).getPrevious();
        }
    }
}
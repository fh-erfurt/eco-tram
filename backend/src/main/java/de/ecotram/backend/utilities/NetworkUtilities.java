package de.ecotram.backend.utilities;

import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public final class NetworkUtilities {
    /**
     * Creates new minimal distance tree for the given start station spanning to each other station across the adjacency
     * map. This method is ripe for optimization.
     *
     * @param adjacencyMap The adjacency map of the graph to build the tree for.
     * @return A new minimal distance tree for the given start station.
     */
    public static MinimalDistanceTree dijkstra(Station start, Map<Station, Set<Station>> adjacencyMap) {
        if (!adjacencyMap.containsKey(start))
            throw new InvalidParameterException("The start station does not exist in this network.");

        // initialize
        final AtomicReference<Station> currentMinimum = new AtomicReference<>(start);
        Set<Station> traversed = new HashSet<>(adjacencyMap.size());
        Set<Station> nodeQueue = new HashSet<>(adjacencyMap.size()) {{
            add(start);
        }};
        Map<Station, NetworkUtilities.DijkstraTuple> distanceMap = new HashMap<>(adjacencyMap.keySet()
                .stream()
                .collect(Collectors.toMap(
                        k -> k,
                        v -> new NetworkUtilities.DijkstraTuple(Integer.MAX_VALUE, 0, null))
                )
        );

        distanceMap.get(currentMinimum.get()).distance = 0;

        // loop until none is left in queue
        while (!nodeQueue.isEmpty()) {
            currentMinimum.get().getSourceConnections().stream()
                    .map(Connection::getDestinationStation)
                    .forEach(adjacentStation -> {
                        DijkstraTuple value = distanceMap.get(adjacentStation);
                        Connection minimumToCurrent = currentMinimum.get()
                                .getConnectionTo(adjacentStation)
                                .orElseThrow();

                        // get minimal distance
                        int distance = currentMinimum.get() == start
                                ? minimumToCurrent.getLength()
                                : distanceMap.get(currentMinimum.get()).distance + minimumToCurrent.getLength();

                        // if smaller assign as new previous etc
                        if (value.distance > distance) {
                            value.distance = distance;
                            value.previous = currentMinimum.get();
                            value.hops = adjacentStation == start ? 0 : distanceMap.get(currentMinimum.get()).hops + 1;

                            // add to queue
                            if (!traversed.contains(adjacentStation))
                                nodeQueue.add(adjacentStation);
                        }
                    });

            // find new minimum
            currentMinimum.set(nodeQueue
                    .stream()
                    .min(Comparator.comparingInt(entry -> distanceMap.get(entry).distance))
                    .orElseThrow()
            );

            // mark as traversed
            traversed.add(currentMinimum.get());
            nodeQueue.remove(currentMinimum.get());
        }

        return new MinimalDistanceTree(start, distanceMap);
    }

    /**
     * A tuple used for a value for a MinimalDistanceTree of a Network.
     */
    @Data
    @AllArgsConstructor
    public static final class DijkstraTuple {
        private int distance;
        private int hops;
        private Station previous;
    }

    /**
     * A tree across a graph with minimal distance paths for a given start station.
     */
    public static final record MinimalDistanceTree(Station root, Map<Station, DijkstraTuple> paths) {
        public List<Station> getPathTo(Station destination) {
            LinkedList<Station> list = new LinkedList<>();

            Station previous = destination;
            while (previous != null) {
                list.addFirst(previous);
                previous = this.paths.get(previous).previous;
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

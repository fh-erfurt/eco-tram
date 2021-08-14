package de.ecotram.backend.utilities;

import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

public final class NetworkUtilities {
    // TODO(erik): optimize
    public static DistanceTree dijkstra(Station start, Map<Station, Set<Station>> adjacencyMap) {
        if (!adjacencyMap.containsKey(start))
            throw new InvalidParameterException("The start station does not exist in this network.");

        // initialize
        Station currentMinimum = start;
        Set<Station> traversed = new HashSet<>(adjacencyMap.size());
        Set<Station> nodeQueue = new HashSet<>() {{
            add(start);
        }};
        Map<Station, NetworkUtilities.DijkstraTuple> distanceMap = new HashMap<>(adjacencyMap.keySet()
                .stream()
                .collect(Collectors.toMap(
                        k -> k,
                        v -> new NetworkUtilities.DijkstraTuple(Integer.MAX_VALUE, 0, null))
                )
        );

        distanceMap.get(currentMinimum).distance = 0;

        // avoid constant re-allocation of unchanging collection
        Set<Map.Entry<Station, NetworkUtilities.DijkstraTuple>> distanceEntrySet = distanceMap.entrySet();

        while (!nodeQueue.isEmpty()) {
            // compute distances and previous
            for (Map.Entry<Station, NetworkUtilities.DijkstraTuple> entry : distanceEntrySet) {
                Station key = entry.getKey();
                NetworkUtilities.DijkstraTuple value = entry.getValue();

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

        return new DistanceTree(start, distanceMap);
    }

    @Data
    @AllArgsConstructor
    public static final class DijkstraTuple {
        private int distance;
        private int hops;
        private Station previous;
    }

    public static final record DistanceTree(Station root, Map<Station, DijkstraTuple> paths) {
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

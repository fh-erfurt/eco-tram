package de.ecotram.backend.entity.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.*;
import java.util.stream.Collectors;

public final class Network {
    @Getter
    private final Map<Station, Set<Station>> adjacencyMap = new HashMap<>();

    public Network(List<Station> stations) {
        for (Station station : stations) {
            if (!this.adjacencyMap.containsKey(station)) {
                this.adjacencyMap.put(station, station.getDestinationConnections()
                        .stream()
                        .map(Connection::getDestinationStation)
                        .collect(Collectors.toSet())
                );

            }
        }
    }

    // TODO: cache results
    public Map<Station, DijkstraTuple> pathDijkstra(Station start) {
        if (!this.adjacencyMap.containsKey(start))
            throw new InvalidParameterException("The start station does not exist in this network.");

        // initialize
        Station currentMinimum = start;
        Set<Station> traversed = new HashSet<>(adjacencyMap.size());
        Set<Station> nodeQueue = new HashSet<>() {{
            add(start);
        }};
        Map<Station, DijkstraTuple> distanceMap = new HashMap<>(adjacencyMap.keySet()
                .stream()
                .collect(Collectors.toMap(k -> k, v -> new DijkstraTuple(Integer.MAX_VALUE, null)))
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

                    // if smaller assign as new previous etc
                    if (value.distance > distance) {
                        value.distance = distance;
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

        return distanceMap;
    }

    @AllArgsConstructor
    public final class DijkstraTuple {
        @Getter
        @Setter
        private int distance;

        @Getter
        @Setter
        private Station previous;
    }
}
package de.ecotram.backend.simulation;

import de.ecotram.backend.entity.network.Network;
import lombok.Getter;

// TODO(erik): a tram schedule
public final class Schedule {
    @Getter
    private final Network network;

    public Schedule(Network network) {
        this.network = network;
    }
}
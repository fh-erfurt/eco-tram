package de.ecotram.backend.simulation;

import de.ecotram.backend.entity.Tram;
import de.ecotram.backend.entity.network.Network;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public final class Schedule {
    @Getter
    private final Network network;

    @Getter
    private final Map<Tram, TramSchedule> tramSchedules = new HashMap<>();

    public Schedule(Network network) {
        this.network = network;
    }

    // TODO(erik): ergonomic way of creating a tramSchedule for each tram
}
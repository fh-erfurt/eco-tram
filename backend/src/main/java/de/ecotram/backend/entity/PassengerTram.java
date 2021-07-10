package de.ecotram.backend.entity;

import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;

@Getter
@Entity
public final class PassengerTram extends EntityBase {
    public static final int DEFAULT_WEIGHT = 5000; // kg
    public static final int DEFAULT_MAX_SPEED = 50; // km/h
    public static final int DEFAULT_SPEED = 50; // km/h

    @Setter
    private int weight = DEFAULT_WEIGHT;

    @Setter
    private int maxSpeed = DEFAULT_MAX_SPEED;

    @Setter
    private int maxPassengers;

    @Setter
    @Transient
    private int speed = DEFAULT_SPEED;

    @Setter
    @Transient
    private int currentPassengers;

    @Transient
    private int currentIndex;

    @Transient
    private final List<LineEntry> route;

    public PassengerTram() {
        this.route = null;
    }

    public PassengerTram(List<LineEntry> route) {
        this.route = route;
    }

    // returns next connection
    public Connection nextStation() {
        if (route == null)
            throw new IllegalStateException("Tram without cannot advance without route.");

        if (currentIndex + 1 >= route.size())
            currentIndex = 0;

        Station from = route.get(currentIndex).getStation();
        Station to = route.get(++currentIndex).getStation();

        return from.getConnectionTo(to).orElseThrow();
    }
}
package de.ecotram.backend.entity;

import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.List;

@MappedSuperclass
public abstract class Tram extends EntityBase {

    private final int weight;
    private final String tramType;
    private final int speed;

    @ManyToMany
    private List<Line> queuedLines;

    @Transient
    private int currentIndex;

    protected Tram(int weight, int speed, String tramType) {
        if (weight < 0)
            throw new IllegalArgumentException("Weight of `" + this + "` cannot to be negative.");
        if (speed < 0)
            throw new IllegalArgumentException("Speed of `" + this + "` cannot to be negative.");
        if (tramType == null || tramType.isEmpty())
            throw new IllegalArgumentException("TramType of `" + this + "` cannot be null or empty.");

        this.weight = weight;
        this.speed = speed;
        this.tramType = tramType;
    }

}

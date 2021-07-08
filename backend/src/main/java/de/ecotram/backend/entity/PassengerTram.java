package de.ecotram.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Getter
@Setter
@Entity
@NoArgsConstructor
public final class PassengerTram extends EntityBase {
    public static final int DEFAULT_WEIGHT = 5000; // kg
    public static final int DEFAULT_MAX_SPEED = 50; // km/h
    public static final int DEFAULT_SPEED = 50; // km/h

    private int weight = DEFAULT_WEIGHT;
    private int maxSpeed = DEFAULT_MAX_SPEED;
    private int maxPassengers;

    @Transient
    private int speed = DEFAULT_SPEED;

    @Transient
    private int currentPassengers;

    @Transient
    private Line currentLine;

    @Transient
    private LineEntry currentLineEntry;

    public int advance() {
        // TODO(erik): return time it takes to traverse to next station
        // TODO: make line entries only stations?
        return 60; // s
    }
}
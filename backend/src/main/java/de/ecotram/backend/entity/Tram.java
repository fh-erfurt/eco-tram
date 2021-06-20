package de.ecotram.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@Getter
@Setter
@MappedSuperclass
public abstract class Tram extends EntityBase {
    public static final int DEFAULT_WEIGHT = 5000; // kg
    public static final int DEFAULT_MAX_SPEED = 50; // km/h
    public static final int DEFAULT_SPEED = 0; // km/h

    protected int weight = DEFAULT_WEIGHT;
    protected int maxSpeed = DEFAULT_MAX_SPEED;

    @Transient
    protected int speed = DEFAULT_SPEED;
}
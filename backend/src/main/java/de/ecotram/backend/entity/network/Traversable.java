package de.ecotram.backend.entity.network;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.ecotram.backend.entity.EntityBase;
import de.ecotram.backend.entity.Line;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Traversable extends EntityBase {
    public static final int DEFAULT_LENGTH = 100; // m
    public static final int DEFAULT_MAX_WEIGHT = 5000; // kg
    public static final float DEFAULT_TRAFFIC_FACTOR = 1.0f; // speed multiplier

    @Setter
    protected int length = DEFAULT_LENGTH;

    @Setter
    protected int maxWeight = DEFAULT_MAX_WEIGHT;

    @Setter
    @Transient
    protected float trafficFactor = DEFAULT_TRAFFIC_FACTOR;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JsonIgnore
    private Set<Line> lines = new HashSet<>();
}
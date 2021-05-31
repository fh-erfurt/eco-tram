package de.ecotram.backend.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance( strategy = InheritanceType.JOINED )
public abstract class Traversable extends EntityBase {

    private float length;
    private int maxWeight;
    private float trafficFactor;

    @Getter
    @ManyToMany
    private Set<Line> lines = new HashSet<>();

}

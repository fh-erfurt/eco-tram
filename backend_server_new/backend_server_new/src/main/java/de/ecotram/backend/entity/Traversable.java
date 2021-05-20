package de.ecotram.backend.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance( strategy = InheritanceType.JOINED )
public abstract class Traversable extends EntityBase {

    private float length;
    private int maxWeight;
    private float trafficFactor;

    @ManyToMany
    private List<Line> lines;

}

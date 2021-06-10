package de.ecotram.backend.entity.network;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.ecotram.backend.entity.EntityBase;
import de.ecotram.backend.entity.Line;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Traversable extends EntityBase {
    @Getter
    @Setter
    private float length;

    @Getter
    @Setter
    private int maxWeight;

    @Getter
    @Setter
    private float trafficFactor;

    @Getter
    @ManyToMany(cascade = {CascadeType.ALL})
    @JsonIgnore
    private Set<Line> lines = new HashSet<>();
}
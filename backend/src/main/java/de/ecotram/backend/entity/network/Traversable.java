package de.ecotram.backend.entity.network;

import de.ecotram.backend.entity.EntityBase;
import de.ecotram.backend.entity.Line;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Traversable extends EntityBase {
    @Getter
    @Setter
    private int length;

    @Getter
    @Setter
    private int maxWeight;

    @Getter
    @Setter
    private float trafficFactor;

    @Getter
    @ManyToMany
    private Set<Line> lines = new HashSet<>();
}
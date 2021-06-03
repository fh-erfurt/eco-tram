package de.ecotram.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.List;

@MappedSuperclass
public abstract class Tram extends EntityBase {
    @Getter
    @Setter
    private int weight;

    @Getter
    @Setter
    private int speed;
}
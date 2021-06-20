package de.ecotram.backend.entity;

import de.ecotram.backend.entity.network.Traversable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public final class Line extends EntityBase {
    @Setter
    private String name;

    // TODO: can this really be duplicate-free?
    @Getter
    @ManyToMany(mappedBy = "lines", cascade = {CascadeType.ALL})
    private Set<Traversable> route = new HashSet<>();
}
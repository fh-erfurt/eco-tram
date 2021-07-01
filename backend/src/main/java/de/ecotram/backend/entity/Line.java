package de.ecotram.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.ecotram.backend.entity.network.Traversable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public final class Line extends EntityBase {
    @Setter
    private String name;

    // TODO: can this really be duplicate-free?
//    @Getter
//    @ManyToMany(mappedBy = "lines", cascade = {CascadeType.ALL})
//    private Set<Traversable> route = new HashSet<>();

    @OneToMany(mappedBy = "line")
    @JsonManagedReference
    private Set<LineEntry> route = new HashSet<>();
}
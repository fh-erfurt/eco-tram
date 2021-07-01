package de.ecotram.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
public final class Line extends EntityBase {
    @Setter
    private String name;

    @OneToMany(mappedBy = "line")
    @JsonManagedReference
    private Set<LineEntry> route = new HashSet<>();
}
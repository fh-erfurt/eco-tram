package de.ecotram.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Line extends EntityBase {

    @Getter
    @Setter
    private String name;

    @Getter
    @ManyToMany( mappedBy = "lines" )
    private Set<Traversable> route = new HashSet<>();
}

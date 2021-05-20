package de.ecotram.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends EntityBase {

    @Getter
    @Setter
    private String name;

    @ManyToMany( mappedBy = "lines" )
    private List<Traversable> route;


}

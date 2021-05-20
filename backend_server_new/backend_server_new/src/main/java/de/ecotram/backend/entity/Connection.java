package de.ecotram.backend.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Connection extends Traversable {

    @ManyToOne
    private Station sourceStation;

    @ManyToOne
    private Station destinationStation;

}

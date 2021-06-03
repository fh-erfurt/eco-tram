package de.ecotram.backend.entity.network;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public final class Connection extends Traversable {
    @Getter
    @ManyToOne
    private Station sourceStation;

    @Getter
    @ManyToOne
    private Station destinationStation;
}
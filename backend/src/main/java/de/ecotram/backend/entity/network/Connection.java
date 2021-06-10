package de.ecotram.backend.entity.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public final class Connection extends Traversable {
    @Getter
    @ManyToOne
    private Station sourceStation;

    @Getter
    @ManyToOne
    private Station destinationStation;
}
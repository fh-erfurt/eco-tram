package de.ecotram.backend.entity.network;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public final class Connection extends Traversable {

    @Setter
    @Getter
    @ManyToOne
    @JsonManagedReference
    private Station sourceStation;

    @Setter
    @Getter
    @ManyToOne
    @JsonManagedReference
    private Station destinationStation;
}
package de.ecotram.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
public final class PassengerTram extends Tram {
    @Getter
    @Setter
    private int maxPassengers;

    @Getter
    @Setter
    private int currentPassengers;
}

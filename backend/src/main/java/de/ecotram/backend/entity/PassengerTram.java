package de.ecotram.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Getter
@Setter
@Entity
@NoArgsConstructor
public final class PassengerTram extends Tram {
    private int maxPassengers;

    @Transient
    private int currentPassengers;
}
package de.ecotram.backend.entity;

import javax.persistence.Entity;

@Entity
public class PassengerTram extends Tram {

    private final int maxPassengers;
    private int currentPassengers;

    public PassengerTram(int weight, int speed, String tramType, int maxPassengers) {
        super(weight, speed, tramType);

        if (maxPassengers < 0)
            throw new IllegalArgumentException("MaxPassengers of `" + this + "` cannot to be negative.");

        this.maxPassengers = maxPassengers;
    }

    public PassengerTram() {
        super(0,0,"tram");
        this.maxPassengers = 0;
    }
}

package de.ecotram.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Station extends Traversable {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int maxPassengers;

    @Getter
    @Setter
    private int currentPassengers;

    @Getter
    @Setter
    private float waitingTime;

    @OneToMany( mappedBy = "sourceStation" )
    private List<Connection> sourceConnections;

    @OneToMany( mappedBy = "destinationStation" )
    private List<Connection> destinationConnections;

}

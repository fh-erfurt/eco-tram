package de.ecotram.backend.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Station extends Traversable {

    private String name;
    private int maxPassengers;
    private int currentPassengers;
    private float waitingTime;

    @OneToMany( mappedBy = "sourceStation" )
    private List<Connection> sourceConnections;

    @OneToMany( mappedBy = "destinationStation" )
    private List<Connection> destinationConnections;

}

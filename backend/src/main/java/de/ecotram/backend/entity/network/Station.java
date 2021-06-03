package de.ecotram.backend.entity.network;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public final class Station extends Traversable {
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
    @OneToMany(mappedBy = "sourceStation")
    private List<Connection> sourceConnections;

    @Getter
    @OneToMany(mappedBy = "destinationStation")
    private List<Connection> destinationConnections;
}
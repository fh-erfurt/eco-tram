package de.ecotram.backend.entity.network;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @JsonBackReference
    private Set<Connection> sourceConnections = new HashSet<>();;

    @Getter
    @OneToMany(mappedBy = "destinationStation")
    @JsonBackReference
    private Set<Connection> destinationConnections = new HashSet<>();;
}
package de.ecotram.backend.entity.network;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.ecotram.backend.entity.EntityBase;
import de.ecotram.backend.entity.LineEntry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * A single node inside a city's tram network.
 */
@Getter
@Entity
@NoArgsConstructor
public final class Station extends EntityBase {
    public static final String DEFAULT_NAME = "Unnamed Station";
    public static final int DEFAULT_MAX_PASSENGERS = 50;

    @Setter
    private String name = DEFAULT_NAME;

    @Setter
    private int maxPassengers = DEFAULT_MAX_PASSENGERS;

    @OneToMany(mappedBy = "station")
    @JsonBackReference
    private Set<LineEntry> lineEntries = new HashSet<>();

    @Setter
    @ManyToOne(cascade = {CascadeType.ALL})
    @JsonBackReference
    private Network network;

    /**
     * The connections leading of from this station.
     */
    @OneToMany(mappedBy = "sourceStation")
    @JsonBackReference
    private Set<Connection> sourceConnections = new HashSet<>();

    /**
     * The connections leading to this station.
     */
    @OneToMany(mappedBy = "destinationStation")
    @JsonBackReference
    private Set<Connection> destinationConnections = new HashSet<>();

    @JsonIgnore
    public Optional<Connection> getConnectionTo(Station destination) {
        return this.sourceConnections
                .stream()
                .filter(c -> c.getDestinationStation().equals(destination))
                .findFirst();
    }

    /**
     * Creates a default connection from this station to the given destination and sets up all internal references.
     *
     * @return The connection that was created.
     */
    public Connection connectTo(Station destination) {
        return this.connectTo(destination, c -> c);
    }

    /**
     * Creates a connection from this station to the given destination and sets up all internal references.
     *
     * @param modifyBuilder a function to set properties of the connection that is created.
     * @return The connection that was created.
     */
    public Connection connectTo(Station destination, Function<Connection.Builder, Connection.Builder> modifyBuilder) {
        Connection connection = modifyBuilder.apply(Connection.builder())
                .sourceStation(this)
                .destinationStation(destination)
                .build();

        this.sourceConnections.add(connection);
        destination.destinationConnections.add(connection);
        return connection;
    }

    /**
     * Creates a default connection from this station to the given destination and back and sets up all internal
     * references.
     *
     * @return The connection that was created.
     */
    public Connection.Pair connectToAndFrom(Station destination) {
        return this.connectToAndFrom(destination, c -> c, c -> c);
    }

    /**
     * Creates a pair of connections from this station to the given destination and back and sets up all internal
     * references.
     *
     * @param modifyBuilder a function to set properties of the connections that is created.
     * @return The pair of connections that was created.
     */
    public Connection.Pair connectToAndFrom(Station destination, Function<Connection.Builder, Connection.Builder> modifyBuilder) {
        Connection connectionTo = modifyBuilder.apply(Connection.builder())
                .sourceStation(this)
                .destinationStation(destination)
                .build();

        this.sourceConnections.add(connectionTo);
        destination.destinationConnections.add(connectionTo);

        Connection connectionFrom = modifyBuilder.apply(Connection.builder())
                .sourceStation(destination)
                .destinationStation(this)
                .build();

        this.destinationConnections.add(connectionFrom);
        destination.sourceConnections.add(connectionFrom);
        return new Connection.Pair(connectionTo, connectionFrom);
    }

    /**
     * Creates a pair of connections from this station to the given destination and back and sets up all internal
     * references.
     *
     * @param modifyToBuilder   a function to set properties of the forward connection that is created.
     * @param modifyFromBuilder a function to set properties of the backward connection that is created.
     * @return The pair of connections that was created.
     */
    @JsonIgnore
    public Connection.Pair connectToAndFrom(
            Station destination,
            Function<Connection.Builder, Connection.Builder> modifyToBuilder,
            Function<Connection.Builder, Connection.Builder> modifyFromBuilder
    ) {
        Connection connectionTo = modifyToBuilder.apply(Connection.builder())
                .sourceStation(this)
                .destinationStation(destination)
                .build();

        this.sourceConnections.add(connectionTo);
        destination.destinationConnections.add(connectionTo);

        Connection connectionFrom = modifyFromBuilder.apply(Connection.builder())
                .sourceStation(destination)
                .destinationStation(this)
                .build();

        this.destinationConnections.add(connectionFrom);
        destination.sourceConnections.add(connectionFrom);

        return new Connection.Pair(connectionTo, connectionFrom);
    }

    @Override
    public String toString() {
        return "Station{name='" + name + "'}";
    }
}
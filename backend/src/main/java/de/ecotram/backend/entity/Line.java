package de.ecotram.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Getter
@Entity
@NoArgsConstructor
public final class Line extends EntityBase {
    @Setter
    private String name;

    @OneToMany(mappedBy = "line")
    @JsonManagedReference
    private Set<LineEntry> route = new HashSet<>();

    @JsonIgnore
    public int getTotalLength() {
        AtomicInteger total = new AtomicInteger();
        AtomicReference<Station> priorStation = new AtomicReference<>();

        this.route.forEach(le -> {
            if (priorStation.get() != null)
                le.getStation().getConnectionTo(priorStation.get()).ifPresent(c -> total.addAndGet(c.getLength()));
            priorStation.set(le.getStation());
        });

        return total.get();
    }

    @JsonIgnore
    public Optional<Station> getStation(int order) {
        return this.route.stream()
                .filter(le -> le.getOrderValue() == order)
                .map(LineEntry::getStation)
                .findFirst();
    }

    // very inefficient
    @JsonIgnore
    public Optional<Connection> getConnection(int startOrder) {
        Optional<Station> from = this.route.stream()
                .filter(le -> le.getOrderValue() == startOrder)
                .map(LineEntry::getStation)
                .findFirst();

        Optional<Station> to = this.route.stream()
                .filter(le -> le.getOrderValue() == startOrder + 1)
                .map(LineEntry::getStation)
                .findFirst();

        return from.isPresent() && to.isPresent() ? from.get().getConnectionTo(to.get()) : Optional.empty();
    }
}
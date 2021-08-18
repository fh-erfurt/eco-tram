package de.ecotram.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.ecotram.backend.entity.network.Station;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A named recurring route of stations in a network.
 */
@Getter
@Entity
@NoArgsConstructor
public final class Line extends EntityBase {
	@OneToMany(mappedBy = "line")
	@JsonManagedReference
	private final Set<LineEntry> route = new HashSet<>();
	@Setter
	private String name;

	@JsonIgnore
	public int getTotalLength() {
		AtomicInteger total = new AtomicInteger();
		AtomicReference<Station> priorStation = new AtomicReference<>();

		this.route.forEach(le -> {
			if(priorStation.get() != null)
				le.getStation().getConnectionTo(priorStation.get()).ifPresent(c -> total.addAndGet(c.getLength()));
			priorStation.set(le.getStation());
		});

		return total.get();
	}
}
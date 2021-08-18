package de.ecotram.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.ecotram.backend.entity.network.Station;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * A Line-Station reference, with it's associated order value, the point at which the station appears in the line's
 * route.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LineEntry extends EntityBase {
	@Getter
	@Setter
	int orderValue = 0;

	@Getter
	@Setter
	@ManyToOne
	@JsonBackReference
	Line line;

	@Getter
	@Setter
	@ManyToOne
	@JsonManagedReference
	Station station;
}
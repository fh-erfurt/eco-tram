package de.ecotram.backend.entity;

import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.entity.network.Station;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;

/**
 * A tram for passenger transport, used for simulation.
 */
@Getter
@Entity
public final class PassengerTram extends EntityBase {
	public static final int DEFAULT_WEIGHT = 5000; // in kilogram
	public static final int DEFAULT_MAX_SPEED = 50; // in kilometers per hour
	public static final int DEFAULT_SPEED = 50; // in kilometers per hour
	@Transient
	private final List<LineEntry> route;
	/**
	 * The weight of this tram in kilogram.
	 */
	@Setter
	private int weight = DEFAULT_WEIGHT;
	/**
	 * The maximum speed of this tram in kilometers per hour.
	 */
	@Setter
	private int maxSpeed = DEFAULT_MAX_SPEED;
	@Setter
	private int maxPassengers;
	/**
	 * The current speed of this tram in kilometers per hour.
	 */
	@Setter
	@Transient
	private int speed = DEFAULT_SPEED;
	@Setter
	@Transient
	private int currentPassengers;
	@Transient
	private int currentIndex;

	public PassengerTram() {
		this.route = null;
	}

	public PassengerTram(List<LineEntry> route) {
		this.route = route;
	}

	/**
	 * Advances the tram along it's route in the simulation.
	 *
	 * @return The connection the tram moved across.
	 */
	public Connection nextStation() {
		if(route == null)
			throw new IllegalStateException("Tram cannot advance without route.");

		if(currentIndex + 1 >= route.size())
			currentIndex = 0;

		Station from = route.get(currentIndex).getStation();
		Station to = route.get(++currentIndex).getStation();

		return from.getConnectionTo(to).orElseThrow();
	}
}
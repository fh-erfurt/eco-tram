package de.ecotram.backend.handler.socketEntity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.ecotram.backend.entity.network.Station;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record SocketStation(String hash, String name) {
	public static SocketStation fromStation(Station station) {
		return new SocketStation(Integer.toHexString(station.hashCode()), station.getName());
	}
}
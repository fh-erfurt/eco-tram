package de.ecotram.backend.handler.socketEntity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.LineEntry;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record SocketLine(String name, Set<SocketStation> route) {
	public static SocketLine fromLine(Line line) {
		return new SocketLine(
				line.getName(),
				line.getRoute().stream()
						.sorted(Comparator.comparing(LineEntry::getOrderValue))
						.map(LineEntry::getStation)
						.map(SocketStation::fromStation)
						.collect(Collectors.toSet())
		);
	}
}
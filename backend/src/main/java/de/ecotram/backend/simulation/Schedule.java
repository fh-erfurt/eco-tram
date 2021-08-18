package de.ecotram.backend.simulation;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.network.Network;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Represents a schedule for all lines of a given network.
 */
public final class Schedule {
	@Getter
	private final Network network;

	@Getter
	private final Map<Line, LineSchedule> lineSchedules;

	private Schedule(Builder builder) {
		this.network = builder.network;
		this.lineSchedules = builder.lineSchedules;
	}

	public static Builder forNetwork(Network network) {
		return new Builder(network);
	}

	public Optional<LineSchedule> getSchedule(Line line) {
		return Optional.ofNullable(this.lineSchedules.get(line));
	}

	@Log
	public final static class Builder {
		private final Set<Line> cachedLines;
		private final Network network;
		private final Map<Line, LineSchedule> lineSchedules = new HashMap<>();
		private boolean isBuilt;

		private Builder(Network network) {
			this.network = network;
			this.cachedLines = network.getLines().collect(Collectors.toSet());
		}

		public Builder withLineSchedule(LineSchedule lineSchedule) {
			if(isBuilt)
				throw new IllegalStateException("The builder was already built and can only be used once.");

			if(!cachedLines.contains(lineSchedule.getLine()))
				throw new IllegalStateException("The given line schedule does not correspond to a line for this builders network.");

			this.lineSchedules.put(lineSchedule.getLine(), lineSchedule);
			return this;
		}

		public Schedule build() {
			if(isBuilt)
				throw new IllegalStateException("The builder was already built and can only be used once.");

			// this check may be destructive on the set of lines, which is why this builder can only be used once
			if(cachedLines.retainAll(this.lineSchedules.keySet())) {
				this.log.log(Level.WARNING, "Not all lines have a schedule set up.");
			}

			this.isBuilt = true;
			return new Schedule(this);
		}
	}
}
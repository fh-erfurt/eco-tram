package de.ecotram.backend.simulation.event;

import de.ecotram.backend.event.EventArgs;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Builder
public final class RunnerStoppedArgs extends EventArgs {
	private final Cause cause;
	private final String reason;
	private final Optional<Exception> exception;

	public enum Cause {
		STOP_CALLED,
		FINISHED,
		EXCEPTION,
	}
}
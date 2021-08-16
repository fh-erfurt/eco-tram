package de.ecotram.backend.simulation.event;

import de.ecotram.backend.event.EventArgs;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class RunnerTicksUpdatedArgs extends EventArgs {
    public long currentTicks;
}
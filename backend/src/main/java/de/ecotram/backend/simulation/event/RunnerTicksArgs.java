package de.ecotram.backend.simulation.event;

import de.ecotram.backend.event.EventArgs;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class RunnerTicksArgs extends EventArgs {
    public long currentTicks;
}
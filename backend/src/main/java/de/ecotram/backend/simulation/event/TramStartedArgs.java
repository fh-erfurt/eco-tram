package de.ecotram.backend.simulation.event;

import de.ecotram.backend.entity.Tram;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.event.EventArgs;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class TramStartedArgs extends EventArgs {
    public Tram tram;
    public Connection connection;
}
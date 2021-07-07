package de.ecotram.backend.simulation.event;

import de.ecotram.backend.entity.PassengerTram;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.event.EventArgs;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class TramStoppedArgs extends EventArgs {
    public PassengerTram tram;
    public Connection connection;
}
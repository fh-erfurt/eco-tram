package de.ecotram.backend.simulation.event;

import de.ecotram.backend.entity.PassengerTram;
import de.ecotram.backend.entity.network.Connection;
import de.ecotram.backend.event.EventArgs;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class TramStartedArgs extends EventArgs {
    public PassengerTram passengerTram;
    public Connection connection;
}
package de.ecotram.backend.handler.socketEntity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.ecotram.backend.entity.PassengerTram;
import de.ecotram.backend.entity.network.Station;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record SocketPassengerTram(String hash, int currentIndex, SocketStation sourceStation, SocketStation destinationStation) {

}

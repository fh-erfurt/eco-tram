package de.ecotram.backend.handler.socketEntity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record SocketPassengerTram(
        String hash,
        int currentIndex,
        SocketStation sourceStation,
        SocketStation destinationStation) {
}
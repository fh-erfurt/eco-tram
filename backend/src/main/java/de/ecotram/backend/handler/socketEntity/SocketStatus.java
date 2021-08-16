package de.ecotram.backend.handler.socketEntity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record SocketStatus(boolean running, long ticks) {
}

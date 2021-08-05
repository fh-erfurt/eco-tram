package de.ecotram.backend.handler.socketEntity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.LineEntry;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record SocketStatus(boolean running, long ticks) {}

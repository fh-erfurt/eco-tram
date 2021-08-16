package de.ecotram.backend.handler.socketEntity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.ecotram.backend.entity.PassengerTram;
import de.ecotram.backend.simulation.LineSchedule;

import java.util.HashSet;
import java.util.Set;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record SocketLineSchedule(SocketLine socketLine, Set<SocketLineScheduleEntry> entries) {

    public static SocketLineSchedule fromLineSchedule(LineSchedule lineSchedule) {
        Set<SocketLineScheduleEntry> entries = new HashSet<>();
        lineSchedule.getTrams().forEach((tram, entry) -> entries.add(SocketLineScheduleEntry.fromEntryAndTram(tram, entry)));

        return new SocketLineSchedule(
                SocketLine.fromLine(lineSchedule.getLine()),
                entries
        );
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public record SocketLineScheduleEntry(String hash, int startingTime, int startOrdering, int maxCount) {

        public static SocketLineScheduleEntry fromEntryAndTram(PassengerTram tram, LineSchedule.Entry entry) {
            return new SocketLineScheduleEntry(
                    Integer.toHexString(tram.hashCode()),
                    entry.startingTime(),
                    entry.startOrdering(),
                    entry.maxCount()
            );
        }
    }
}
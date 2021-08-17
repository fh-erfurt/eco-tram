package de.ecotram.backend.simulation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.LineEntry;
import de.ecotram.backend.entity.PassengerTram;
import lombok.Getter;

import java.util.*;

/**
 * Represents a schedule for starting all trams for a given line.
 */
public final class LineSchedule {
    @Getter
    private final Line line;

    @Getter
    private final Map<PassengerTram, Entry> trams = new HashMap<>();

    private LineSchedule(Line line) {
        this.line = line;
    }

    public Optional<Entry> getEntry(PassengerTram passengerTram) {
        return Optional.ofNullable(this.trams.get(passengerTram));
    }

    public static LineSchedule fromWaitingTime(Line line, int waitingTime) {
        // TODO(erik): fix this, currently evaluates to 0, probably incorrect denominator

        //        (distance / 1000)      // m => km
        // ----------------------------- == minNumberOfTrams
        // speed * (maxWaitingTime / 60) // s => h
        // simplified to reduce to one division
        int minimumNumberOfTrams = (int) (((float) line.getTotalLength() * 60) / (PassengerTram.DEFAULT_MAX_SPEED * waitingTime * 1000));

        minimumNumberOfTrams = 10;

        List<LineEntry> route = line.getRoute().stream()
                .sorted(Comparator.comparingInt(LineEntry::getOrderValue))
                .toList();

        LineSchedule schedule = new LineSchedule(line);

        for (int i = 1; i < minimumNumberOfTrams + 1; i++) {
            PassengerTram passengerTram = new PassengerTram(route);
            schedule.trams.put(passengerTram, new Entry(i * waitingTime, 0, -1, passengerTram, line));
        }

        return schedule;
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public record Entry(
            int startingTime,
            int startOrdering,
            int maxCount,
            @JsonIgnore PassengerTram passengerTram,
            @JsonIgnore Line line) {
    }
}
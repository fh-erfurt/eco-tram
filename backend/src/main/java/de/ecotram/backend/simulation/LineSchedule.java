package de.ecotram.backend.simulation;

import de.ecotram.backend.entity.Line;
import de.ecotram.backend.entity.LineEntry;
import de.ecotram.backend.entity.PassengerTram;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class LineSchedule {
    @Getter
    private final Line line;

    @Getter(value = AccessLevel.PACKAGE)
    private final Map<PassengerTram, Entry> trams = new HashMap<>();

    private LineSchedule(Line line) {
        this.line = line;
    }

    public Optional<Entry> getEntry(PassengerTram tram) {
        return Optional.ofNullable(this.trams.get(tram));
    }

    public static LineSchedule fromWaitingTime(Line line, int waitingTime) {
        //        (distance / 1000)      // m => km
        // ----------------------------- == minNumberOfTrams
        // speed * (maxWaitingTime / 60) // s => h
        // simplified to reduce to one division
        int minimumNumberOfTrams = (int) (((float) line.getTotalLength() * 60) / (PassengerTram.DEFAULT_MAX_SPEED * waitingTime * 1000));

        LineSchedule schedule = new LineSchedule(line);
        for (int i = 0; i < minimumNumberOfTrams; i++) {
            PassengerTram tram = new PassengerTram(line.getRoute().stream().sorted(Comparator.comparingInt(LineEntry::getOrderValue)).toList());
            schedule.trams.put(tram, new Entry((i + 1) * waitingTime, 0, -1, tram, line));
        }

        return schedule;
    }

    public record Entry(int startingTime, int startOrdering, int maxCount, PassengerTram tram, Line line) {
    }
}
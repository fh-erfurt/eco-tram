package de.fhe.ai.manager;

import de.fhe.ai.model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A class that actively manages traffic for the current tram network
 */
public class TrafficManager
{
    private static TrafficManager INSTANCE; // Instance of SingletonPattern

    private final Set<Tram> trams = new HashSet<Tram>();
    private final Set<Line> lines = new HashSet<Line>();
    private final Set<TemporaryLine> temporarylines = new HashSet<TemporaryLine>();

    //SingletonPattern
    public static TrafficManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new TrafficManager();
        return INSTANCE;
    }

    private TrafficManager() { }

    public void addLine(Line line) {
        this.lines.add(line);
    }

    public void removeLine(Line line) {
        this.lines.remove(line);
    }

    public void addTemporaryLine(TemporaryLine temporaryline) {
        this.temporarylines.add(temporaryline);
    }

    public void removeTemporaryLine(TemporaryLine temporaryline) {
        this.temporarylines.remove(temporaryline);
    }

    public void addTram(Tram tram) {
        this.trams.add(tram);
    }

    public void removeTram(Tram tram) {
        this.trams.remove(tram);
    }

    public boolean isStationInUse(Station station) { return trams.stream().anyMatch(tram -> tram.getCurrentPosition().equals(station)); }

    public boolean isTramInUse(Tram tram) {
        return tram.isInUse();
    }

    public List<Tram> getTramsInLine(Line line) { return trams.stream().filter(tram -> tram.getPaths().contains(line)).collect(Collectors.toList()); }

    public void assignTram(Tram tram, Collection<Line> lines) {
        for (Line line : lines) {
            tram.addLine(line);
        }
    }

    public void reassignTram(Tram tram, TemporaryLine line) {
        tram.reassign(line);
    }

    public void unassignTram(Tram tram) {
        tram.unassign();
    }
}
package de.fhe.ai.manager;

import de.fhe.ai.model.Line;
import de.fhe.ai.model.Station;
import de.fhe.ai.model.Tram;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrafficManager {

    private final List<Line> lines = new ArrayList<Line>();
    private final List<Tram> trams = new ArrayList<Tram>();
    private static TrafficManager INSTANCE;

    public static TrafficManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new TrafficManager();
        return INSTANCE;
    }

    private TrafficManager() {

    }

    public void addLine(Line line) {
        if (!this.lines.contains(line))
            this.lines.add(line);
    }

    public void removeLine(Line line) {
        this.lines.remove(line);
    }

    public void addTram(Tram tram) {
        if (!this.trams.contains(tram))
            this.trams.add(tram);
    }

    public void removeTram(Tram tram) {
        this.trams.remove(tram);
    }

    public boolean isStationInUse(Station station) {
        return trams.stream().anyMatch(tram -> tram.getCurrentPosition().equals(station));
    }

    public boolean isTramInUse(Tram tram) {
        return tram.getPaths().isEmpty();
    }

    public List<Tram> getTramsInLine(Line line) {
        return trams.stream().filter(tram -> tram.getPaths().contains(line)).collect(Collectors.toList());
    }

    public void assignTram(Tram tram, Line[] lines) {
        for (Line line : lines) {
            tram.addLine(line);
        }
    }

    public void unassignTram(Tram tram) {
        tram.getPaths().clear();
    }
}
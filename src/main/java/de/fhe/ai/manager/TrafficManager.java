package de.fhe.ai.manager;

import de.fhe.ai.model.Station;
import de.fhe.ai.model.Tram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrafficManager {

    /*private List<Line> lines = new ArrayList<Line>();*/
    private List<Tram> trams = new ArrayList<Tram>();

    /*
    public void addLine(Line line) {
        if(!this.lines.contains(line))
            this.lines.add(line);
    }*/

    /*
    public void removeLine(Line line) {
        this.lines.remove(line);
    }*/

    public void addTram(Tram tram) {
        if(!this.trams.contains(tram))
            this.trams.add(tram);
    }

    public void removeTram(Tram tram) {
        this.trams.remove(tram);
    }

    public boolean isStationInUse(Station station) {
        return trams.stream().anyMatch(tram -> tram.getPosition().equals(station));
    }

    public boolean isTramInUse(Tram tram) {
        return tram.getPaths().isEmpty();
    }

    /*
    public List<Tram> getTramsInLine(Line line) {
        return trams.stream().filter(tram -> tram.getPaths().contains(line));
    }*/

    /*
    public void assignTram(Tram tram, Line[] lines) {
        for(Line line : lines) {
            tram.addPath(line);
        }
    }*/

    public void unassignTram(Tram tram) {
        tram.getPaths().clear();
    }

}

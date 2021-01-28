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
public class TrafficManager {
    private final Set<Tram>          trams = new HashSet<Tram>();
    private final Set<Line>          lines = new HashSet<Line>();
    private final Set<TemporaryLine> temporaryLines = new HashSet<TemporaryLine>();

    //#region SingletonPattern
    private static TrafficManager INSTANCE; // Instance of SingletonPattern
    public static TrafficManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new TrafficManager();
        return INSTANCE;
    }

    private TrafficManager() { }
    //#endregion

    //#region Getters & Setters
    /**
     * Attempts to add a line to the lines managed by this traffic manager
     * 
     * @param line the line to add to the current lines
     * 
     * @return {@code true} if the given line was not yet in the set and added successfully; otherwise {@code false}
     */
    public boolean addLine(Line line) { return this.lines.add(line); }

    /**
     * Attempts to remove a line from the lines managed by this traffic manager
     * 
     * @param line the line to add to the current lines
     * 
     * @return {@code true} if the given line was in the set of current lines and removed succesfully; otherwise {@code false}
     */
    public boolean removeLine(Line line) { return this.lines.remove(line); }
    
    /**
     * Attempts to add a temporary line to the temporary lines managed by this traffic manager
     * 
     * @param temporaryline the line to add to the current temporary lines
     * 
     * @return {@code true} if the given temporary line was not yet in the set and added successfully lines; otherwise {@code false}
     */
    public boolean addTemporaryLine(TemporaryLine temporaryline) { return this.temporaryLines.add(temporaryline); }

    /**
     * Attempts to remove a temporary line from the temporary lines managed by this traffic manager
     * 
     * @param temporaryLine the line to add to the current temporary lines
     * 
     * @return {@code true} if the given temporary line was in the set of temporary lines and removed succesfully lines; otherwise {@code false}
     */
    public boolean removeTemporaryLine(TemporaryLine temporaryLine) { return this.temporaryLines.remove(temporaryLine); }
    /**
     * Attempts to add a tram to the trams managed by this traffic manager
     * 
     * @param tram the line to add to the current trams
     * 
     * @return {@code true} if the given tram was not yet in the set and added successfully; otherwise {@code false}
     */
    public boolean addTram(Tram tram) { return this.trams.add(tram); }

    /**
     * Attempts to remove a tram from the trams managed by this traffic manager
     * 
     * @param tram the line to add to the current trams
     * 
     * @return {@code true} if the given tram was in the set of trams and removed succesfully; otherwise {@code false}
     */
    public boolean removeTram(Tram tram) { return this.trams.remove(tram); }
    //#endregion

    public boolean isStationInUse(Station station) {
        return trams.stream().anyMatch(tram -> tram.getCurrentPosition().equals(station));
    }

    public List<Tram> getTramsInLine(Line line) {
        return trams.stream().filter(tram -> tram.getPaths().contains(line)).collect(Collectors.toList());
    }

    /**
     * Attempts to assign a tram to new lines that are added after all it's current lines
     * 
     * @param tram the tram to assign
     * @param lines the lines to assign the tram to
     * 
     * @exception IllegalArgumentException if the lines paths are not directly traversable, that is that moving from destination1 to start2
     */
    public void assignTram(Tram tram, Collection<Line> lines) {
        for (Line line : lines) {
            tram.addLine(line);
        }
    }

    /**
     * Works similar to {@link #assignTram(Tram tram, Collection<Line> lines)} but sends the tram directly to the new line
     * 
     * @param tram the tram to reassign
     * @param line the line to assign the tram to
     * 
     * @exception IllegalArgumentException if the line paths are not directly traversable, that is that moving from destination1 to start2
     */
    public void reassignTram(Tram tram, TemporaryLine line) { tram.reassign(line); }

    /**
     * Stops a tram at the current position without giving it any new lines
     * 
     * @param tram the tram to unassign
     */
    public void unassignTram(Tram tram) { tram.unassign(); }
}
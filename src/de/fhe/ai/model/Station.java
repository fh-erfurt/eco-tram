package de.fhe.ai.model;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private String name;
    private int capacity;
    private List<Track> tracks;
    private List<Connection> connections;


    public Station() {
        this.tracks = new ArrayList<>();
        this.connections = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(String capacity) { this.capacity = capacity; }

    public List<Track> getTracks() { return tracks; }
    public void addTrack(Track track) {
        this.tracks.add(track);
    }
    public void addTracks(List<Track> tracks) {
        for (Track track : tracks) {
            addTrack(track);
        }
    }

    public List<Connection> getConnections() { return connections; }
    public void addConnection(Connection connection) {
        this.connections.add(connection);
    }
    public void addConnections(List<Connection> connections) {
        for (Connection connection : connections) {
            addConnection(connection);
        }
    }
}
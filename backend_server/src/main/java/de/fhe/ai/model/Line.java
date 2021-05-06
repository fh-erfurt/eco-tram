package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;
import de.fhe.ai.manager.TrafficManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a path taken by a {@link Tram}
 */
public class Line extends ModelBase {
    private final String name;
    private final List<Traversable> route = new ArrayList<>();

    /**
     * Initializes a new Line
     *
     * @param id           the internal id of the line
     * @param eventManager the eventManager used to communicate with the TrafficManager, must be non-null
     * @param name         the name of the line, must be non-null, can be empty
     * @param route        the route of the line, must be non-null, contain at least 3 elements and lead from Station to Station
     * @throws IllegalArgumentException if invalid arguments are passed
     */
    public Line(int id, EventManager eventManager, TrafficManager trafficManager, String name, List<Traversable> route) {
        super(id, eventManager, trafficManager);

        if (name == null)
            throw new IllegalArgumentException("TramType of `" + this + "` cannot be null.");
        if (route == null)
            throw new IllegalArgumentException("Route of `" + this + "` cannot be null.");
        if (route.size() < 3)
            throw new IllegalArgumentException("Route of `" + this + "` cannot have less than 3 traversables.");
        if (!(route.get(0) instanceof Station && route.get(route.size() - 1) instanceof Station))
            throw new IllegalArgumentException("Route of `" + this + "` must start and end in a Station.");

        this.name = name;
        this.route.addAll(route);
    }

    public String getName() {
        return name;
    }

    public Station getSourceStation() {
        return (Station) this.route.get(0);
    }

    public Station getDestinationStation() {
        return (Station) this.route.get(this.route.size() - 1);
    }

    public List<Traversable> getRoute() {
        return route;
    }

    /**
     * Checks whether the given tram is allowed to traverse this line completely
     *
     * @param tram the tram to check traversability for
     * @return {@code true} if this tram is allowed to traverse all traversables of this line; otherwise {@code false}
     */
    public boolean isTramAllowed(Tram tram) {
        for (Traversable traversable : this.route)
            if (!traversable.isTramAllowed(tram))
                return false;

        return true;
    }
}
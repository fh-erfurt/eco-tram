package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;

import java.util.ArrayList;

public class Line extends ModelBase
{
    private final String name;
    private final ArrayList<ITraversable> route;

    public Line(int id, EventManager eventManager, String name, ArrayList<ITraversable> route) {
        super(id, eventManager);

        this.name = name; 
        this.route = route; 

    }


    public String getName() {
        return name;
    }

    public ArrayList<ITraversable> getRoute() {
        return route;
    }

    /**
     * Checks the stations in route and if the DestinationStation is inside
     * @return Station
     * otherwise
     * @return null
     */
    public Station getDestinationStation()
    {
        for(int i = route.size() - 1; i >= 0; i--)
        {
            ITraversable traversable = route.get(i);
            if(traversable instanceof Station)
                return (Station) traversable;
        }
        return null; 
    }

    /**
     * Checks whether tram is allowed to pass
     * @param tram
     * @return boolean
     */
    public boolean isTramAllowed(Tram tram)
    {
        for(int i = 0; i < this.route.size(); i++)
        {
            ITraversable traversable = this.route.get(i);
            if(!traversable.isTramAllowed(tram))
                return false; 
        }
        return true; 
    }
}
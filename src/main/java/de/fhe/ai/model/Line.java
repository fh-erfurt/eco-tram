package de.fhe.ai.model;

import de.fhe.ai.manager.EventManager;

import java.util.ArrayList;

public class Line extends ModelBase
{

    private final ArrayList<ITraversable> route; //eingesetzte Bahn
    private final String name; //Name der Linie

    public Line(int id, EventManager eventManager, String name, ArrayList<ITraversable> route)
    {
        super(id, eventManager);
        this.name = name; 
        this.route = route; 

    }
    
    //getter (siehe Fahrplanbeispiel)

    public String getName()
    {
        return this.name;
    }

    public ArrayList<ITraversable> getRoute()
    {
        return this.route; 
    }

    /*Nur um zu sehen, wie setter aussehen würde 
    
    public void setName(String name)
    {   
        this.name = name; 
    }*/

    /**

     * gibt Endstation zurück 
     * @return Station 
     */
    public Station getDestination()
    {
        for(int i = this.route.size() - 1; i >= 0; i--)
        {
            ITraversable traversable = this.route.get(i);
            if(traversable instanceof Station)
                return (Station) traversable;
        }

        return null; 
    }

    /**
     * prüft ob Tram langfahren darf 
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
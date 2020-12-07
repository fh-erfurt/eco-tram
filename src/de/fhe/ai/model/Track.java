package de.fhe.ai.model;

public class Track
{
    
    private Station stationStop; //Endhaltestelle 
    private Station stationStart; //Starthaltestelle
    private ArrayList<Tram> usedTrams; //eingesetzte Bahn 

    public Track(Station stationStop, Station stationStart)
    {
        this.stationStop = stationStop; 
        this.stationStart = stationStart; 

        this.usedTrams = new ArrayList<Tram>(); 
    }

    public Station getStopStation()
    {
        return this.stationStop; 
    }

    public Station getStartStation()
    {
        return this.stationStart; 
    }

    public ArrayList<Tram> getUsedTrams()
    {
        return this.usedTrams; 
    }
    
    /**
     * fügt Tram zur Liste hinzu, wenn noch nicht vorhanden 
     * @param tram
     */
    public void injectTram(Tram tram)
    {
        if(!usedTrams.contains(tram))
        {
            usedTrams.add(tram);
        }
    } 

    /**
     * entfernt Tram aus der Liste, wenn vorhanden
     * @param tram
     */
    public void ejectTram(Tram tram)
    {
        if(usedTrams.contains(tram))
        {
            usedTrams.remove(tram);
        }
    }

}
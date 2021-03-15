package code;

import java.util.Collections;
import java.util.List;

public class Trip {
	
	//Fields
	private String tripID;
    private List<Stop> stopsOnTrip;
    
    /**
     * A Trip consists of a tripID and a list of Stops on this Trip
     */
    public Trip(String tripID, List<Stop> stopsOnTrip){
        this.tripID = tripID;
        this.stopsOnTrip = stopsOnTrip;
    }
    
    //Get methods
    public String getTripID(){return tripID;}
    public List<Stop> getStopsOnTrip(){return Collections.unmodifiableList(stopsOnTrip);}

}

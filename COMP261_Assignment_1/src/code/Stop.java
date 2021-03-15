package code;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stop {
	
	// Fields
    private String stopID;
    private String name;
    private double latitude;
    private double longitude;
    private Location location;
    private boolean highlighted = false;
    private List<Connection> incomingConnections;
    private List<Connection> outgoingConnections;
    
    /**
     * A Stop consists of a stopID, name, latitude and longitude.
     * A Location for the stop is created from latitude and longitude
     */
    public Stop(String stopID, String name, double latitude, double longitude){
        this.stopID = stopID;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        location = Location.newFromLatLon(latitude, longitude);
        incomingConnections = new ArrayList<Connection>();
        outgoingConnections = new ArrayList<Connection>();
    }
    
    /**
     * Adds incoming Connection
     */
    public void addIncomingConnection(Connection incoming){
        incomingConnections.add(incoming);
    }
    
    /**
     * Adds outgoing Connection
     */
    public void addOutgoingConnection(Connection outgoing){
        outgoingConnections.add(outgoing);
    }
    
    /**
     * Highlights the Stop
     */
    public void setHighlighted() {
    	if (!highlighted) {
    		highlighted = true;
    	}
    	else {
    		highlighted = false;
    	}
    }

    // Get methods
    public String getStopID(){return stopID;}
    public String getStopName(){return name;}
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
	public Location getLocation() {return location;}
	public boolean isHighlighted() {return highlighted;}
    public List<Connection> getIncomingConnections(){return Collections.unmodifiableList(incomingConnections);}
    public List<Connection> getOutgoingConnections(){return Collections.unmodifiableList(outgoingConnections);}

}

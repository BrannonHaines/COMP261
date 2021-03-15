package code;

public class Connection {
	
	//Fields
	private String tripID;
	private Stop fromStop;
	private Stop toStop;
	private boolean highlighted = false;
	
	/**
	 * A Connection consists of a tripID for the trip they belong to, fromStop, toStop.
	 * fromStop and toStop essentially represent where the Connection is.
	 */
    public Connection(String tripID, Stop fromStop, Stop toStop) {
        this.tripID = tripID;
        this.fromStop = fromStop;
        this.toStop = toStop;
    }
    
    /**
     * Highlights the Connection
     */
    public void setHighlighted() {
    	if (highlighted) {
    		highlighted = false;
    	}
    	else {
    		highlighted = true;
    	}
    }
    
    // Get methods
    public String getTripID() {return tripID;}
    public Stop getFromStop() {return fromStop;}
    public Stop getToStop() {return toStop;}
    public boolean isHighlighted() {return highlighted;}
}

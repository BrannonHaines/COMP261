package code;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class JourneyPlannerNetwork extends GUI{
	
	// Fields
	private Map<String, Stop> stops = new HashMap<String, Stop>();
    private Map<String, Trip> trips = new HashMap<String, Trip>();
    private List<Connection> connections = new ArrayList<Connection>();
    private Location origin;
    private Dimension windowSize = getDrawingAreaDimension();
    private double scale;
    
    public static final double ZOOM_FACTOR = 1.1;
    public double left = Double.POSITIVE_INFINITY;
	public double top = Double.NEGATIVE_INFINITY;
	public double right = Double.NEGATIVE_INFINITY;
	public double bottom = Double.POSITIVE_INFINITY;
    
    
    public static void main(String[] args){
       new JourneyPlannerNetwork();
    }
    
    /**
     * Calculates the scale and origin for displaying the graph.
     * Each stops x and y position is compared against the predefined values of left, top, right and bottom.
     * If the stop x/y positions are greater than/less than each value respectively, the value is assigned that x/y position. 
     * Once the comparing is done, the origin for displaying is set to Location(left, top).
     * The scale is calculated as windowSize /(maxLocation - minLocation).
     */
    public void getScaleAndOrigin() {
    	for (Stop stop : stops.values()) {
    		if (stop.getLocation().x < left) {
    			left = stop.getLocation().x;
    		}
    		if (stop.getLocation().x > right) {
    			right = stop.getLocation().x;
    		}
    		if (stop.getLocation().y > top) {
    			top = stop.getLocation().y;
    		}
    		if (stop.getLocation().y < bottom) {
    			bottom = stop.getLocation().y;
    		}
    	}
    	origin = new Location(left, top);
    	scale = (windowSize.getWidth()/(right - left) + windowSize.getHeight()/(top - bottom));
    }
    
    
    /**
     * Draws Stops as small blue rectangles and Connections as grey lines between two stops.
     * For each of the Stops, a point is created and is drawn as a blue rectangle at point.x, point.y.
     * If the Stop is highlighted, the Stop is drawn as a yellow rectangle.
     * For each of the Connections, a point is created separately for each of the Stops stored in the Connection.
     * The Connection is drawn as a grey line from one Stops point to the other Stops point.
     * If the Connection is highlighted, then the Connection is drawn as a red line.
     */
    @Override
    protected void redraw(Graphics g) {
    	for (Stop stop : stops.values()) {
    		Point stopPoint = stop.getLocation().asPoint(origin, scale);
    		if(stop.isHighlighted()) {
    			g.setColor(Color.YELLOW);
    			g.fillRect(stopPoint.x-1, stopPoint.y-1, 5, 5);  // the '-1' helps centre the rect on the connection line
    			stop.setHighlighted();
    		}
    		else {
    		g.setColor(Color.BLUE);
    		g.fillRect(stopPoint.x-1, stopPoint.y-1, 5, 5);  // the '-1' helps centre the rect on the connection line
    		}
    	}
    	for (Connection connection : connections) {
    		Location l1 = connection.getFromStop().getLocation();
    		Location l2 = connection.getToStop().getLocation();
    		Point p1 = l1.asPoint(origin, scale);
    		Point p2 = l2.asPoint(origin, scale);
    		if (connection.isHighlighted()) {
    			g.setColor(Color.RED);
        		g.drawLine(p1.x, p1.y, p2.x, p2.y);
        		connection.setHighlighted();
    		}
    		else {
    			g.setColor(Color.GRAY);
        		g.drawLine(p1.x, p1.y, p2.x, p2.y);
    		}
    		
    	}
    }
    
    
    /**
     * Allows the user to select a Stop with the mouse and the name of the Stop and all the Trips in that Stop are printed.
     * The clickedLocation is obtained from the parameter e and the clickedStop is initialized as null.
     * For each stop, if the clickedLocation is close to the stop, stop is assigned to clickedStop.
     * The distance between the clickedLocation and the current Stop is assigned to distToClickedStop.
     * By the end of the loop, the selected Stop will have been obtained and the clickedStop is the highlighted.
     * For every Trip that contains the clickedStop, their tripID is added to the tripIDs variable.
     * The text output area the prints the clickedStop name and the tripIDs.
     */
    @Override
    protected void onClick(MouseEvent e) {
    	Stop clickedStop = null;
    	Location clickedLocation = Location.newFromPoint(e.getPoint(), origin, scale);
    	double distToClickedStop = Double.POSITIVE_INFINITY;
    	for (Stop stop : stops.values()) {
    		if (clickedLocation.isClose(stop.getLocation(), distToClickedStop)) {
    			distToClickedStop = clickedLocation.distance(stop.getLocation());
    			clickedStop = stop;
    		}
    	}
    	String tripIDs =""; 
    	clickedStop.setHighlighted();
    	for (Trip trip : trips.values()) {
    		if (trip.getStopsOnTrip().contains(clickedStop)) {
    			tripIDs += trip.getTripID()+", ";
    		}
    	}
    	getTextOutputArea().setText("Stop Name : "+clickedStop.getStopName()+"\nTrips : "+tripIDs);
    	redraw();
    }
    
    
    /**
     *  Allows the user to search for a Stop in the search box, Stop and all the Connections for that Stop are highlighted.
     *  Gets the stopName from the search box and searches for the Stop in the collection of Stops, Once found searchedStop is highlighted.
     *  For each Connection, if the Connection contains the searchedStop then that Connection is highlighted.
     *  Prints the Stop name and the Trips connected to the Stop in the text output area
     */
    @Override
    protected void onSearch() {
    	Stop searchedStop = null;
    	String stopName = getSearchBox().getText();
    	for (Stop stop : stops.values()) {
    		if(stop.getStopName().equals(stopName)){
    			searchedStop = stop;
    		}
    	}
    	for (Connection connection : connections) {
    		if (connection.getFromStop().equals(searchedStop) || connection.getToStop().equals(searchedStop)) {
    			connection.setHighlighted();
    		}
    	}
    	searchedStop.setHighlighted();
    	String tripIDs = "";
    	for (Trip trip : trips.values()) {
    		if (trip.getStopsOnTrip().contains(searchedStop)) {
    			tripIDs += trip.getTripID()+", ";
    		}
    	}
    	getTextOutputArea().setText("Stop Name : "+searchedStop.getStopName()+"\nTrips : "+tripIDs);
    	redraw();
    }
    
    
    /**
     * Allows the user to navigate the displayed graph by zooming or panning in different directions.
     * If the user decides to pan in a direction, the origin x/y coordinate is incremented/decremented by 0.6 in that direction.
     * If the user decides to zoom in or out, the origin is incremented by dx and dy and scale is adjusted by 10%. 
     */
    @Override
    protected void onMove(Move m) {
    	Location topLeft = new Location(left, top);
    	Location topRight = new Location(right, top);
    	Location botLeft = new Location(left, bottom);
    	double width = topRight.x - topLeft.x;
    	double height = botLeft.y - topLeft.y;
    	if (m.equals(Move.NORTH)) {
    		origin = origin.moveBy(0, 0.6);
    		redraw();
    	}
    	else if (m.equals(Move.SOUTH)) {
    		origin = origin.moveBy(0, -0.6);
    		redraw();
    	}
    	else if (m.equals(Move.EAST)) {
    		origin = origin.moveBy(0.6, 0);
    		redraw();
    	}
    	else if (m.equals(Move.WEST)) {
    		origin = origin.moveBy(-0.6, 0);
    		redraw();
    	}
    	else if (m.equals(Move.ZOOM_IN)) {
    		double dx = (width-(width/ZOOM_FACTOR))/2;
    		double dy = (height-(height/ZOOM_FACTOR))/2;
    		scale *= ZOOM_FACTOR;
    		origin = origin.moveBy(dx, dy);
    		redraw();
    	}
    	else if (m.equals(Move.ZOOM_OUT)) {
    		double dx = (width-(width*ZOOM_FACTOR))/2;
    		double dy = (height-(height*ZOOM_FACTOR))/2;
    		scale /= ZOOM_FACTOR;
    		origin = origin.moveBy(dx, dy);
    		redraw();
    	}
    }
    
    
    /**
     * Creates Stops, Connections and Trips from stopFile and tripFile using BufferedReader.
     * When each line of the file is read, the line is split up into an String Array with each entry being separated by the tab input.
     * Then each object is created from each line of the file.
     * When each object is created, they are added to their respective collections.
     */
    @Override
    protected void onLoad(File stopFile, File tripFile) {
        try{
        	 BufferedReader brStops = new BufferedReader(new FileReader(stopFile));
        	 brStops.readLine();  // throws away title line
        	 String stopLines;
             while ((stopLines = brStops.readLine()) != null) {
            	 String[] stopLine = stopLines.split("\t");
            	 String stopID = stopLine[0];
            	 String name = stopLine[1];
            	 double latitude = Double.parseDouble(stopLine[2]);
            	 double longitude = Double.parseDouble(stopLine[3]);
            	 stops.put(stopID, new Stop(stopID, name, latitude, longitude));
             }
             BufferedReader brTrips = new BufferedReader(new FileReader(tripFile));
             brTrips.readLine();  // throws away title line
             String tripLines;
             while ((tripLines = brTrips.readLine()) != null) {
            	 String[] tripLine = tripLines.split("\t");
            	 String tripID = tripLine[0];
            	 List<Stop> stopsOnTrip = new ArrayList<Stop>();
            	 stopsOnTrip.add(stops.get(tripLine[1]));
            	 for (int i = 2; i < tripLine.length; i++) {
            		 stopsOnTrip.add(stops.get(tripLine[i]));
            		 Stop fromStop = stops.get(tripLine[i-1]);
            		 Stop connectingStop = stops.get(tripLine[i]);
            		 Stop toStop = null;
            		 if (i+1 != tripLine.length) {  // checks i+1 isn't out of bounds in the Array
            			  toStop = stops.get(tripLine[i+1]);
            		 }
            		 Connection incoming = new Connection(tripID, fromStop, connectingStop);
            		 connections.add(incoming);
            		 connectingStop.addIncomingConnection(incoming);
            		 if (toStop != null) {  // only makes an outgoing connection if toStop isn't null
            			 Connection outgoing = new Connection(tripID, connectingStop, toStop);
            			 connections.add(outgoing);
            			 connectingStop.addOutgoingConnection(outgoing);
            		 }
            	 }
            	 trips.put(tripID, new Trip(tripID, stopsOnTrip));
             }
             getScaleAndOrigin();
        }catch (IOException e){System.out.println("Loading of data failed "+e);}
    }
}

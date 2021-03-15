package code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * This is the main class for the mapping program. It extends the GUI abstract
 * class and implements all the methods necessary, as well as having a main
 * function.
 * 
 * @author tony
 */
public class Mapper extends GUI {
	public static final Color NODE_COLOUR = new Color(77, 113, 255);
	public static final Color SEGMENT_COLOUR = new Color(130, 130, 130);
	public static final Color HIGHLIGHT_COLOUR = new Color(255, 219, 77);

	// these two constants define the size of the node squares at different zoom
	// levels; the equation used is node size = NODE_INTERCEPT + NODE_GRADIENT *
	// log(scale)
	public static final int NODE_INTERCEPT = 1;
	public static final double NODE_GRADIENT = 0.8;

	// defines how much you move per button press, and is dependent on scale.
	public static final double MOVE_AMOUNT = 100;
	// defines how much you zoom in/out per button press, and the maximum and
	// minimum zoom levels.
	public static final double ZOOM_FACTOR = 1.3;
	public static final double MIN_ZOOM = 1, MAX_ZOOM = 200;

	// how far away from a node you can click before it isn't counted.
	public static final double MAX_CLICKED_DISTANCE = 0.15;

	// these two define the 'view' of the program, ie. where you're looking and
	// how zoomed in you are.
	private Location origin;
	private double scale;
	private double totalPathCost;  // total path cost in distance

	// our data structures.
	private Graph graph;
	private List<Node> pathNodes = new ArrayList<Node>();  // nodes for A* Search
	private Collection<Segment> segments = new ArrayList<Segment>();  // segments for highlighted

	@Override
	protected void redraw(Graphics g) {
		if (graph != null)
			graph.draw(g, getDrawingAreaDimension(), origin, scale);
	}

	@Override
	protected void onClick(MouseEvent e) {
		Location clicked = Location.newFromPoint(e.getPoint(), origin, scale);
		// find the closest node.
		double bestDist = Double.MAX_VALUE;
		Node closest = null;

		for (Node node : graph.nodes.values()) {
			double distance = clicked.distance(node.location);
			if (distance < bestDist) {
				bestDist = distance;
				closest = node;
			}
		}

		// if it's close enough, highlight it and show some information.
		if (clicked.distance(closest.location) < MAX_CLICKED_DISTANCE) {
			getTextOutputArea().setText(closest.toString());
		}
		
		// Adds the first clicked node and highlights it
		if (pathNodes.size() == 0) {
			pathNodes.add(closest);
			graph.highlightedNodeStart = closest;
			getTextOutputArea().setText("Start : "+closest.toString()+"\n\nClick where your route ends");
		}
		
		// Adds second node and begins the A* Search
		// Once A* search is complete, we get the road name and segment lengths from segments
		// and make an output detailing the path
		else if (pathNodes.size() == 1) {
			pathNodes.add(closest);
			findShortestPath(pathNodes.get(0), pathNodes.get(1));
			graph.highlightedNodeEnd = closest;
			graph.highlightedSegments = segments;
			redraw();
			String output = "";
			for (Segment segment : segments) {
				totalPathCost += segment.length;
				output += segment.road.name+": "+Math.round(segment.length*100.0)/100.0+"km\n";
			}
			output += "Total distance = "+Math.round(totalPathCost*100.0)/100.0+"km";
			getTextOutputArea().setText(output);
		}
		// resets pathNodes back to empty, adds the first clicked node and re-initializes the highlighted fields 
		else if (pathNodes.size() >= 2){
			pathNodes.clear();
			pathNodes.add(closest);
			graph.highlightedNodeStart = closest;
			graph.highlightedNodeEnd = null;
			totalPathCost = 0;
		}
	}

	@Override
	protected void onSearch() {
	}

	@Override
	protected void onMove(Move m) {
		if (m == GUI.Move.NORTH) {
			origin = origin.moveBy(0, MOVE_AMOUNT / scale);
		} else if (m == GUI.Move.SOUTH) {
			origin = origin.moveBy(0, -MOVE_AMOUNT / scale);
		} else if (m == GUI.Move.EAST) {
			origin = origin.moveBy(MOVE_AMOUNT / scale, 0);
		} else if (m == GUI.Move.WEST) {
			origin = origin.moveBy(-MOVE_AMOUNT / scale, 0);
		} else if (m == GUI.Move.ZOOM_IN) {
			if (scale < MAX_ZOOM) {
				// yes, this does allow you to go slightly over/under the
				// max/min scale, but it means that we always zoom exactly to
				// the centre.
				scaleOrigin(true);
				scale *= ZOOM_FACTOR;
			}
		} else if (m == GUI.Move.ZOOM_OUT) {
			if (scale > MIN_ZOOM) {
				scaleOrigin(false);
				scale /= ZOOM_FACTOR;
			}
		}
	}

	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons) {
		graph = new Graph(nodes, roads, segments, polygons);
		origin = new Location(-250, 250); // close enough
		scale = 1;
		getTextOutputArea().setText("Click where your route starts");
	}

	/**
	 * This method does the nasty logic of making sure we always zoom into/out
	 * of the centre of the screen. It assumes that scale has just been updated
	 * to be either scale * ZOOM_FACTOR (zooming in) or scale / ZOOM_FACTOR
	 * (zooming out). The passed boolean should correspond to this, ie. be true
	 * if the scale was just increased.
	 */
	private void scaleOrigin(boolean zoomIn) {
		Dimension area = getDrawingAreaDimension();
		double zoom = zoomIn ? 1 / ZOOM_FACTOR : ZOOM_FACTOR;

		int dx = (int) ((area.width - (area.width * zoom)) / 2);
		int dy = (int) ((area.height - (area.height * zoom)) / 2);

		origin = Location.newFromPoint(new Point(dx, dy), origin, scale);
	}
	
	/**
	 * Finds the shortest path between a start node and a goal node using A* Search.
	 * The shortest path is determined by the length of the segments connecting to each node.
	 * Creates a starting Fringe object and adds it to the PriorityQueue of fringes.
	 * The PriorityQueue prioritizes by the totalCost of a Fringe (or it's f cost).
	 * While fringes is not empty, the top fringe is polled and expanded.
	 * If the currentNode in the fringe is not visited, then we visit it and the parent node of currentNode is assigned the prevNode in the fringe.
	 * If the currentNode is the goal node, then we have found our path and we get the segments along the path.
	 * If we haven't found the goal node after visiting the currentNode, then we get the outgoing neighbours of the currentNode and 
	 * create a new Fringe object for each neighbour and add it to the queue of fringes if the neighbour is not visited
	 */
	public void findShortestPath(Node startNode, Node goalNode) {
		// Initializes all fields
		for (Node node : graph.nodes.values()) {
			node.visited = false;
			node.previous = null;
			node.heuristic = Double.POSITIVE_INFINITY;
		}
		
		Fringe firstFringe = new Fringe(startNode, null, 0, startNode.location.distance(goalNode.location));
		PriorityQueue<Fringe> fringes = new PriorityQueue<Fringe>();
		fringes.offer(firstFringe);
		
		while(!fringes.isEmpty()) {
			Fringe fringe = fringes.poll();
			Node currentNode = fringe.getCurrentNode();
			Node prevNode = fringe.getPreviousNode();
			double currentCost = fringe.getCurrentCost();
			if (!currentNode.visited) {
				currentNode.visited = true;
			    currentNode.previous = prevNode;
				if (currentNode == goalNode) {
					getSegments(fringe);
					break;
				}
				for (Node neigh : currentNode.getNeighbours()) {
					if (!neigh.visited) {
						double neighCost = currentCost + currentNode.getSegmentBetweenNodes(neigh).length;  // neigh g cost
						double totalEstNeighCost = neighCost + neigh.location.distance(goalNode.location);  // neigh f cost
						Fringe newFringe = new Fringe(neigh, currentNode, neighCost, totalEstNeighCost);
						fringes.offer(newFringe);
					}
				}
			}
		}
	}
	
	/**
	 * Returns the segments of a path.
	 * Gets the currentNode and prevNode of the given fringe and while the prevNode is not null,
	 * gets the segment between the currentNode and prevNode and adds it to a list of segments.
	 * Then assigns the prevNode to the currentNode and the parent node stored in prevNode is assigned to prevNode
	 * Once prevNode reaches null, we have reached the start of the path and the segment list is returned to the field of segments
	 */
	public Collection<Segment> getSegments(Fringe fringe){
		segments = new ArrayList<Segment>();
		Node currentNode = fringe.getCurrentNode();
		Node prevNode = fringe.getPreviousNode();
		while (prevNode != null) {
			segments.add(prevNode.getSegmentBetweenNodes(currentNode));
			Node swap = prevNode;
			prevNode = prevNode.previous;
			currentNode = swap;
		}
		return segments;
	}

	public static void main(String[] args) {
		new Mapper();
	}
}

// code for COMP261 assignments
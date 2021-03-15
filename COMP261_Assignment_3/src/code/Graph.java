package code;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * This represents the data structure storing all the roads, nodes, and
 * segments, as well as some information on which nodes and segments should be
 * highlighted.
 * 
 * @author tony
 */
public class Graph {
	// map node IDs to Nodes.
	static Map<Integer, Node> nodes = new HashMap<>();
	// map road IDs to Roads.
	Map<Integer, Road> roads;
	// just some collection of Segments.
	static Collection<Segment> segments;

	Collection<Node> highlightedNodes = new ArrayList<Node>();
	Collection<Road> highlightedRoads = new HashSet<>();
	Collection<Segment> highlightedSegments = new HashSet<Segment>();

	public Graph(File nodes, File roads, File segments, File polygons) {
		this.nodes = Parser.parseNodes(nodes, this);
		this.roads = Parser.parseRoads(roads, this);
		this.segments = Parser.parseSegments(segments, this);
	}

	public void draw(Graphics g, Dimension screen, Location origin, double scale) {
		// a compatibility wart on swing is that it has to give out Graphics
		// objects, but Graphics2D objects are nicer to work with. Luckily
		// they're a subclass, and swing always gives them out anyway, so we can
		// just do this.
		Graphics2D g2 = (Graphics2D) g;

		// draw all the segments.
		g2.setColor(Mapper.SEGMENT_COLOUR);
		for (Segment s : segments)
			s.draw(g2, origin, scale);

		// draw the segments of all highlighted roads.
		g2.setColor(Mapper.HIGHLIGHT_COLOUR);
		g2.setStroke(new BasicStroke(3));
		for (Road road : highlightedRoads) {
			for (Segment seg : road.components) {
				seg.draw(g2, origin, scale);
			}
		}

		// draw all the nodes.
		g2.setColor(Mapper.NODE_COLOUR);
		for (Node n : nodes.values())
			n.draw(g2, screen, origin, scale);
		
		for (Segment seg : highlightedSegments) {
			if (seg != null) {
				g2.setColor(Color.GREEN);
				seg.draw(g2, origin, scale);
			}
		}
		
		// draw the highlighted node, if it exists.
			for (Node highlightedNode : highlightedNodes) {
				if (highlightedNode != null) {
					g2.setColor(Color.RED);
					highlightedNode.draw(g2, screen, origin, scale);
				}
			}
	}

	public void setHighlightedNodes(Collection<Node> nodes) {
		this.highlightedNodes = nodes;
	}
	
	public void setHighlightedSegments(Collection<Segment> segments) {
		this.highlightedSegments = segments;
		
	}

	public void setHighlight(Collection<Road> roads) {
		this.highlightedRoads = roads;
	}
	
	public Map<Integer, Node> getNodes() {
		return nodes;
	}
}

// code for COMP261 assignments
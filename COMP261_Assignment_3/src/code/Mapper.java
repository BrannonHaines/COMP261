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
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

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

	// our data structures.
	private static Graph graph;
	private static Set<Node> artPts = new HashSet<Node>();  // set of nodes that are articulation points
	private static Set<Node> forest = new HashSet<Node>();  // 

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
		
	}
	
	/**
	 * Finds articulation points for all nodes on the graph
	 * 
	 * @author Brannon Haines
	 */
	public static void findArtPts() {
		// Intializes a new set of articulation points and sets each nodes depth and reachBack to infinity
		artPts = new HashSet<Node>();
		int numSubTrees = 0;
		graph.setHighlightedSegments(new HashSet<Segment>());
		for (Node node : graph.nodes.values()) {
			node.depth = Integer.MAX_VALUE;
			node.reachBack = Integer.MAX_VALUE;
		}
		
		// if a nodes depth is infinity, then it has not been visited and is set as the root with a depth of 0
		for (Node node : graph.nodes.values()) {
			if (node.depth == Integer.MAX_VALUE) {
				Node root = node;
				root.depth = 0;
				numSubTrees = 0;
				
				// if a nodes neighbour has not been visited, then we search in the neighbour
				for (Node neighbour : root.getAdjacentNeighbours()) {
					if (neighbour.depth == Integer.MAX_VALUE) {
						findNeighArtPts(neighbour, 1, root);
						numSubTrees++;
					}
					
					// if by the end of the search the number of sub trees is greater than 1, then the root is an articulation point
					if (numSubTrees > 1) {
						artPts.add(root);
					}
				}
			}
		}
		graph.setHighlightedNodes(artPts);
		redraw();
		getTextOutputArea().setText("Articulation Points : "+artPts.size());
	}
	
	/**
	 * Finds Articulation Points using the iterative AP algorithm.
	 * 
	 * @author Brannon Haines
	 */
	public static void findNeighArtPts(Node node, int depth, Node parent) {
		// Intializes a stack and adds the first IterNode
		Stack<IterNode> stack = new Stack<IterNode>();
		stack.push(new IterNode(node, depth, parent));
		
		// While the stack is not empty, we look at the first IterNode and expand it
		while (!stack.isEmpty()) {
			IterNode current = stack.peek();
			Node currentNode = current.getNode();
			int currentDepth = current.getDepth();
			Node parentNode = current.getParent();
			
			// If the node is not visited, then we visit it and set it's depth and reachBack to the current depth
			if (currentNode.depth == Integer.MAX_VALUE) {
				currentNode.depth = currentDepth;
				currentNode.reachBack = currentDepth;
				
				// For each adjacent node (except the parent) to this node, we add each node as the current nodes children
				for (Node neighbour : currentNode.getAdjacentNeighbours()) {
					if (!neighbour.equals(parentNode)) {
						currentNode.children.add(neighbour);
					}
				}
			}
			
			// If the node has children, we get the a child and remove it
			else if (!currentNode.children.isEmpty()) {
				Node child = currentNode.children.poll();
				
				// If they have been visited before, then we have found an alternative path and the reachBack is set to the minimum of the childs depth or the current nodes reachBack
				// If not, it is added to the stack as a IterNode
				if (child.depth < Integer.MAX_VALUE) {
					currentNode.reachBack = Math.min(child.depth, currentNode.reachBack);
				}
				
				else {
					stack.push(new IterNode(child, currentNode.depth+1, currentNode));
				}
			}
			else {
				
				// If the current node is not the first node passed in the parameter, then the reachBack of the parent is set
				// to the minimum of the it's current reachBack or the current nodes reachBack 
				if (!currentNode.equals(node)) {
					parentNode.reachBack = Math.min(currentNode.reachBack, parentNode.reachBack);
					
					// If the reachBack of the current node is greater than the depth of the parent, then the parent is an articulation point
					if (currentNode.reachBack >= parentNode.depth) {
						artPts.add(parentNode);
					}
				}
				stack.remove(current);
			}
		}
	}
	
	/**
	 * Finds a minimum spanning tree using Kruskal's algorithm
	 * 
	 * @author Brannon Haines
	 */
	public static void findMSTKruskal() {
		// Intializes all the fields
		forest = new HashSet<Node>();
		Queue<Segment> edges = new PriorityQueue<Segment>();
		Set<Segment> tree = new HashSet<Segment>();
		
		// Makes a set for each node and adds all segments to the edges queue
		for (Node node : graph.nodes.values()) {
			Node newNode = makeSet(node);
			forest.add(newNode);
		}
		for (Segment seg : graph.segments) {
			edges.offer(seg);
		}
		
		// While edges is not empty, it gets and expands the segment from edges with minimum length
		while(!edges.isEmpty()) {
			Segment seg = edges.poll();
			Node n1 = seg.start;
			Node n2 = seg.end;
			Node n1Root = findRoot(n1);
			Node n2Root = findRoot(n2);
			
			// If n1's root is not equal to n2's root, then this means they're not in the same tree and thus merges the two trees and adds the segment to the tree
			if (!n1Root.equals(n2Root)) {
				mergeTrees(n1, n2);
				tree.add(seg);
			}
		}
		
		// Passes the tree and nodes for display
		graph.setHighlightedSegments(tree);
		graph.setHighlightedNodes(graph.nodes.values());
		getTextOutputArea().setText("MST : "+forest.size());
		redraw();
	}
	
	/**
	 * Makes a set for the forest with the node as it's parent and depth 0.
	 * 
	 * @author Brannon Haines
	 * 
	 * @return A Node with it's parent as itself
	 */
	public static Node makeSet(Node node) {
		node.parent = node;
		node.depth = 0;
		return node;
	}
	
	/**
	 * Finds the root of a node. If the node itself is it's parent then it's the root and is returned.
	 * If not, then the root is recursively searched for through the nodes parent.
	 * 
	 * @author Brannon Haines
	 * 
	 * @return Root of the given Node
	 */
	public static Node findRoot(Node node) {
		Node root = null;
		if (node.parent == node) {
			return node;
		}
		else {
			root = findRoot(node.parent);
			return root;
		}
	}
	
	/**
	 * Merges two trees together. If there's a tree that's bigger than the other, then the smaller tree is merged into the bigger tree.
	 * 
	 * @author Brannon Haines
	 * 	
	 */
	public static void mergeTrees(Node n1, Node n2) {
		// Gets the roots of n1 and n2 and if the roots are equal, then they are in the same tree and we do nothing 
		Node n1Root = findRoot(n1);
		Node n2Root = findRoot(n2);
		if (n1Root.equals(n2Root)) {
			return;
		}
		
		else {
			
			// if the depth of n1 is less than the depth of n2, then n1 is the shorter tree and is merged into n2 and is removed from the forest
			if (n1Root.depth < n2Root.depth) {
				n1Root.parent = n2Root;
				forest.remove(n1Root);
			}
			
			// if we're here then n2 is the shorter tree and is merged into n1 and is removed from the forest
			else {
				n2Root.parent = n1Root;
				forest.remove(n2Root);
				if (n1Root.depth == n2Root.depth) {
					n1Root.depth++;
				}
			}
		}
	}
	
	@Override
	protected void onSearch() {
		// Does nothing
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

	public static void main(String[] args) {
		new Mapper();
	}
}

// code for COMP261 assignments
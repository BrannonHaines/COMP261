package code;

public class Fringe implements Comparable<Fringe>{
	
	private Node currentNode;
	private Node previousNode;
	private double currentCost;
	private double totalEstCost;
	
	/**
	 * A Fringe object helps with representing the state of the path while trying to find the shortest path between two nodes
	 * A Fringe consists of a current node and the node before the current node
	 * The current cost is the cost of the path so far and the total is the current cost plus an estimate of how far is left to go
	 */
	public Fringe(Node currentNode, Node previousNode, double currentCost, double totalEstCost) {
		this.currentNode = currentNode;
		this.previousNode = previousNode;
		this.currentCost = currentCost;
		this.totalEstCost = totalEstCost;
		
	}

	/**
	 * Compares this totalEstCost with the totalEstCost of another Fringe
	 */
	public int compareTo(Fringe otherFringe) {
		if (this.totalEstCost < otherFringe.totalEstCost) { return -1;}
		else if (this.totalEstCost > otherFringe.totalEstCost) { return 1;}
		else { return 0;}
	}
	
	
	// Getters
	
	public Node getCurrentNode() {return currentNode;}
	public Node getPreviousNode() {return previousNode;}
	public double getCurrentCost() {return currentCost;}
	public double getTotalEstCost() {return totalEstCost;}
}

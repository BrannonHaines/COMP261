package code.nodes.operators;

import code.nodes.ExprNode;
import code.src.Robot;

public class AdditionNode implements ExprNode{
	
	ExprNode node1;
	ExprNode node2;
	
	/**
	 * Represents the Addition operator
	 * @author Brannon Haines
	 *
	 */
	public AdditionNode(ExprNode node1, ExprNode node2) {
		this.node1 = node1;
		this.node2 = node2;
	}

	@Override
	public int evaluate(Robot robot) {
		return node1.evaluate(robot) + node2.evaluate(robot);
	}
	
	
	public String toString() {
		return "Add("+node1.toString()+", "+node2.toString()+")";
	}
}
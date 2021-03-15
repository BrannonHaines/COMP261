package code.nodes.operators;

import code.nodes.ExprNode;
import code.src.Robot;

public class MultiplicationNode implements ExprNode{
	
	ExprNode node1;
	ExprNode node2;
	
	/**
	 * Represents the Multiplication operator
	 * @author Brannon Haines
	 *
	 */
	public MultiplicationNode(ExprNode node1, ExprNode node2) {
		this.node1 = node1;
		this.node2 = node2;
	}

	@Override
	public int evaluate(Robot robot) {
		return node1.evaluate(robot)*node2.evaluate(robot);
	}
	
	public String toString() {
		return "Multiply("+node1.toString()+", "+node2.toString()+")";
	}

}


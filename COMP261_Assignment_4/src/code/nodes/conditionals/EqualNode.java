package code.nodes.conditionals;

import code.nodes.ExprNode;
import code.src.Robot;

public class EqualNode implements ConditionalNode{
	
	ExprNode node1;
	ExprNode node2;
	
	/**
	 * Represents the "Equal" conditional
	 * @author Brannon Haines
	 *
	 */
	public EqualNode(ExprNode node1, ExprNode node2) {
		this.node1 = node1;
		this.node2 = node2;
	}

	@Override
	public boolean evaluate(Robot robot) {
		return node1.evaluate(robot) == node2.evaluate(robot);
	}
	
	public String toString() {
		return node1.toString()+" equal to "+node2.toString();
	}

}

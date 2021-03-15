package code.nodes.conditionals;

import code.src.Robot;

public class OrNode implements ConditionalNode{
	
	ConditionalNode node1;
	ConditionalNode node2;
	
	/**
	 * Represents the "Or" conditional
	 * @author Brannon Haines
	 *
	 */
	public OrNode(ConditionalNode node1, ConditionalNode node2) {
		this.node1 = node1;
		this.node2 = node2;
	}

	@Override
	public boolean evaluate(Robot robot) {
		return node1.evaluate(robot) || node2.evaluate(robot);
	}
	
	public String toString() {
		return node1.toString()+" or "+node2.toString();
	}

}

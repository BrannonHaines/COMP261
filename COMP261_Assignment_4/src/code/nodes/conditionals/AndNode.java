package code.nodes.conditionals;

import code.src.Robot;

public class AndNode implements ConditionalNode{
	
	ConditionalNode node1;
	ConditionalNode node2;
	
	/**
	 * Represents the "And" conditional
	 * @author Brannon Haines
	 *
	 */
	public AndNode(ConditionalNode node1, ConditionalNode node2) {
		this.node1 = node1;
		this.node2 = node2;
	}

	@Override
	public boolean evaluate(Robot robot) {
		return node1.evaluate(robot) && node2.evaluate(robot);
	}
	
	@Override
	public String toString() {
		return node1.toString()+" and "+node2.toString();
	}

}

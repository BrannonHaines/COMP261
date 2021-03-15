package code.nodes.conditionals;

import code.src.Robot;

public class NotNode implements ConditionalNode{
	
	ConditionalNode node;
	
	/**
	 * Represents the "Not" conditional
	 * @author Brannon Haines
	 *
	 */
	public NotNode(ConditionalNode node) {
		this.node = node;
	}

	@Override
	public boolean evaluate(Robot robot) {
		return !node.evaluate(robot);
	}
	
	public String toString() {
		return "Not : "+node.toString();
	}
	
}

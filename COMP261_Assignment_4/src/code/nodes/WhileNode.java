package code.nodes;

import code.nodes.conditionals.ConditionalNode;
import code.src.Robot;
import code.src.RobotProgramNode;

public class WhileNode implements RobotProgramNode{
	
	ConditionalNode conditionalNode;
	BlockNode blockNode;
	
	/**
	 * Represents While statement
	 * @param Condition for while
	 * @author Brannon Haines
	 */
	public WhileNode(ConditionalNode conditionalNode) {
		this.conditionalNode = conditionalNode;
	}

	@Override
	public void execute(Robot robot) {
		while(conditionalNode.evaluate(robot)) {
			blockNode.execute(robot);
		}
		
	}
	
	public String toString() {
		return "WHILE : "+conditionalNode.toString();
	}
	
	public void setBlockNode(BlockNode blockNode) {
		this.blockNode = blockNode;
	}

}

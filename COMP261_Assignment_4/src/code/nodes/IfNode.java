package code.nodes;

import code.nodes.conditionals.ConditionalNode;
import code.src.Robot;
import code.src.RobotProgramNode;

public class IfNode implements RobotProgramNode{
	
	ConditionalNode conditionalNode;
	BlockNode blockNode;
	BlockNode elseBlockNode;
	
	/**
	 * Represents "If" logic statement 
	 * @param Conditon for If
	 * @author Brannon Haines
	 */
	public IfNode(ConditionalNode conditionalNode) {
		this.conditionalNode = conditionalNode;
	}

	@Override
	/**
	 * Executes standard block node or else block node
	 * @author Brannon Haines
	 */
	public void execute(Robot robot) {
		if (conditionalNode.evaluate(robot)) {
			blockNode.execute(robot);
		}
		else {
			elseBlockNode.execute(robot);
		}
	}
	
	@Override
	public String toString() {
		return "IF : "+conditionalNode.toString();
	}
	
	public void setBlockNode(BlockNode blockNode) {
		this.blockNode = blockNode;
	}
	
	public void setElseBlockNode(BlockNode elseBlockNode) {
		this.elseBlockNode = elseBlockNode;
	}



}

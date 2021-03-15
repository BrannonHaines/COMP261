package code.nodes;

import code.src.*;

public class LoopNode implements RobotProgramNode{
	
	BlockNode blockNode;
	
	/**
	 * Represents Loop statement
	 * @param Block node to loop on
	 * @author Brannon Haines
	 */
	public LoopNode(BlockNode blockNode) {
		this.blockNode = blockNode;
	}

	@Override
	public void execute(Robot robot) {
		while(true) {
			blockNode.execute(robot);
		}
		
	}
	
	public String toString() {
		return "Looping";
	}

}

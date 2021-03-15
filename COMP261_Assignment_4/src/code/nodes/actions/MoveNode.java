package code.nodes.actions;

import code.nodes.ExprNode;
import code.src.*;

public class MoveNode implements RobotProgramNode{
	
	ExprNode exprNode;
	
	/**
	 * Represents a Move action
	 * @author Brannon Haines
	 */
	public MoveNode() {
		
	}
	
	@Override
	public void execute(Robot robot) {
		if (exprNode == null) {
			robot.move();
		}
		else {
			int timesToMove = exprNode.evaluate(robot);
			for (int i = 0; i < timesToMove; i++) {
				robot.move();
			}
		}
	}
	
	public String toString() {
		return "Robot moving";
	}
	
	public void setExprNode(ExprNode exprNode) {
		this.exprNode = exprNode;
	}

}

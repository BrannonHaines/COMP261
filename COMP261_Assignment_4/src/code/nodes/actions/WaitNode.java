package code.nodes.actions;

import code.nodes.ExprNode;
import code.src.*;

public class WaitNode implements RobotProgramNode{
	
	ExprNode exprNode;
	
	/**
	 * Represents a Wait action
	 * @author Brannon Haines
	 */
	public WaitNode() {

	}
	
	@Override
	public void execute(Robot robot) {
		if (exprNode == null) {
			robot.idleWait();
		}
		else {
			int waitingTime = exprNode.evaluate(robot);
			for (int i = 0; i < waitingTime; i++) {
				robot.idleWait();
			}
		}
	}
	
	public String toString() {
		return "Robot waiting";
	}
	
	public void setExprNode(ExprNode exprNode) {
		this.exprNode = exprNode;
	}

}

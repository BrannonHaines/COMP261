package code.nodes;

import code.src.Robot;
import code.src.RobotProgramNode;

/**
 * Represents all the different statements
 * @author Brannon Haines
 *
 */
public class StateNode implements RobotProgramNode{
	
	RobotProgramNode statement;
	
	public StateNode(RobotProgramNode actNode) {
		statement = actNode;
	}
	
	public StateNode(LoopNode loopNode) {
		statement = loopNode;
	}
	
	public StateNode(IfNode ifNode) {
		statement = ifNode;
	}
	
	public StateNode(WhileNode whileNode) {
		statement = whileNode;
	}

	@Override
	public void execute(Robot robot) {
		statement.execute(robot);
	}
	
	public String toString() {
		return statement.toString();
	}


}

package code.nodes.actions;

import code.src.Robot;
import code.src.RobotProgramNode;

public class TurnAroundNode implements RobotProgramNode{
	
	/**
	 * Represents a Turn Around action
	 * @author Brannon Haines
	 */
	public TurnAroundNode() {
		
	}
	
	@Override
	public void execute(Robot robot) {
		robot.turnAround();
	}
	
	public String toString() {
		return "Turning around";
	}

}

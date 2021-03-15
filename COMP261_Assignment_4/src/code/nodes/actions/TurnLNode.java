package code.nodes.actions;

import code.src.*;

public class TurnLNode implements RobotProgramNode{
	
	/**
	 * Represents a Turn Left action
	 * @author Brannon Haines
	 */
	public TurnLNode() {
		
	}
	
	@Override
	public void execute(Robot robot) {
		robot.turnLeft();
	}
	
	public String toString() {
		return "Robot turning left";
	}
}

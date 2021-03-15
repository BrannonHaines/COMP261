package code.nodes.actions;

import code.src.*;

public class TurnRNode implements RobotProgramNode{
	
	/**
	 * Represents a Turn Right action
	 * @author Brannon Haines
	 */
	public TurnRNode() {
		
	}
	
	@Override
	public void execute(Robot robot) {
		robot.turnRight();
	}
	
	public String toString() {
		return "Robot turning right";
	}

}

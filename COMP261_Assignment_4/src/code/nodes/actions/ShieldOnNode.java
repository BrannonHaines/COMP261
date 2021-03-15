package code.nodes.actions;

import code.src.Robot;
import code.src.RobotProgramNode;

public class ShieldOnNode implements RobotProgramNode{
	
	/**
	 * Represents a Shield On action
	 * @author Brannon Haines
	 */
	public ShieldOnNode() {
		
	}
	
	@Override
	public void execute(Robot robot) {
		robot.setShield(true);
	}
	
	public String toString() {
		return "Shield On!!!";
	}
}

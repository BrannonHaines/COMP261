package code.nodes.actions;

import code.src.Robot;
import code.src.RobotProgramNode;

public class ShieldOffNode implements RobotProgramNode{
	
	/**
	 * Represents a Shield Off action
	 * @author Brannon Haines
	 */
	public ShieldOffNode() {
		
	}
	
	@Override
	public void execute(Robot robot) {
		robot.setShield(false);
	}
	
	public String toString() {
		return "Shield Off!!!";
	}
}

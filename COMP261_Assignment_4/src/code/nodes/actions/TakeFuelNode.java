package code.nodes.actions;

import code.src.*;

public class TakeFuelNode implements RobotProgramNode{
	
	/**
	 * Represents a Take Fuel action
	 * @author Brannon Haines
	 */
	public TakeFuelNode() {
		
	}
	
	@Override
	public void execute(Robot robot) {
		robot.takeFuel();
	}
	
	public String toString() {
		return "Robot taking fuel";
	}

}

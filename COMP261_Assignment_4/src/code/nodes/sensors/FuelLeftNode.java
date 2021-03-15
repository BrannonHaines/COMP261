package code.nodes.sensors;

import code.nodes.ExprNode;
import code.src.Robot;

/**
 * Represents the fuel left in the robot
 * @author Brannon Haines
 *
 */
public class FuelLeftNode implements ExprNode{

	@Override
	public int evaluate(Robot robot) {
		return robot.getFuel();
	}
	
	public String toString() {
		return "fuelLeft";
	}

}

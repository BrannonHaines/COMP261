package code.nodes.sensors;

import code.nodes.ExprNode;
import code.src.Robot;

/**
 * Represents the distance to the wall from the robot
 * @author Brannon Haines
 *
 */
public class WallDistNode implements ExprNode{

	@Override
	public int evaluate(Robot robot) {
		return robot.getDistanceToWall();
	}
	
	public String toString() {
		return "wallDist";
	}

}

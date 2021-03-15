package code.nodes.sensors;

import code.nodes.ExprNode;
import code.src.Robot;

/**
 * Represents the Front Back location of the closest Barrel
 * @author Brannon Haines
 *
 */
public class BarrelFBNode implements ExprNode{

	@Override
	public int evaluate(Robot robot) {
		return robot.getClosestBarrelFB();
	}
	
	public String toString() {
		return "barrelFB";
	}

}

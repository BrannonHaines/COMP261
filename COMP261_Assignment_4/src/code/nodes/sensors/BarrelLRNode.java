package code.nodes.sensors;

import code.nodes.ExprNode;
import code.src.Robot;

/**
 * Represents the Left Right location of the closest barrel
 * @author Brannon Haines
 *
 */
public class BarrelLRNode implements ExprNode{

	@Override
	public int evaluate(Robot robot) {
		return robot.getClosestBarrelLR();
	}
	
	public String toString() {
		return "barrelLR";
	}
	
	

}

package code.nodes.sensors;

import code.nodes.ExprNode;
import code.src.Robot;

/**
 * Represents the number of barrels
 * @author Brannon Haines
 *
 */
public class NumBarrelsNode implements ExprNode{

	@Override
	public int evaluate(Robot robot) {
		return robot.numBarrels();
	}
	
	public String toString() {
		return "numBarrels";
	}

}

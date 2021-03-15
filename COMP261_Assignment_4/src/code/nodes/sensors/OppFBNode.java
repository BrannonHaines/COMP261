package code.nodes.sensors;

import code.nodes.ExprNode;
import code.src.Robot;

/**
 * Represents the Front Back location of the opponent robot
 * @author Brannon Haines
 *
 */
public class OppFBNode implements ExprNode{

	@Override
	public int evaluate(Robot robot) {
		return robot.getOpponentFB();
	}
	
	public String toString() {
		return "oppFB";
	}

}

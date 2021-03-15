package code.nodes.sensors;

import code.nodes.ExprNode;
import code.src.Robot;

/**
 * Represents the Left Right location of the opponent 
 * @author Brannon Haines
 *
 */
public class OppLRNode implements ExprNode{

	@Override
	public int evaluate(Robot robot) {
		return robot.getOpponentLR();
	}
	
	public String toString() {
		return "oppLR";
	}

}

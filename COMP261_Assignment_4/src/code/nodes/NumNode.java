package code.nodes;

import code.src.Robot;

public class NumNode implements ExprNode{
	
	int num;
	
	/**
	 * Represents a Number
	 * @param Number
	 * @author Brannon Haines
	 */
	public NumNode(int number) {
		this.num = number;
	}
	
	@Override
	public int evaluate(Robot robot) {
		return num;
	}
	
	public String toString() {
		return String.valueOf(num);
	}
	
	

}

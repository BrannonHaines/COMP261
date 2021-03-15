package code.nodes.conditionals;

import code.src.Robot;

/**
 * Interface for all Conditionals
 * @author Brannon Haines
 *
 */
public interface ConditionalNode {
	
	public boolean evaluate(Robot robot);
	
	public String toString();

}

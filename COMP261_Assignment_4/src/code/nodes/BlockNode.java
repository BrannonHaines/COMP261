package code.nodes;

import java.util.ArrayList;

import code.src.*;

public class BlockNode implements RobotProgramNode{
	
	ArrayList<StateNode> statements;
	
	/**
	 * Represents a block of statements to execute
	 * @param List of Statements to execute
	 * @author Brannon Haines
	 */
	public BlockNode(ArrayList<StateNode> statements) {
		this.statements = statements;
	}
	
	public BlockNode() {
		statements = new ArrayList<StateNode>();
	}
	
	@Override
	public void execute(Robot robot) {
		for (StateNode state : statements) {
			state.execute(robot);
		}
	}
	
	public void addStateNode(StateNode node) {
		statements.add(node);
	}
	
	public String toString() {
		String str = "";
		for (StateNode statement : statements) {
			str += statement.toString()+", ";
		}
		return str;
	}
}

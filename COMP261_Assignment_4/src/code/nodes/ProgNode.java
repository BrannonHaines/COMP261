package code.nodes;

import java.util.ArrayList;

import code.src.*;

public class ProgNode implements RobotProgramNode{
	
	ArrayList<StateNode> statements = new ArrayList<StateNode>();
	
	public ProgNode(ArrayList<StateNode> statements) {
		this.statements = statements;
	}

	@Override
	public void execute(Robot robot) {
		for (StateNode statement : statements) {
			statement.execute(robot);
		}
		
	}
	
	public void addNode(StateNode node) {
		statements.add(node);
	}
	
	public String toString() {
		String msg = "";
		for (StateNode statement : statements) {
			msg += statement.toString()+"\n";
		}
		return msg;
	}

}

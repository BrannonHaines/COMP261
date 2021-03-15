package code.src;
import code.nodes.*;
import code.nodes.actions.MoveNode;
import code.nodes.actions.ShieldOffNode;
import code.nodes.actions.ShieldOnNode;
import code.nodes.actions.TakeFuelNode;
import code.nodes.actions.TurnAroundNode;
import code.nodes.actions.TurnLNode;
import code.nodes.actions.TurnRNode;
import code.nodes.actions.WaitNode;
import code.nodes.conditionals.AndNode;
import code.nodes.conditionals.ConditionalNode;
import code.nodes.conditionals.EqualNode;
import code.nodes.conditionals.GreaterThanNode;
import code.nodes.conditionals.LessThanNode;
import code.nodes.conditionals.NotNode;
import code.nodes.conditionals.OrNode;
import code.nodes.operators.AdditionNode;
import code.nodes.operators.DivisionNode;
import code.nodes.operators.MultiplicationNode;
import code.nodes.operators.SubtractionNode;
import code.nodes.sensors.BarrelFBNode;
import code.nodes.sensors.BarrelLRNode;
import code.nodes.sensors.FuelLeftNode;
import code.nodes.sensors.NumBarrelsNode;
import code.nodes.sensors.OppFBNode;
import code.nodes.sensors.OppLRNode;
import code.nodes.sensors.WallDistNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.*;
import javax.swing.JFileChooser;

/**
 * The parser and interpreter. The top level parse function, a main method for
 * testing, and several utility methods are provided. You need to implement
 * parseProgram and all the rest of the parser.
 */
public class Parser {

	/**
	 * Top level parse method, called by the World
	 */
	static RobotProgramNode parseFile(File code) {
		Scanner scan = null;
		try {
			scan = new Scanner(code);

			// the only time tokens can be next to each other is
			// when one of them is one of (){},;
			scan.useDelimiter("\\s+|(?=[{}(),;])|(?<=[{}(),;])");

			RobotProgramNode n = parseProgram(scan); // You need to implement this!!!

			scan.close();
			return n;
		} catch (FileNotFoundException e) {
			System.out.println("Robot program source file not found");
		} catch (ParserFailureException e) {
			System.out.println("Parser error:");
			System.out.println(e.getMessage());
			scan.close();
		}
		return null;
	}

	/** For testing the parser without requiring the world */

	public static void main(String[] args) {
		if (args.length > 0) {
			for (String arg : args) {
				File f = new File(arg);
				if (f.exists()) {
					System.out.println("Parsing '" + f + "'");
					RobotProgramNode prog = parseFile(f);
					System.out.println("Parsing completed ");
					if (prog != null) {
						System.out.println("================\nProgram:");
						System.out.println(prog);
					}
					System.out.println("=================");
				} else {
					System.out.println("Can't find file '" + f + "'");
				}
			}
		} else {
			while (true) {
				JFileChooser chooser = new JFileChooser(".");// System.getProperty("user.dir"));
				int res = chooser.showOpenDialog(null);
				if (res != JFileChooser.APPROVE_OPTION) {
					break;
				}
				RobotProgramNode prog = parseFile(chooser.getSelectedFile());
				System.out.println("Parsing completed");
				if (prog != null) {
					System.out.println("Program: \n" + prog);
				}
				System.out.println("=================");
			}
		}
		System.out.println("Done");
	}

	// Useful Patterns

	static Pattern NUMPAT = Pattern.compile("-?\\d+"); // ("-?(0|[1-9][0-9]*)");
	static Pattern OPENPAREN = Pattern.compile("\\(");
	static Pattern CLOSEPAREN = Pattern.compile("\\)");
	static Pattern OPENBRACE = Pattern.compile("\\{");
	static Pattern CLOSEBRACE = Pattern.compile("\\}");
	static Pattern SEMICOLON = Pattern.compile(";");
	static Pattern ACT = Pattern.compile("move|turnL|turnR|takeFuel|wait|shieldOn|shieldOff|turnAround");
	static Pattern LOOP = Pattern.compile("loop");
	static Pattern IF = Pattern.compile("if");
	static Pattern WHILE = Pattern.compile("while");
	static Pattern COND = Pattern.compile("RELOP|\\(|SEN|NUM|\\)");
	static Pattern RELOP = Pattern.compile("lt|gt|eq");
	static Pattern SEN = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");
	static Pattern NUM = Pattern.compile("-?[1-9][0-9]*|0");
	static Pattern OP = Pattern.compile("add|sub|mul|div");
	static Pattern COMMA = Pattern.compile(",");

	/**
	 * PROG ::= STMT+
	 */
	static RobotProgramNode parseProgram(Scanner s) {
		// THE PARSER GOES HERE
		ArrayList<StateNode> statements = new ArrayList<StateNode>();
		while(s.hasNext()) {
			statements.add(parseState(s));
		}
		return new ProgNode(statements);
	}
	
	/**
	 * STMT ::= ACT ";" | LOOP | IF | WHILE
	 * @author Brannon Haines
	 */
	static StateNode parseState(Scanner s) {
		if (s.hasNext(ACT)) {
			StateNode stateNode = new StateNode(parseAction(s));
			require(SEMICOLON, "Statement not finished", s);
			return stateNode;
		}
		if (s.hasNext(LOOP)) {
			StateNode stateNode = new StateNode(parseLoop(s));
			return stateNode;
		}
		if (s.hasNext(IF)) {
			s.next();
			StateNode stateNode = new StateNode(parseIf(s));
			return stateNode;
		}
		if (s.hasNext(WHILE)) {
			s.next();
			StateNode stateNode = new StateNode(parseWhile(s));
			return stateNode;
		}
		fail("Invalid statement", s);
		return null;
	}
	
	/**
	 * ACT ::= "move("EXPR")" | "turnL" | "turnR" | "turnAround" | "shieldOn" | "shieldOff" | "takeFuel" | "wait("EXPR")"
	 * @author Brannon Haines
	 */
	static RobotProgramNode parseAction(Scanner s) {
		if (s.hasNext("move")) {
			s.next();
			MoveNode moveNode = new MoveNode();
			if (s.hasNext(OPENPAREN)) {
				s.next();
				moveNode.setExprNode(parseExpr(s));
				require(CLOSEPAREN, "No closing parentheses found", s);
			}
			return moveNode;
		}
		else if (s.hasNext("turnL")) {
			s.next();
			return new TurnLNode();
		}
		else if (s.hasNext("turnR")) {
			s.next();
			return new TurnRNode();
		}
		else if (s.hasNext("turnAround")) {
			s.next();
			return new TurnAroundNode();
		}
		else if (s.hasNext("takeFuel")) {
			s.next();
			return new TakeFuelNode();
		}
		else if (s.hasNext("wait")) {
			s.next();
			WaitNode waitNode = new WaitNode();
			if (s.hasNext(OPENPAREN)) {
				s.next();
				waitNode.setExprNode(parseExpr(s));
				require(CLOSEPAREN, "No closing parentheses", s);
			}
			return waitNode;
		}
		else if (s.hasNext("shieldOn")) {
			s.next();
			return new ShieldOnNode();
		}
		else if (s.hasNext("shieldOff")) {
			s.next();
			return new ShieldOffNode();
		}
		fail("Invalid action", s);
		return null;
	}
	
	/**
	 * LOOP ::= "loop" BLOCK
	 * @author Brannon Haines
	 */
	static LoopNode parseLoop(Scanner s) {
		require(LOOP, "Loop starts", s);
		require(OPENBRACE, "Loop doesn't have open brace", s);
		BlockNode blockNode = null;
		if (s.hasNext(CLOSEBRACE)) {
			fail("Loop is empty", s);
		}
		else {
			blockNode = parseBlock(s);
		}
		require(CLOSEBRACE, "Loop doesn't have closed brace", s);
		return new LoopNode(blockNode);
	}
	
	/**
	 * BLOCK ::= "{" STMT+ "}"
	 * @author Brannon Haines
	 */
	static BlockNode parseBlock(Scanner s) {
		ArrayList<StateNode> statements = new ArrayList<StateNode>();
		while(!s.hasNext(CLOSEBRACE)) {
			statements.add(parseState(s));
		}
		if (statements.isEmpty()) {
			fail("No statements found in block", s);
		}
		return new BlockNode(statements);
		
	}
	
	/**
	 * WHILE ::= "while" "(" COND ")" BLOCK
	 * @author Brannon Haines
	 */
	static WhileNode parseWhile(Scanner s) {
		require(OPENPAREN, "No opening parentheses found", s);
		WhileNode whileNode = new WhileNode(parseConditional(s));
		require(CLOSEPAREN, "No closing parentheses found", s);
		require(OPENBRACE, "No open brace", s);
		whileNode.setBlockNode(parseBlock(s));
		require(CLOSEBRACE, "No closing brace", s);
		return whileNode;
	}
	
	/**
	 * IF ::= "if" "(" COND ")" BLOCK [ "else" BLOCK ]
	 * @author Brannon Haines
	 */
	static IfNode parseIf(Scanner s) {
		require(OPENPAREN, "No opening parentheses found", s);
		IfNode ifNode = new IfNode(parseConditional(s));
		require(CLOSEPAREN, "No closing parentheses found", s);
		require(OPENBRACE, "No open brace", s);
		ifNode.setBlockNode(parseBlock(s));
		require(CLOSEBRACE, "No closing brace", s);
		if(s.hasNext("else")) {
			s.next();
			require(OPENBRACE, "No open brace", s);
			ifNode.setElseBlockNode(parseBlock(s));
			require(CLOSEBRACE, "No closing brace", s);
		}
		return ifNode;
	}
	
	/**
	 * COND ::= "and" "(" COND "," COND ")" | or "(" COND "," COND ")" | "not" "(" COND ")" | "lt" "(" EXPR "," EXPR ")" |  "gt" "(" EXPR "," EXPR ")" |  "eq" "(" EXPR "," EXPR ")"
	 * @author Brannon Haines
	 */
	static ConditionalNode parseConditional(Scanner s) {
		if (s.hasNext("lt")) {
			s.next();
			require(OPENPAREN, "No opening parentheses found", s);
			ExprNode node1 =  parseExpr(s);
			require(COMMA, "No comma found", s);
			ExprNode node2 = parseExpr(s);
			require(CLOSEPAREN, "No closing parentheses found", s);
			return new LessThanNode(node1, node2);
		}
		else if (s.hasNext("gt")) {
			s.next();
			require(OPENPAREN, "No opening parentheses found", s);
			ExprNode node1 = parseExpr(s);
			require(COMMA, "No comma found", s);
			ExprNode node2 = parseExpr(s);
			require(CLOSEPAREN, "No closing parentheses found", s);
			return new GreaterThanNode(node1, node2);
		}
		else if (s.hasNext("eq")) {
			s.next();
			require(OPENPAREN, "No opening parentheses found", s);
			ExprNode node1 = parseExpr(s);
			require(COMMA, "No comma found", s);
			ExprNode node2 = parseExpr(s);
			require(CLOSEPAREN, "No closing parentheses found", s);
			return new EqualNode(node1, node2);
		}
		else if (s.hasNext("and")) {
			s.next();
			require(OPENPAREN, "No open parentheses", s);
			ConditionalNode node1 = parseConditional(s);
			require(COMMA, "No comma found", s);
			ConditionalNode node2 = parseConditional(s);
			require(CLOSEPAREN, "No closing parentheses", s);
			return new AndNode(node1, node2);
		}
		else if (s.hasNext("or")) {
			s.next();
			require(OPENPAREN, "No open parentheses", s);
			ConditionalNode node1 = parseConditional(s);
			require(COMMA, "No comma found", s);
			ConditionalNode node2 = parseConditional(s);
			require(CLOSEPAREN, "No closing parentheses", s);
			return new OrNode(node1, node2);
		}
		else if (s.hasNext("not")) {
			s.next();
			require(OPENPAREN, "No open parentheses", s);
			ConditionalNode node1 = parseConditional(s);
			require(CLOSEPAREN, "No closing parentheses", s);
			return new NotNode(node1);
		}
		fail("Invalid conditional", s);
		return null;
	}
	
	/**
	 * EXPR ::= NUM | SEN | OP "(" EXPR "," EXPR ")"
	 */
	static ExprNode parseExpr(Scanner s) {
		if (s.hasNext(NUM)) {
			return parseNum(s);
		}
		else if (s.hasNext(SEN)) {
			return parseSensor(s);
		}
		else if (s.hasNext(OP)) {
			return parseOperator(s);
		}
		fail("Invalid expression", s);
		return null;
	}
	
	/**
	 * NUM ::= "-?[0-9]+"
	 * @author Brannon Haines
	 */
	static ExprNode parseNum(Scanner s) {
		if (s.hasNext("-?[0-9]+")) {
			int num = s.nextInt();
			return new NumNode(num);
		}
		fail("Invalid number", s);
		return null;
	}
	
	/**
	 * SEN ::= "fuelLeft" | "oppLR" | "oppFB" | "numBarrels" | "barrelLR" | "barrelFB" | "wallDist"
	 * @author Brannon Haines
	 */
	static ExprNode parseSensor(Scanner s) {
		if (s.hasNext("fuelLeft")) {
			s.next();
			return new FuelLeftNode();
		}
		else if (s.hasNext("oppLR")) {
			s.next();
			return new OppLRNode();
		}
		else if (s.hasNext("oppFB")) {
			s.next();
			return new OppFBNode();
		}
		else if (s.hasNext("numBarrels")) {
			s.next();
			return new NumBarrelsNode();
		}
		else if (s.hasNext("barrelLR")) {
			s.next();
			return new BarrelLRNode();
		}
		else if (s.hasNext("barrelFB")) {
			s.next();
			return new BarrelFBNode();
		}
		else if (s.hasNext("wallDist")) {
			s.next();
			return new WallDistNode();
		}
		fail("Invalid Sensor", s);
		return null;
	}
	
	/**
	 * OP ::= "add" | "sub" | "mul" | "div"
	 * @author Brannon Haines
	 */
	static ExprNode parseOperator(Scanner s) {
		if (s.hasNext("add")) {
			s.next();
			require(OPENPAREN, "No open parentheses", s);
			ExprNode node1 = parseExpr(s);
			require(COMMA, "No comma found", s);
			ExprNode node2 = parseExpr(s);
			require(CLOSEPAREN, "No closing parentheses", s);
			return new AdditionNode(node1, node2);
		}
		else if (s.hasNext("sub")) {
			s.next();
			require(OPENPAREN, "No open parentheses", s);
			ExprNode node1 = parseExpr(s);
			require(COMMA, "No comma found", s);
			ExprNode node2 = parseExpr(s);
			require(CLOSEPAREN, "No closing parentheses", s);
			return new SubtractionNode(node1, node2);
		}
		else if (s.hasNext("mul")) {
			s.next();
			require(OPENPAREN, "No open parentheses", s);
			ExprNode node1 = parseExpr(s);
			require(COMMA, "No comma found", s);
			ExprNode node2 = parseExpr(s);
			require(CLOSEPAREN, "No closing parentheses", s);
			return new MultiplicationNode(node1, node2);
		}
		else if (s.hasNext("div")) {
			s.next();
			require(OPENPAREN, "No open parentheses", s);
			ExprNode node1 = parseExpr(s);
			require(COMMA, "No comma found", s);
			ExprNode node2 = parseExpr(s);
			require(CLOSEPAREN, "No closing parentheses", s);
			return new DivisionNode(node1, node2);
		}
		fail("Invalid operator", s);
		return null;
	}

	// utility methods for the parser

	/**
	 * Report a failure in the parser.
	 */
	static void fail(String message, Scanner s) {
		String msg = message + "\n   @ ...";
		for (int i = 0; i < 5 && s.hasNext(); i++) {
			msg += " " + s.next();
		}
		throw new ParserFailureException(msg + "...");
	}

	/**
	 * Requires that the next token matches a pattern if it matches, it consumes
	 * and returns the token, if not, it throws an exception with an error
	 * message
	 */
	static String require(String p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}

	static String require(Pattern p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}

	/**
	 * Requires that the next token matches a pattern (which should only match a
	 * number) if it matches, it consumes and returns the token as an integer if
	 * not, it throws an exception with an error message
	 */
	static int requireInt(String p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	static int requireInt(Pattern p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	/**
	 * Checks whether the next token in the scanner matches the specified
	 * pattern, if so, consumes the token and return true. Otherwise returns
	 * false without consuming anything.
	 */
	static boolean checkFor(String p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

	static boolean checkFor(Pattern p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

}

// You could add the node classes here, as long as they are not declared public (or private)

package io.github.learnlexyacc.cookcc;

import java.io.IOException;
import java.util.HashMap;

import org.yuanheng.cookcc.CookCCByte;
import org.yuanheng.cookcc.CookCCOption;
import org.yuanheng.cookcc.CookCCToken;
import org.yuanheng.cookcc.Lex;
import org.yuanheng.cookcc.Lexs;
import org.yuanheng.cookcc.Rule;
import org.yuanheng.cookcc.TokenGroup;
import org.yuanheng.cookcc.TokenType;

/**
 * Adapted from:
 *
 * https://github.com/coconut2015/cookcc/blob/master/tests/javaap/calc/Calculator.java
 *
 * which is a great example of a small programming language.
 */
@CookCCOption(lexerTable = "compressed", parserTable = "compressed")
public class CookCCCalculatorParser extends CookCCByte {
	/**
	 * This is the result of parsing the input
	 */
	private final HashMap<String, Double> variables = new HashMap<>();

	public HashMap<String, Double> getVariables() {
		return variables;
	}

	/**
	 * Specify the tokens used by the lexer and parser.
	 */
	@CookCCToken
	static enum Token {
		// TokenGroup is used to specify the token type and precedence.
		// By default, if the type of the token is not specified, it is
		// TokenGroup.NONASSOC.
		@TokenGroup
		VARIABLE, NUMBER, ASSIGNMENT, SEMICOLON, LPAREN, RPAREN,

		// specify left associativity.
		@TokenGroup(type = TokenType.LEFT)
		ADD, SUBTRACT,

		@TokenGroup(type = TokenType.LEFT)
		MULTIPLY, DIVIDE,

		@TokenGroup(type = TokenType.LEFT)
		UMINUS
	}

	/**
	 * Track the nodes that represent variable assignments
	 */
	private final HashMap<String, Node> variableNodes = new HashMap<>();

	// ========================================================================
	// Lexing Rules: they run in order of precedence, so one would want key words
	// to appear before identifiers

	@Lex(pattern = "[0-9]+(\\.[0-9]+)?", token = "NUMBER")
	public Double parseNumber() {
		return Double.parseDouble(yyText());
	}

	@Lexs(patterns = { @Lex(pattern = "[;]", token = "SEMICOLON"), @Lex(pattern = "[=]", token = "ASSIGNMENT"),
			@Lex(pattern = "[+]", token = "ADD"), @Lex(pattern = "[\\-]", token = "SUBTRACT"),
			@Lex(pattern = "[*]", token = "MULTIPLY"), @Lex(pattern = "[/]", token = "DIVIDE") })
	public char parseOperator() {
		return yyText().charAt(0);
	}

	@Lexs(patterns = { @Lex(pattern = "[(]", token = "LPAREN"), @Lex(pattern = "[)]", token = "RPAREN") })
	public char parseParen() {
		return yyText().charAt(0);
	}

	@Lex(pattern = "[a-z]+", token = "VARIABLE")
	public String parseVariable() {
		return yyText();
	}

	@Lex(pattern = "[ \\t\\r\\n]+")
	public void ignoreWhiteSpace() {
	}

	@Lex(pattern = ".")
	public void invalidCharacter() throws IOException {
		throw new IOException("Illegal character: " + yyText());
	}

	@Lex(pattern = "<<EOF>>", token = "$")
	public void parseEOF() {
	}

	// ========================================================================
	// Parsing Rules

	@Rule(lhs = "program", rhs = "statement_list", args = "1")
	public void parseProgram(Node node) {
		interpret(node);
	}

	@Rule(lhs = "statement_list", rhs = "statement", args = "1")
	public Node parseStatementList(Node statement) {
		return statement;
	}

	@Rule(lhs = "statement_list", rhs = "statement_list statement", args = "1 2")
	public Node parseStatementList(Node statementList, Node statement) {
		return new SemicolonNode(statementList, statement);
	}

	@Rule(lhs = "statement", rhs = "SEMICOLON")
	public Node parseStatement() {
		return new SemicolonNode();
	}

	@Rule(lhs = "statement", rhs = "expression SEMICOLON", args = "1")
	public Node parseStatement(Node expr) {
		return expr;
	}

	@Rule(lhs = "statement", rhs = "VARIABLE ASSIGNMENT expression SEMICOLON", args = "1 3")
	public Node parseAssignment(String var, Node expr) {
		return new AssignmentNode(var, expr);
	}

	@Rule(lhs = "expression", rhs = "SUBTRACT expression", args = "2", precedence = "UMINUS")
	public Node parseUminus(Node expression) {
		return new ExpressionNode(Token.UMINUS, expression, null);
	}

	@Rule(lhs = "expression", rhs = "expression ADD expression", args = "1 3")
	public Node parseAddition(Node expression1, Node expression2) {
		return new ExpressionNode(Token.ADD, expression1, expression2);
	}

	@Rule(lhs = "expression", rhs = "expression SUBTRACT expression", args = "1 3")
	public Node parseSubtraction(Node expression1, Node expression2) {
		return new ExpressionNode(Token.SUBTRACT, expression1, expression2);
	}

	@Rule(lhs = "expression", rhs = "expression MULTIPLY expression", args = "1 3")
	public Node parseMultiplication(Node expression1, Node expression2) {
		return new ExpressionNode(Token.MULTIPLY, expression1, expression2);
	}

	@Rule(lhs = "expression", rhs = "expression DIVIDE expression", args = "1 3")
	public Node parseDivision(Node expression1, Node expression2) {
		return new ExpressionNode(Token.DIVIDE, expression1, expression2);
	}

	@Rule(lhs = "expression", rhs = "LPAREN expression RPAREN", args = "2")
	public Node parseNestedExpression(Node nestedExpression) {
		return nestedExpression;
	}

	@Rule(lhs = "expression", rhs = "NUMBER", args = "1")
	public Node parseNumber(Double value) {
		return new NumberNode(value);
	}

	@Rule(lhs = "expression", rhs = "VARIABLE", args = "1")
	public Node parseVariable(String variable) {
		IdentifierNode identifier = (IdentifierNode) variableNodes.get(variable);
		if (identifier == null) {
			identifier = new IdentifierNode(variable);
			variableNodes.put(variable, identifier);
		}
		return identifier;
	}

	// ========================================================================
	// Node classes used to build a syntax tree

	public static class Node {
	}

	static class IdentifierNode extends Node {
		String name;
		Double value;

		public IdentifierNode(String name) {
			this.name = name;
		}
	}

	static class NumberNode extends Node {
		double value;

		public NumberNode(Double value) {
			this.value = value.doubleValue();
		}
	}

	static class OperatorNode extends Node {
		final Token operator;

		public OperatorNode(Token operator) {
			this.operator = operator;
		}
	}

	static class SemicolonNode extends OperatorNode {
		Node statement1;
		Node statement2;

		public SemicolonNode() {
			super(Token.SEMICOLON);
		}

		public SemicolonNode(Node statement1, Node statement2) {
			super(Token.SEMICOLON);
			this.statement1 = statement1;
			this.statement2 = statement2;
		}
	}

	static class AssignmentNode extends OperatorNode {
		String variable;
		Node expression;

		public AssignmentNode(String variable, Node expression) {
			super(Token.ASSIGNMENT);
			this.variable = variable;
			this.expression = expression;
		}
	}

	static class ExpressionNode extends OperatorNode {
		Node lhs;
		Node rhs;

		public ExpressionNode(Token operator, Node lhs, Node rhs) {
			super(operator);
			this.lhs = lhs;
			this.rhs = rhs;
		}
	}

	double interpret(Node node) {
		if (node == null) {
			System.out.println("Huh?");
			return 0;
		}
		if (node instanceof NumberNode) {
			return ((NumberNode) node).value;
		}
		if (node instanceof IdentifierNode) {
			if (((IdentifierNode) node).value == null) {
				throw new IllegalArgumentException("Undefined variable: " + ((IdentifierNode) node).name);
			}
			return ((IdentifierNode) node).value;
		}

		// Everything else is an operator with expression node children
		switch (((OperatorNode) node).operator) {
		case SEMICOLON:
			interpret(((SemicolonNode) node).statement1);
			return interpret(((SemicolonNode) node).statement2);
		case ASSIGNMENT: {
			String variable = ((AssignmentNode) node).variable;
			IdentifierNode identifier = (IdentifierNode) variableNodes.get(variable);
			if (identifier == null) {
				identifier = new IdentifierNode(variable);
				variableNodes.put(variable, identifier);
			}
			identifier.value = interpret(((AssignmentNode) node).expression);
			variables.put(variable, identifier.value);
			return identifier.value;
		}
		case UMINUS:
			return -interpret(((ExpressionNode) node).lhs);
		case ADD:
			return interpret(((ExpressionNode) node).lhs) + interpret(((ExpressionNode) node).rhs);
		case SUBTRACT:
			return interpret(((ExpressionNode) node).lhs) - interpret(((ExpressionNode) node).rhs);
		case MULTIPLY:
			return interpret(((ExpressionNode) node).lhs) * interpret(((ExpressionNode) node).rhs);
		case DIVIDE:
			return interpret(((ExpressionNode) node).lhs) / interpret(((ExpressionNode) node).rhs);
		default:
			throw new IllegalArgumentException("Unsupported operator: " + ((OperatorNode) node).operator);
		}
	}
}

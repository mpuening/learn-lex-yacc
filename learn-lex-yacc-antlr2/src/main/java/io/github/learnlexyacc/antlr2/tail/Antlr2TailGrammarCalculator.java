package io.github.learnlexyacc.antlr2.tail;

import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import antlr.collections.AST;
import io.github.learnlexyacc.antlr2.Antlr2Calculator;

/**
 * This is a simple POJO class that evaluates an expression statement.
 *
 * It uses the tail grammar.
 */
public class Antlr2TailGrammarCalculator implements Antlr2Calculator {

	public Map<String, Double> evaluate(String statement) throws Exception {
		CalcLexer lexer = new CalcLexer(new ByteArrayInputStream(statement.getBytes()));
		CalcTailParser parser = new CalcTailParser(lexer);
		parser.stmt();
		Evaluator evaluator = new Evaluator(parser.getAST());
		return evaluator.getResult();
	}

	/**
	 * This class walks the syntax tree to evaluate the statement.
	 */
	public static class Evaluator {
		private final AST statement;
		private final Stack<Double> terms = new Stack<>();
		private final Stack<Integer> operators = new Stack<>();
		private final Map<String, Double> variables = new LinkedHashMap<>();
		private String currentVariable = null;

		public Evaluator(AST statement) throws Exception {
			this.statement = statement;
			walkStatement(this.statement);
		}

		public Map<String, Double> getResult() {
			return variables;
		}

		private AST walkStatement(AST node) {
			while (node != null) {
				node = walkVariable(node);
				// Move pass the ASSIGNMENT
				node = node.getNextSibling();
				node = walkExpression(node);
				// Move pass the SEMI
				node = node.getNextSibling();
				variables.put(currentVariable, terms.pop());
			}
			return node;
		}

		private AST walkVariable(AST node) {
			this.currentVariable = node.getText();
			return node.getNextSibling();
		}

		private AST walkExpression(AST node) {
			node = walkProduct(node);
			node = walkSumTail(node);
			return node;
		}

		private AST walkSumTail(AST node) {
			if (node != null
					&& (CalcLexerTokenTypes.ADD == node.getType() || CalcLexerTokenTypes.SUBTRACT == node.getType())) {
				operators.push(node.getType());
				node = node != null ? node.getNextSibling() : null;
				node = walkProduct(node);
				performOperation();
				node = walkSumTail(node);
			}
			return node;
		}

		private AST walkProduct(AST node) {
			node = walkTerm(node);
			node = walkProductTail(node);
			return node;
		}

		private AST walkProductTail(AST node) {
			if (node != null && (CalcLexerTokenTypes.MULTIPLY == node.getType()
					|| CalcLexerTokenTypes.DIVIDE == node.getType())) {
				operators.push(node.getType());
				node = node != null ? node.getNextSibling() : null;
				node = walkTerm(node);
				performOperation();
				node = walkProductTail(node);
			}
			return node;
		}

		private AST walkTerm(AST node) {
			if (node != null && CalcLexerTokenTypes.NUMBER == node.getType()) {
				terms.push(Double.parseDouble(node.getText()));
				node = node.getNextSibling();
			} else if (node != null && CalcLexerTokenTypes.VARIABLE == node.getType()) {
				String variable = node.getText();
				if (!variables.containsKey(variable)) {
					throw new IllegalArgumentException("Undefined variable: " + variable);
				}
				terms.push(variables.get(variable));
				node = node.getNextSibling();
			} else if (node != null && CalcLexerTokenTypes.LPAREN == node.getType()) {
				node = walkExpression(node.getNextSibling());
				// Move pass the RPAREN
				node = node.getNextSibling();
			}
			return node;
		}

		private void performOperation() {
			Integer operator = operators.pop();
			Double rhs = terms.pop();
			Double lhs = terms.pop();
			if (operator.equals(CalcLexerTokenTypes.ADD)) {
				terms.push(lhs + rhs);
			} else if (operator.equals(CalcLexerTokenTypes.SUBTRACT)) {
				terms.push(lhs - rhs);
			} else if (operator.equals(CalcLexerTokenTypes.MULTIPLY)) {
				terms.push(lhs * rhs);
			} else if (operator.equals(CalcLexerTokenTypes.DIVIDE)) {
				terms.push(lhs / rhs);
			}
		}
	}
}

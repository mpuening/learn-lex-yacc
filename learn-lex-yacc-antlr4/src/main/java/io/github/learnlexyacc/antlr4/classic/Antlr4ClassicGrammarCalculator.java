package io.github.learnlexyacc.antlr4.classic;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import io.github.learnlexyacc.antlr4.Antlr4Calculator;
import io.github.learnlexyacc.antlr4.classic.ClassicCalcBaseVisitor;
import io.github.learnlexyacc.antlr4.classic.ClassicCalcLexer;
import io.github.learnlexyacc.antlr4.classic.ClassicCalcParser;
import io.github.learnlexyacc.antlr4.classic.ClassicCalcParser.ExpressionContext;
import io.github.learnlexyacc.antlr4.classic.ClassicCalcParser.ProductContext;
import io.github.learnlexyacc.antlr4.classic.ClassicCalcParser.StatementContext;
import io.github.learnlexyacc.antlr4.classic.ClassicCalcParser.TermContext;
import io.github.learnlexyacc.antlr4.classic.ClassicCalcParser.VariableContext;

/**
 * This is a simple POJO class that evaluates an expression statement.
 *
 * It uses a classic style grammar for expressions.
 */
public class Antlr4ClassicGrammarCalculator implements Antlr4Calculator {

	public Map<String, Double> evaluate(String statement) {
		ClassicCalcLexer lexer = new ClassicCalcLexer(CharStreams.fromString(statement));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		ClassicCalcParser parser = new ClassicCalcParser(tokens);
		parser.setErrorHandler(new BailErrorStrategy());
		ParseTree tree = parser.statement();
		return new StatementEvaluator(lexer, tree).getVariables();
	}

	/**
	 * Evaluates statement to a map of values
	 */
	class StatementEvaluator extends ClassicCalcBaseVisitor<Map<String, Double>> {

		private final ClassicCalcLexer lexer;
		private final Map<String, Double> variables = new LinkedHashMap<>();

		private String currentVariable = null;

		public StatementEvaluator(ClassicCalcLexer lexer, ParseTree tree) {
			this.lexer = lexer;
			visit(tree);
		}

		public Map<String, Double> getVariables() {
			return variables;
		}

		@Override
		public Map<String, Double> visitStatement(StatementContext ctx) {
			for (int i = 0; i < ctx.expression().size(); i++) {
				visit(ctx.variable(i));
				double result = new ExpressionEvaluator(lexer, variables, ctx.expression(i)).getResult();
				variables.put(currentVariable, result);
			}
			return variables;
		}

		@Override
		public Map<String, Double> visitVariable(VariableContext ctx) {
			currentVariable = ctx.getText();
			return variables;
		}
	}

	/**
	 * Evaluates expression to a value
	 */
	class ExpressionEvaluator extends ClassicCalcBaseVisitor<Void> {

		private final ClassicCalcLexer lexer;
		private final Map<String, Double> variables;
		private final Stack<Double> terms = new Stack<>();

		public ExpressionEvaluator(ClassicCalcLexer lexer, Map<String, Double> variables, ParseTree tree) {
			this.lexer = lexer;
			this.variables = variables;
			visit(tree);
		}

		public Double getResult() {
			return terms.peek();
		}

		@Override
		public Void visitExpression(ExpressionContext ctx) {
			visit(ctx.product(0));
			for (int i = 1; i < ctx.product().size(); i++) {
				visit(ctx.product(i));
				performOperation(ctx.addOperation(i - 1).getText());
			}
			return null;
		}

		@Override
		public Void visitProduct(ProductContext ctx) {
			visit(ctx.term(0));
			for (int i = 1; i < ctx.term().size(); i++) {
				visit(ctx.term(i));
				performOperation(ctx.productOperation(i - 1).getText());
			}
			return null;
		}

		@Override
		public Void visitTerm(TermContext ctx) {
			String type = this.lexer.getVocabulary().getSymbolicName(ctx.getStart().getType());
			if ("NUMBER".equals(type)) {
				terms.push(Double.parseDouble(ctx.getText()));
			} else if ("VARIABLE".equals(type)) {
				String variable = ctx.getText();
				if (!variables.containsKey(variable)) {
					throw new IllegalArgumentException("Undefined variable: " + variable);
				}
				terms.push(variables.get(variable));
			} else {
				visit(ctx.expression());
			}
			return null;
		}

		private void performOperation(String operator) {
			Double rhs = terms.pop();
			Double lhs = terms.pop();
			if ("+".equals(operator)) {
				terms.push(lhs + rhs);
			} else if ("-".equals(operator)) {
				terms.push(lhs - rhs);
			} else if ("*".equals(operator)) {
				terms.push(lhs * rhs);
			} else if ("/".equals(operator)) {
				terms.push(lhs / rhs);
			} else {
				throw new IllegalArgumentException("Unsupported operation: " + operator);
			}
		}
	}
}

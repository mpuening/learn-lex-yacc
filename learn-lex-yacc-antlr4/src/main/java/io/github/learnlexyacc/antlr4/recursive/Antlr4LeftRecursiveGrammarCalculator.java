package io.github.learnlexyacc.antlr4.recursive;

import java.util.LinkedHashMap;
import java.util.Map;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import io.github.learnlexyacc.antlr4.Antlr4Calculator;
import io.github.learnlexyacc.antlr4.recursive.LeftRecursiveCalcBaseVisitor;
import io.github.learnlexyacc.antlr4.recursive.LeftRecursiveCalcLexer;
import io.github.learnlexyacc.antlr4.recursive.LeftRecursiveCalcParser;
import io.github.learnlexyacc.antlr4.recursive.LeftRecursiveCalcParser.NestedExpressionContext;
import io.github.learnlexyacc.antlr4.recursive.LeftRecursiveCalcParser.NumberTermContext;
import io.github.learnlexyacc.antlr4.recursive.LeftRecursiveCalcParser.OperationContext;
import io.github.learnlexyacc.antlr4.recursive.LeftRecursiveCalcParser.StatementContext;
import io.github.learnlexyacc.antlr4.recursive.LeftRecursiveCalcParser.VariableContext;
import io.github.learnlexyacc.antlr4.recursive.LeftRecursiveCalcParser.VariableTermContext;

/**
 * This is a simple POJO class that evaluates an expression statement.
 *
 * It uses a left recursive grammar for expressions.
 */
public class Antlr4LeftRecursiveGrammarCalculator implements Antlr4Calculator {

	@Override
	public Map<String, Double> evaluate(String statement) {
		LeftRecursiveCalcLexer lexer = new LeftRecursiveCalcLexer(CharStreams.fromString(statement));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LeftRecursiveCalcParser parser = new LeftRecursiveCalcParser(tokens);
		parser.setErrorHandler(new BailErrorStrategy());
		ParseTree tree = parser.statement();
		return new StatementEvaluator(tree).getVariables();
	}

	/**
	 * Evaluates statement to a map of values
	 */
	class StatementEvaluator extends LeftRecursiveCalcBaseVisitor<Map<String, Double>> {

		private final Map<String, Double> variables = new LinkedHashMap<>();
		private String currentVariable = null;

		public StatementEvaluator(ParseTree tree) {
			visit(tree);
		}

		public Map<String, Double> getVariables() {
			return variables;
		}

		@Override
		public Map<String, Double> visitStatement(StatementContext ctx) {
			for (int i = 0; i < ctx.expression().size(); i++) {
				visit(ctx.variable(i));
				double result = new ExpressionEvaluator(variables).visit(ctx.expression(i));
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
	class ExpressionEvaluator extends LeftRecursiveCalcBaseVisitor<Double> {

		private final Map<String, Double> variables;

		public ExpressionEvaluator(Map<String, Double> variables) {
			this.variables = variables;
		}

		@Override
		public Double visitOperation(OperationContext ctx) {
			double lhs = visit(ctx.lhs);
			double rhs = visit(ctx.rhs);
			String operator = ctx.op.getText();
			if ("+".equals(operator)) {
				return lhs + rhs;
			} else if ("-".equals(operator)) {
				return lhs - rhs;
			} else if ("*".equals(operator)) {
				return lhs * rhs;
			} else if ("/".equals(operator)) {
				return lhs / rhs;
			} else {
				throw new IllegalArgumentException("Unsupported operation: " + operator);
			}
		}

		@Override
		public Double visitNestedExpression(NestedExpressionContext ctx) {
			return visit(ctx.expression());
		}

		@Override
		public Double visitNumberTerm(NumberTermContext ctx) {
			return Double.valueOf(ctx.getText());
		}

		@Override
		public Double visitVariableTerm(VariableTermContext ctx) {
			String variable = ctx.getText();
			if (!variables.containsKey(variable)) {
				throw new IllegalArgumentException("Undefined variable: " + variable);
			}
			return variables.get(variable);
		}
	}
}

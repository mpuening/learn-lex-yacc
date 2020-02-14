package io.github.learnlexyacc.jparsec;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jparsec.OperatorTable;
import org.jparsec.Parser;
import org.jparsec.Parsers;
import org.jparsec.Scanners;
import org.jparsec.Terminals;

/**
 * This is a simple POJO class that evaluates an expression statement.
 */
public class JParsecCalculator {

	public Map<String, Double> evaluate(String statement) {
		State state = new State();
		Parser<?> evaluator = statement(state).from(TOKENIZER, IGNORED);
		evaluator.parse(statement);
		return state.variables;
	}

	class State {
		final Map<String, Double> variables = new LinkedHashMap<>();
		String currentVariable = null;
	}

	/**
	 * Statement
	 */
	private static Parser<?> statement(State state) {
		return Parsers.sequence(

				Terminals.identifier().map(variable -> {
					state.currentVariable = variable;
					return state.currentVariable;
				}),

				term("="),

				expression(state)).map(result -> {
					state.variables.put(state.currentVariable, result);
					return result;
				}).next(term(";")).many();
	}

	/**
	 * Expression
	 */
	private static Parser<Double> expression(State state) {
		Parser.Reference<Double> ref = Parser.newReference();

		Parser<Double> variable = variable(state);
		Parser<Double> NUMBER = Terminals.DecimalLiteral.PARSER.map(Double::valueOf);
		Parser<Double> term = ref.lazy()

				.between(term("("), term(")"))

				.or(variable)

				.or(NUMBER);

		Parser<Double> expression = new OperatorTable<Double>()

				// Infix left associative operators with precedence
				.infixl(operator("+", (lhs, rhs) -> lhs + rhs), 100)

				.infixl(operator("-", (lhs, rhs) -> lhs - rhs), 100)

				.infixl(operator("*", (lhs, rhs) -> lhs * rhs), 200)

				.infixl(operator("/", (lhs, rhs) -> lhs / rhs), 200)

				// Negate value
				.prefix(operator("-", value -> -value), 300)

				.build(term);
		ref.set(expression);
		return expression;
	}

	/**
	 * Variable
	 */
	private static Parser<Double> variable(State state) {
		return Terminals.identifier().map(variable -> {
			if (!state.variables.containsKey(variable)) {
				throw new IllegalArgumentException("Undefined variable: " + variable);
			}
			return state.variables.get(variable);
		});
	}

	private static final Parser<Void> IGNORED = Scanners.WHITESPACES.skipMany();

	private static final Terminals OPERATORS = Terminals.operators("=", "+", "-", "*", "/", "(", ")", ";");

	private static <T> Parser<T> operator(String name, T value) {
		return term(name).retn(value);
	}

	private static Parser<?> term(String... names) {
		return OPERATORS.token(names);
	}

	/**
	 * Add tokenizer for each type of item to find in the statement
	 */
	private static final Parser<?> TOKENIZER = Parsers.or(Terminals.Identifier.TOKENIZER,
			Terminals.DecimalLiteral.TOKENIZER, OPERATORS.tokenizer());
}

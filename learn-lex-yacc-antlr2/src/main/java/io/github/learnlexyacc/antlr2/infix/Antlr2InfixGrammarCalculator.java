package io.github.learnlexyacc.antlr2.infix;

import java.io.ByteArrayInputStream;
import java.util.Map;

import io.github.learnlexyacc.antlr2.Antlr2Calculator;
import io.github.learnlexyacc.antlr2.tail.Antlr2TailGrammarCalculator.Evaluator;

/**
 * This is a simple POJO class that evaluates an expression statement.
 *
 * It uses the infix grammar.
 */
public class Antlr2InfixGrammarCalculator implements Antlr2Calculator {

	public Map<String, Double> evaluate(String statement) throws Exception {
		CalcLexer lexer = new CalcLexer(new ByteArrayInputStream(statement.getBytes()));
		CalcInfixParser parser = new CalcInfixParser(lexer);
		parser.stmt();
		// Evaluation is the same as the tail recursion impl
		Evaluator evaluator = new Evaluator(parser.getAST());
		return evaluator.getResult();
	}
}

package io.github.learnlexyacc.antlr2.incorrect;

import java.io.ByteArrayInputStream;

/**
 * This is a simple POJO class that evaluates an expression statement.
 *
 * It uses an incorrect tree, and can produce wrong answers.
 */
public class Antlr2IncorrectParserCalculator {

	public double evaluate(String statement) throws Exception {
		CalcLexer lexer = new CalcLexer(new ByteArrayInputStream(statement.getBytes()));
		CalcIncorrectParser parser = new CalcIncorrectParser(lexer);
		parser.stmt();

		CalcIncorrectTreeWalker treeWalker = new CalcIncorrectTreeWalker();
		return treeWalker.stmt(parser.getAST());
	}

}

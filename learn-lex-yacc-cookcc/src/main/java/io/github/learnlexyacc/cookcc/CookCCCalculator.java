package io.github.learnlexyacc.cookcc;

import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * This is a simple POJO class that evaluates an expression statement.
 *
 * It uses the cookcc parser.
 */
public class CookCCCalculator {

	public Map<String, Double> evaluate(String statement) throws Exception {
		ByteArrayInputStream input = new ByteArrayInputStream(statement.getBytes());

		CookCCCalculatorParser parser = new CookCCCalculatorParser();
		parser.setInput(input);
		int result = parser.yyParse();
		if (result != 0) {
			throw new IllegalArgumentException("Unable to parse input");
		}
		return parser.getVariables();
	};

}
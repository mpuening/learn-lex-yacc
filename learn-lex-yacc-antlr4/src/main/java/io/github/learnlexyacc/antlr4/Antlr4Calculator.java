package io.github.learnlexyacc.antlr4;

import java.util.Map;

/**
 * This is an interface which the calculators implement.
 */
public interface Antlr4Calculator {

	Map<String, Double> evaluate(String statement);

}

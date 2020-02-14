package io.github.learnlexyacc.antlr2;

import java.util.Map;

/**
 * This is an interface which the calculators implement.
 */
public interface Antlr2Calculator {

	Map<String, Double> evaluate(String statement) throws Exception;

}

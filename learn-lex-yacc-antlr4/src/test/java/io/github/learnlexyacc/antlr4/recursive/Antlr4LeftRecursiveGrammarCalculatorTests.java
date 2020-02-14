package io.github.learnlexyacc.antlr4.recursive;

import org.junit.jupiter.api.BeforeEach;

import io.github.learnlexyacc.antlr4.AbstractAntlr4CalculatorTests;

public class Antlr4LeftRecursiveGrammarCalculatorTests extends AbstractAntlr4CalculatorTests {

	@BeforeEach
	public void setup() {
		this.calculator = new Antlr4LeftRecursiveGrammarCalculator();
	}
}

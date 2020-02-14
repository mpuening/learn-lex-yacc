package io.github.learnlexyacc.antlr4.classic;

import org.junit.jupiter.api.BeforeEach;

import io.github.learnlexyacc.antlr4.AbstractAntlr4CalculatorTests;

public class Antlr4ClassicGrammarCalculatorTests extends AbstractAntlr4CalculatorTests {

	@BeforeEach
	public void setup() {
		this.calculator = new Antlr4ClassicGrammarCalculator();
	}
}

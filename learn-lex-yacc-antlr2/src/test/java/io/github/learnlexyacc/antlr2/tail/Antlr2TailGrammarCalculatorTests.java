package io.github.learnlexyacc.antlr2.tail;

import org.junit.jupiter.api.BeforeEach;

import io.github.learnlexyacc.antlr2.AbstractAntlr2CalculatorTests;

public class Antlr2TailGrammarCalculatorTests extends AbstractAntlr2CalculatorTests {

	@BeforeEach
	public void setup() {
		this.calculator = new Antlr2TailGrammarCalculator();
	}
}

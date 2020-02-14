package io.github.learnlexyacc.antlr2.incorrect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * These tests show the limitations of attempting to infix operations into a
 * parser that implements LL parsing. Notice that it can do many test cases but
 * starts failing when concatenating many operations together and using right
 * associative operations rather than left associative.
 */
public class Antlr2IncorrectParserCalculatorTests {

	protected Antlr2IncorrectParserCalculator calculator = new Antlr2IncorrectParserCalculator();
	protected double delta = 0.001;

	@Test
	public void testInvalidInput() throws Exception {
		assertThrows(Exception.class, () -> {
			calculator.evaluate(null);
		});
		assertThrows(Exception.class, () -> {
			calculator.evaluate("abc");
		});
	}

	@Test
	public void testSingleTerms() throws Exception {
		assertEquals(1.0, calculator.evaluate("1;"), delta);
		assertEquals(10.0, calculator.evaluate("10;"), delta);
		assertEquals(1.0, calculator.evaluate("(1);"), delta);
		assertEquals(10.0, calculator.evaluate("(10);"), delta);
		assertEquals(10.012, calculator.evaluate("(10.01200);"), delta);
	}

	@Test
	public void testAddition() throws Exception {
		assertEquals(3.0, calculator.evaluate("1+2;"), delta);
		assertEquals(3.0, calculator.evaluate("2+1;"), delta);
		assertEquals(6.0, calculator.evaluate("1+(2+3);"), delta);
		assertEquals(6.0, calculator.evaluate("(1+2)+3;"), delta);
		assertEquals(3.2, calculator.evaluate("1.1+2.1;"), delta);
		assertEquals(3.2, calculator.evaluate("2.1+1.1;"), delta);
		assertEquals(6.3, calculator.evaluate("1.1+(2.1+3.1);"), delta);
		assertEquals(6.3, calculator.evaluate("(1.1+2.1)+3.1;"), delta);
		assertEquals(15.0, calculator.evaluate("1+2+3+4+5;"), delta);
	}

	@Test
	public void testSubtraction() throws Exception {
		assertEquals(-1.0, calculator.evaluate("1-2;"), delta);
		assertEquals(1.0, calculator.evaluate("2-1;"), delta);
		assertEquals(2.0, calculator.evaluate("1-(2-3);"), delta);
		assertEquals(-4.0, calculator.evaluate("(1-2)-3;"), delta);
		assertEquals(-1.0, calculator.evaluate("1.1-2.1;"), delta);
		assertEquals(1.0, calculator.evaluate("2.1-1.1;"), delta);
		assertEquals(2.1, calculator.evaluate("1.1-(2.1-3.1);"), delta);
		assertEquals(-4.1, calculator.evaluate("(1.1-2.1)-3.1;"), delta);
		assertThrows(Error.class, () -> {
			assertEquals(-13.0, calculator.evaluate("1-2-3-4-5;"), delta);
		});
	}

	@Test
	public void testMultiplication() throws Exception {
		assertEquals(2.0, calculator.evaluate("1*2;"), delta);
		assertEquals(2.0, calculator.evaluate("2*1;"), delta);
		assertEquals(6.0, calculator.evaluate("1*(2*3);"), delta);
		assertEquals(6.0, calculator.evaluate("(1*2)*3;"), delta);
		assertEquals(2.31, calculator.evaluate("1.1*2.1;"), delta);
		assertEquals(2.31, calculator.evaluate("2.1*1.1;"), delta);
		assertEquals(7.161, calculator.evaluate("1.1*(2.1*3.1);"), delta);
		assertEquals(7.161, calculator.evaluate("(1.1*2.1)*3.1;"), delta);
		assertEquals(120.0, calculator.evaluate("1*2*3*4*5;"), delta);
	}

	@Test
	public void testDivision() throws Exception {
		assertEquals(0.5, calculator.evaluate("1/2;"), delta);
		assertEquals(2.0, calculator.evaluate("2/1;"), delta);
		assertEquals(1.5, calculator.evaluate("1/(2/3);"), delta);
		assertEquals(0.16666666666, calculator.evaluate("(1/2)/3;"), delta);
		assertEquals(0.52380952381, calculator.evaluate("1.1/2.1;"), delta);
		assertEquals(1.90909090909, calculator.evaluate("2.1/1.1;"), delta);
		assertEquals(1.62380952381, calculator.evaluate("1.1/(2.1/3.1);"), delta);
		assertEquals(0.16897081413, calculator.evaluate("(1.1/2.1)/3.1;"), delta);
		assertTrue(Double.isInfinite(calculator.evaluate("1/0;")));
		assertThrows(Error.class, () -> {
			// Oops!
			assertEquals(0.00833333333, calculator.evaluate("1/2/3/4/5;"), delta);
		});
	}

	@Test
	public void testMultipleTermsIsBroken() throws Exception {
		assertEquals(15.0, calculator.evaluate("1+2+3+4+5;"), delta);
		assertThrows(Error.class, () -> {
			// Oops!
			assertEquals(-13.0, calculator.evaluate("1-2-3-4-5;"), delta);
		});
		assertEquals(120.0, calculator.evaluate("1*2*3*4*5;"), delta);
		assertThrows(Error.class, () -> {
			// Oops!
			assertEquals(0.00833333333, calculator.evaluate("1/2/3/4/5;"), delta);
		});
		assertThrows(Error.class, () -> {
			// Wrong value
			assertEquals(15.0, calculator.evaluate("1+2+3-4+5*3-4/2;"));
		});
		assertThrows(Error.class, () -> {
			// Wrong value
			assertEquals(-4.0, calculator.evaluate("1*2+3-4-5+4/2-2;"));
		});
	}

	@Test
	public void testOrderOfOperationsIsBroken() throws Exception {
		assertEquals(17.0, calculator.evaluate("15+5-3;"), delta);
		assertEquals(30.0, calculator.evaluate("15+5*3;"), delta);
		assertEquals(16.66666666667, calculator.evaluate("15+5/3;"), delta);

		assertThrows(Error.class, () -> {
			// Oops! Does addition before subtraction
			assertEquals(13.0, calculator.evaluate("15-5+3;"), delta);
		});
		assertEquals(0.0, calculator.evaluate("15-5*3;"), delta);
		assertEquals(13.3333333333, calculator.evaluate("15-5/3;"), delta);

		assertEquals(78.0, calculator.evaluate("15*5+3;"), delta);
		assertEquals(72.0, calculator.evaluate("15*5-3;"), delta);
		assertEquals(25.0, calculator.evaluate("15*5/3;"), delta);

		assertEquals(6.0, calculator.evaluate("15/5+3;"), delta);
		assertEquals(0.0, calculator.evaluate("15/5-3;"), delta);
		assertThrows(Error.class, () -> {
			// Oops! Does multiplication before division
			assertEquals(9.0, calculator.evaluate("15/5*3;"), delta);
		});
	}
}

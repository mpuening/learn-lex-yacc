package io.github.learnlexyacc.cookcc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class CookCCCalculatorTest {

	protected CookCCCalculator calculator = new CookCCCalculator();
	protected double delta = 0.001;

	@Test
	public void testInvalidInput() {
		assertThrows(Exception.class, () -> {
			calculator.evaluate(null);
		});
		assertThrows(Exception.class, () -> {
			calculator.evaluate("");
		});
		assertThrows(Exception.class, () -> {
			calculator.evaluate("2");
		});
		assertThrows(Exception.class, () -> {
			calculator.evaluate("a = abc");
		});
		assertThrows(Exception.class, () -> {
			calculator.evaluate("a = 2");
		});
	}

	@Test
	public void testSingleTerms() throws Exception {
		assertEquals(1.0, calculator.evaluate("a=1;").get("a"), delta);
		assertEquals(10.0, calculator.evaluate("a=10;").get("a"), delta);
		assertEquals(1.0, calculator.evaluate("a=(1);").get("a"), delta);
		assertEquals(10.0, calculator.evaluate("a=(10);").get("a"), delta);
		assertEquals(10.012, calculator.evaluate("a=(10.01200);").get("a"), delta);
	}

	@Test
	public void testAddition() throws Exception {
		assertEquals(3.0, calculator.evaluate("a = 1+2;").get("a"), delta);
		assertEquals(3.0, calculator.evaluate("a = 2+1;").get("a"), delta);
		assertEquals(6.0, calculator.evaluate("a = 1+(2+3);").get("a"), delta);
		assertEquals(6.0, calculator.evaluate("a = (1+2)+3;").get("a"), delta);
		assertEquals(3.2, calculator.evaluate("a = 1.1+2.1;").get("a"), delta);
		assertEquals(3.2, calculator.evaluate("a = 2.1+1.1;").get("a"), delta);
		assertEquals(6.3, calculator.evaluate("a = 1.1+(2.1+3.1);").get("a"), delta);
		assertEquals(6.3, calculator.evaluate("a = (1.1+2.1)+3.1;").get("a"), delta);
		assertEquals(15.0, calculator.evaluate("a = 1+2+3+4+5;").get("a"), delta);
	}

	@Test
	public void testSubtraction() throws Exception {
		assertEquals(-1.0, calculator.evaluate("a = 1-2;").get("a"), delta);
		assertEquals(1.0, calculator.evaluate("a = 2-1;").get("a"), delta);
		assertEquals(2.0, calculator.evaluate("a = 1-(2-3);").get("a"), delta);
		assertEquals(-4.0, calculator.evaluate("a = (1-2)-3;").get("a"), delta);
		assertEquals(-1.0, calculator.evaluate("a = 1.1-2.1;").get("a"), delta);
		assertEquals(1.0, calculator.evaluate("a = 2.1-1.1;").get("a"), delta);
		assertEquals(2.1, calculator.evaluate("a = 1.1-(2.1-3.1);").get("a"), delta);
		assertEquals(-4.1, calculator.evaluate("a = (1.1-2.1)-3.1;").get("a"), delta);
		assertEquals(-13.0, calculator.evaluate("a = 1-2-3-4-5;").get("a"), delta);
	}

	@Test
	public void testMultiplication() throws Exception {
		assertEquals(2.0, calculator.evaluate("a = 1*2;").get("a"), delta);
		assertEquals(2.0, calculator.evaluate("a = 2*1;").get("a"), delta);
		assertEquals(6.0, calculator.evaluate("a = 1*(2*3);").get("a"), delta);
		assertEquals(6.0, calculator.evaluate("a = (1*2)*3;").get("a"), delta);
		assertEquals(2.31, calculator.evaluate("a = 1.1*2.1;").get("a"), delta);
		assertEquals(2.31, calculator.evaluate("a = 2.1*1.1;").get("a"), delta);
		assertEquals(7.161, calculator.evaluate("a = 1.1*(2.1*3.1);").get("a"), delta);
		assertEquals(7.161, calculator.evaluate("a = (1.1*2.1)*3.1;").get("a"), delta);
		assertEquals(120.0, calculator.evaluate("a = 1*2*3*4*5;").get("a"), delta);
	}

	@Test
	public void testDivision() throws Exception {
		assertEquals(0.5, calculator.evaluate("a = 1/2;").get("a"), delta);
		assertEquals(2.0, calculator.evaluate("a = 2/1;").get("a"), delta);
		assertEquals(1.5, calculator.evaluate("a = 1/(2/3);").get("a"), delta);
		assertEquals(0.16666666666, calculator.evaluate("a = (1/2)/3;").get("a"), delta);
		assertEquals(0.52380952381, calculator.evaluate("a = 1.1/2.1;").get("a"), delta);
		assertEquals(1.90909090909, calculator.evaluate("a = 2.1/1.1;").get("a"), delta);
		assertEquals(1.62380952381, calculator.evaluate("a = 1.1/(2.1/3.1);").get("a"), delta);
		assertEquals(0.16897081413, calculator.evaluate("a = (1.1/2.1)/3.1;").get("a"), delta);
		assertTrue(Double.isInfinite(calculator.evaluate("a = 1/0;").get("a")));
		assertEquals(0.00833333333, calculator.evaluate("a = 1/2/3/4/5;").get("a"), delta);
	}

	@Test
	public void testOrderOfOperations() throws Exception {
		assertEquals(17.0, calculator.evaluate("a = 15+5-3;").get("a"), delta);
		assertEquals(30.0, calculator.evaluate("a = 15+5*3;").get("a"), delta);
		assertEquals(16.66666666667, calculator.evaluate("a = 15+5/3;").get("a"), delta);

		assertEquals(13.0, calculator.evaluate("a = 15-5+3;").get("a"), delta);
		assertEquals(0.0, calculator.evaluate("a = 15-5*3;").get("a"), delta);
		assertEquals(13.3333333333, calculator.evaluate("a = 15-5/3;").get("a"), delta);

		assertEquals(78.0, calculator.evaluate("a = 15*5+3;").get("a"), delta);
		assertEquals(72.0, calculator.evaluate("a = 15*5-3;").get("a"), delta);
		assertEquals(25.0, calculator.evaluate("a = 15*5/3;").get("a"), delta);

		assertEquals(6.0, calculator.evaluate("a = 15/5+3;").get("a"), delta);
		assertEquals(0.0, calculator.evaluate("a = 15/5-3;").get("a"), delta);
		assertEquals(9.0, calculator.evaluate("a = 15/5*3;").get("a"), delta);
	}

	@Test
	public void testMixedOperations() throws Exception {
		assertEquals(454.0, calculator.evaluate("a = 15*2*(6*2+3)+4;").get("a"), delta);
		assertEquals(36.0, calculator.evaluate("a = (1+2)*3*4;").get("a"), delta);
		assertEquals(36.0, calculator.evaluate("a = (((1.0+2)))*((3.0*4));").get("a"), delta);
		assertEquals(5.0, calculator.evaluate("a = 1+2*3-4/2;").get("a"), delta);
		assertEquals(2.0, calculator.evaluate("a = 1*2+4/2-2;").get("a"), delta);
		assertEquals(15.0, calculator.evaluate("a = 1+2+3-4+5*3-4/2;").get("a"), delta);
		assertEquals(-4.0, calculator.evaluate("a = 1*2+3-4-5+4/2-2;").get("a"), delta);
	}

	@Test
	public void testVariableUsage() throws Exception {
		Map<String, Double> variables = calculator.evaluate("a = 5; b = 4; c = (a*b)+(a-b)-1.9;");
		assertEquals(5.0, variables.get("a"), delta);
		assertEquals(4.0, variables.get("b"), delta);
		assertEquals(19.1, variables.get("c"), delta);
		assertNull(variables.get("d"));

		assertThrows(IllegalArgumentException.class, () -> {
			calculator.evaluate("a = 2; b = c * d;");
		});
	}
}

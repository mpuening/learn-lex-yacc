package io.github.learnlexyacc.jflex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class JFlexCalculatorTests {

	@Test
	public void testPropertiesLexer() throws IOException {

		Reader reader = new StringReader(

				"# Test Last Property\n" +

						"a=b c d\n" +

						"epsilon\n" +

						" last : property  \n");
		PropertiesLexer lexer = new PropertiesLexer(reader);

		String lastKeyFound = null;
		String lastValueFound = null;
		int token = lexer.yylex();
		while (token != -1) {
			String text = lexer.yytext();
			if (token == TokenType.COMMENT) {
				assertEquals("# Test Last Property", text);
			} else if (token == TokenType.KEY) {
				lastKeyFound = text.trim();
			} else if (token == TokenType.VALUE) {
				lastValueFound = text.trim();
			}
			token = lexer.yylex();
		}
		assertEquals("last", lastKeyFound);
		assertEquals("property", lastValueFound);
	}

	@Test
	public void testEmptyValueWithSeparator() throws IOException {

		Reader reader = new StringReader(

				"# Test Empty Property\n" +

						"epsilon: \n");
		PropertiesLexer lexer = new PropertiesLexer(reader);

		String lastKeyFound = null;
		String lastValueFound = null;
		int token = lexer.yylex();
		while (token != -1) {
			String text = lexer.yytext();
			if (token == TokenType.COMMENT) {
				assertEquals("# Test Empty Property", text);
			} else if (token == TokenType.KEY) {
				lastKeyFound = text.trim();
			} else if (token == TokenType.VALUE) {
				lastValueFound = text.trim();
			}
			token = lexer.yylex();
		}
		assertEquals("epsilon", lastKeyFound);
		assertEquals(null, lastValueFound);
	}

	@Test
	public void testEmptyValue() throws IOException {

		Reader reader = new StringReader(

				"# Test Empty Property\n" +

						"epsilon\n");
		PropertiesLexer lexer = new PropertiesLexer(reader);

		String lastKeyFound = null;
		String lastValueFound = null;
		int token = lexer.yylex();
		while (token != -1) {
			String text = lexer.yytext();
			if (token == TokenType.COMMENT) {
				assertEquals("# Test Empty Property", text);
			} else if (token == TokenType.KEY) {
				lastKeyFound = text.trim();
			} else if (token == TokenType.VALUE) {
				lastValueFound = text.trim();
			}
			token = lexer.yylex();
		}
		assertEquals("epsilon", lastKeyFound);
		assertEquals(null, lastValueFound);
	}

	@Test
	public void testPropertiesLexerWithLineContinuation() throws IOException {

		Reader reader = new StringReader(

				"# Test Line Continuation\n" +

						"a=b c d\\\n" +

						" e f \n");
		PropertiesLexer lexer = new PropertiesLexer(reader);

		String lastKeyFound = null;
		String lastValueFound = null;
		int token = lexer.yylex();
		while (token != -1) {
			String text = lexer.yytext();
			if (token == TokenType.COMMENT) {
				assertEquals("# Test Line Continuation", text);
			} else if (token == TokenType.KEY) {
				lastKeyFound = text.trim();
			} else if (token == TokenType.VALUE) {
				lastValueFound = text.trim();
			}
			token = lexer.yylex();
		}
		assertEquals("a", lastKeyFound);
		assertEquals("b c d\\\n e f", lastValueFound);
	}

	@Test
	public void tesSingleProperty() throws IOException {

		Reader reader = new StringReader(

				"a\n");
		PropertiesLexer lexer = new PropertiesLexer(reader);

		String lastKeyFound = null;
		String lastValueFound = null;
		int token = lexer.yylex();
		while (token != -1) {
			String text = lexer.yytext();
			if (token == TokenType.KEY) {
				lastKeyFound = text.trim();
			} else if (token == TokenType.VALUE) {
				lastValueFound = text.trim();
			}
			token = lexer.yylex();
		}
		assertEquals("a", lastKeyFound);
		assertEquals(null, lastValueFound);
	}
}

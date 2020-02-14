// ==============================================
header {
    package io.github.learnlexyacc.antlr2.incorrect;
}

// ==============================================
class CalcLexer extends Lexer;

WS
	:	(' '
	|	'\t'
	|	'\n'
	|	'\r')
		{ _ttype = Token.SKIP; }
	;

LPAREN
	:	'('
	;

RPAREN
	:	')'
	;

MULTIPLY
	:	'*'
	;

DIVIDE
	:	'/'
	;

ADD
	:	'+'
	;

SUBTRACT
	:	'-'
	;

NUMBER
	:	('0' .. '9') + ('.' ('0' .. '9') +)?
	;

SEMI
	:	';'
	;

// ==============================================
//
// For more documentation, see https://www.antlr2.org/doc/trees.html
//

//
// This grammar is incorrect in that it pivots(^) on the operations and
// causes the order of operations to become right associative.
//
class CalcIncorrectParser extends Parser;
options {
	buildAST = true;
}

stmt
	:	expr SEMI
	;
  
expr
	:	sum
	;

sum
	:	( product ADD^ sum ) => addition_expr
	|	( product SUBTRACT^ sum ) => subtraction_expr
	|	product
	;

addition_expr
	:	product ADD^ sum
	;

subtraction_expr
	:	product SUBTRACT^ sum
	;

product
	:	( term MULTIPLY^ product ) => multiplication_expr
	|	( term DIVIDE^ product ) => division_expr
	|	term
	;

multiplication_expr
	:	term MULTIPLY^ product
	;

division_expr
	:	term DIVIDE^ product
	;

term
	:	NUMBER
	|	LPAREN! expr RPAREN!
	;

class CalcIncorrectTreeWalker extends TreeParser;

stmt returns [double result] {
		double e;
		result=0;
	}
	:	e=expr SEMI						{result = e;}       
	;

expr returns [double result] {
		double lhs,rhs;
		result=0;
	}
	:	LPAREN lhs=expr RPAREN			{result = lhs;}
	|	#(ADD lhs=expr rhs=expr)		{result = lhs + rhs;}
	|	#(SUBTRACT lhs=expr rhs=expr)	{result = lhs - rhs;}
	|	#(MULTIPLY lhs=expr rhs=expr)	{result = lhs * rhs;}
	|	#(DIVIDE lhs=expr rhs=expr)		{result = lhs / rhs;}
	|	n:NUMBER						{result = (double)Double.parseDouble(n.getText());}
	;

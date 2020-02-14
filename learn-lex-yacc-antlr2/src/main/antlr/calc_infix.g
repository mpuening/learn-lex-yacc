// ==============================================
header {
    package io.github.learnlexyacc.antlr2.infix;
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

ASSIGNMENT
	:	'='
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

VARIABLE
	:	('a' .. 'z')+
	;

SEMI
	:	';'
	;

// ==============================================
//
// For more documentation, see https://www.antlr2.org/doc/trees.html
//

//
// This grammar shows how the operations are shown infixed in the rules.
// Additional rules are needed to resolve the ambiguity.
//
class CalcInfixParser extends Parser;
options {
	buildAST = true;
}

stmt
	:	(variable ASSIGNMENT expr SEMI)+
	;

variable
	:	VARIABLE;

expr
	:	sum
	;

sum
	:	( product ADD sum ) => addition_expr
	|	( product SUBTRACT sum ) => subtraction_expr
	|	product
	;

addition_expr
	:	product ADD sum
	;

subtraction_expr
	:	product SUBTRACT sum
	;

product
	:	( term MULTIPLY product ) => multiplication_expr
	|	( term DIVIDE product ) => division_expr
	|	term
	;

multiplication_expr
	:	term MULTIPLY product
	;

division_expr
	:	term DIVIDE product
	;

term
	:	NUMBER
	|	VARIABLE
	|	LPAREN expr RPAREN
	;

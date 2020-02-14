// ==============================================
header {
    package io.github.learnlexyacc.antlr2.tail;
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
// This grammar shows how the operations are shown as tail recursion.
//
class CalcTailParser extends Parser;
options {
    buildAST = true;
}

stmt
	:	(variable ASSIGNMENT expr SEMI)+
	;

variable
	:	VARIABLE;

expr
  	:	product sum_tail
  	;

sum_tail
	:	ADD product sum_tail
	|	SUBTRACT product sum_tail
	|	// epsilon
	;

product
	:	term product_tail
	;

product_tail
	:	MULTIPLY term product_tail
	|	DIVIDE term product_tail
	|	// epsilon
	;

term
	:	NUMBER
	|	VARIABLE
	|	LPAREN expr RPAREN
	;

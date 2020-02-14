grammar ClassicCalc;

statement
	:	(variable ASSIGNMENT expression SEMI)+
	;

variable
	:	VARIABLE
	;

expression
	:	product (addOperation product)*
	;

addOperation
	:	ADD
	|	SUBTRACT
	;

product
	:	term (productOperation term)*
	;

productOperation
	:	MULTIPLY
	|	DIVIDE
	;

term
	:	NUMBER
	|	VARIABLE
	|	LPAREN expression RPAREN
	;

SEMI
	:	';'
	;

LPAREN
	:	'('
	;

RPAREN
	:	')'
	;

ADD
	:	'+'
	;

SUBTRACT
	:	'-'
	;

MULTIPLY
	:	'*'
	;

DIVIDE
	:	'/'
	;

ASSIGNMENT
	:	'='
	;

NUMBER
	:	('0' .. '9') + ('.' ('0' .. '9') +)?
	;

VARIABLE
	:	('a' .. 'z')+
	;

WS
	:	[ \r\n\t] + -> skip
	;
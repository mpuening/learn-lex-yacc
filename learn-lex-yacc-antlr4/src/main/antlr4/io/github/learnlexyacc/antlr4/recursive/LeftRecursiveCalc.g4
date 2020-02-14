grammar LeftRecursiveCalc;

statement
	:	(variable '=' expression ';')+
	;

variable
	:	VARIABLE
	;

expression
	:	lhs=expression op=('*'|'/') rhs=expression	#operation
	|	lhs=expression op=('+'|'-') rhs=expression	#operation
	|	'(' expression ')'							#nestedExpression
	|	term=variable								#variableTerm
	|	term=NUMBER									#numberTerm
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
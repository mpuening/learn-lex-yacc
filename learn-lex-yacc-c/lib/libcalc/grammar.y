%{

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "calc.h"

%}

%token VARIABLE ASSIGNMENT NUMBER ADD SUBTRACT MULTIPLY DIVIDE LPAREN RPAREN SEMI

%union { 
	double dval;
	char  *sval;
} 

%type <sval> variable
%type <dval> number
%type <dval> expression

%left ADD SUBTRACT
%left MULTIPLY DIVIDE

%start program

%%

program
	:	statements
	;

statements
	:	/* epsilon */
	|	statement statements
	;

statement
	:	variable ASSIGNMENT expression SEMI {
			calc_assign_variable($1, $3);
		}
	|	error SEMI {
			yyerrok;
		}
	;

expression
	:	expression MULTIPLY expression {
			$$ = $1 * $3;
		}
	|	expression DIVIDE expression {
			$$ = $1 / $3;
		}
	|	expression ADD expression {
			$$ = $1 + $3;
		}
	|	expression SUBTRACT expression {
			$$ = $1 - $3;
		}
	|	number {
			$$ = $1;
		}
	|	variable {
			$$ = calc_find_value($1);
		}
	|	LPAREN expression RPAREN {
			$$ = $2;
		}
	;

variable
	:	VARIABLE {
			// I think this leaks variable names
			$$ = strdup(yytext);
		}
	;

number
	:	NUMBER {
			$$ = calc_parse_string(yytext);
		}
	;

%%


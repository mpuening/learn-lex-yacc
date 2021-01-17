%{
 
#include <stdio.h>
#include "grammar.h"

%}

%%

[ \n\t\r]+	;

"="			{
				return(ASSIGNMENT);
			}

";"			{
				return(SEMI);
			}

"("			{
				return(LPAREN);
			}

")"			{
				return(RPAREN);
			}

"+"			{
				return(ADD);
			}

"-"			{
				return(SUBTRACT);
			}

"*"			{
				return(MULTIPLY);
			}

"/"			{
				return(DIVIDE);
			}

[a-z]+		{
				return(VARIABLE);
			}

[0-9]+(\.[0-9]+)? {
				return(NUMBER);
 			}

.			{
				return(yytext[0]);
			}

%%

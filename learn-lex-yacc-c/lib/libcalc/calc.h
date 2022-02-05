#ifndef _CALC_H_
#define _CALC_H_

#include "fields.h"
#include "jrb.h"

extern char *yytext;
extern int yylex(void);
extern int yyparse(void);
extern void yyerror(char *s);

extern void   calc_initialize();
extern int    calc_parse();
extern JRB    calc_variables();
extern double calc_parse_string(char *number);
extern double calc_find_value(char *variable);
extern void   calc_assign_variable(char *variable, double value);
extern void   calc_print_variables();

#endif

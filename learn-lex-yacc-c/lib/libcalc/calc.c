#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "calc.h"

/**
 * Basic Yacc functions
 */

int yywrap() {
	return(1);
}

void yyerror(char *s) {
	fprintf(stderr, "%s\n",s);
}

/**
 * Calculator methods
 */

JRB __variables;

void calc_initialize() {
	__variables = make_jrb();
}

int calc_parse() {
	return yyparse();
}

JRB calc_variables() {
	return __variables;
}

double calc_parse_string(char *number) {
	char *eptr;
	double result;
	result = strtod(yytext, &eptr);
	return result;
}

double calc_find_value(char *variable) {
	double result;
	// I think this leaks variable names
	JRB v;
	v = jrb_find_str(calc_variables(), variable);
	if (v == NULL) {
		char *error = "Undefined variable: ";
		char *message;
		message = malloc(strlen(error) + strlen(variable) + 1);
		strcpy(stpcpy(message, error), variable);
		yyerror(message);
		free(message);
		// Using zero as a value isn't very good
		result = 0.0;
	} else {
		result = jval_d(v->val);
	}
	return result;
}

void calc_assign_variable(char *variable, double value) {
	JRB previous;
	previous = jrb_find_str(calc_variables(), variable);
	if (previous != NULL) {
		// I think this leaks values
		previous->val = new_jval_d(value);
	} else {
		jrb_insert_str(calc_variables(), variable, new_jval_d(value));
	}
}

void calc_print_variables() {
	JRB iterator;
	jrb_traverse(iterator, calc_variables()) {
		printf("%s = %lf\n", jval_s(iterator->key), jval_d(iterator->val));
	}
}

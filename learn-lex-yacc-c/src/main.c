#include <stdio.h>
#include <stdlib.h>
#include "calc.h"

int main(int argc, char *argv[]) {
	calc_initialize();

	// yyin = fopen(argv[1],"r");

	calc_parse();

	// fclose(yyin);

	calc_print_variables();

	exit(0);
}

AC_INIT([calc], [1.0], [bug-automake@gnu.org])
AM_INIT_AUTOMAKE([-Wall -Werror foreign])

AC_PROG_CC

AC_PROG_LEX([noyywrap])
AC_PROG_YACC

AC_PROG_RANLIB
AM_PROG_AR

AC_CONFIG_HEADERS([config.h])
AC_CONFIG_FILES([
 Makefile
 lib/libfdr/Makefile
 lib/libcalc/Makefile
 src/Makefile
])

AC_PROG_AWK
AC_REQUIRE_AUX_FILE([tap-driver.sh])

AC_OUTPUT


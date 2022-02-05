bin_PROGRAMS = calc
calc_SOURCES = main.c

calc_CFLAGS = -I../lib/libcalc -I../lib/libfdr
calc_LDADD = ../lib/libcalc/libcalc.a ../lib/libfdr/libfdr.a $(EXTRALIBS)


TESTS_ENVIRONMENT =
TEST_EXTENSIONS = .test

TEST_LOG_COMPILE = $(SHELL)
TEST_LOG_DRIVER = env AM_TAP_AWK='$(AWK)' $(SHELL) $(top_srcdir)/tap-driver.sh

TESTS = calc.test

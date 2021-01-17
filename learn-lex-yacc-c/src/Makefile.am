bin_PROGRAMS = calc
calc_SOURCES = calc.c

calc_CFLAGS = -I../lib/libfdr -I../lib/libcalc
calc_LDADD = ../lib/libfdr/libfdr.a ../lib/libcalc/libcalc.a $(EXTRALIBS)


TESTS_ENVIRONMENT =
TEST_EXTENSIONS = .test

TEST_LOG_COMPILE = $(SHELL)
TEST_LOG_DRIVER = env AM_TAP_AWK='$(AWK)' $(SHELL) $(top_srcdir)/tap-driver.sh

TESTS = calc.test

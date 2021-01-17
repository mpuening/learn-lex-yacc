#!/bin/sh

#
# Removes autoreconf generated files
#

rm -rf .autotools
rm -rf install-sh
rm -rf missing
rm -rf compile
rm -rf depcomp
rm -rf autom4te.cache
rm -rf aux-dist
rm -rf m4
rm -rf aclocal.m4
rm -rf stamp-h1
rm -rf libtool
rm -rf ar-lib
rm -rf tap-driver.sh

rm -rf Makefile
rm -rf Makefile.in
rm -rf config.h.in
rm -rf config.h
rm -rf config.log
rm -rf config.status
rm -rf configure

rm -rf src/Makefile
rm -rf src/Makefile.in
rm -rf src/.deps
rm -rf src/.libs
rm -rf src/*.lo
rm -rf src/*.la

rm -rf lib/libcalc/Makefile
rm -rf lib/libcalc/Makefile.in
rm -rf lib/libcalc/.deps
rm -rf lib/libcalc/.libs
rm -rf lib/libcalc/*.lo
rm -rf lib/libcalc/*.la

rm -rf lib/libfdr/Makefile
rm -rf lib/libfdr/Makefile.in
rm -rf lib/libfdr/.deps
rm -rf lib/libfdr/.libs
rm -rf lib/libfdr/*.lo
rm -rf lib/libfdr/*.la


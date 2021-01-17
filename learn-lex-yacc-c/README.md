Yacc (Bison) and Lex (Flex)
===========================

Flex and Bison are the GNU versions of Lex and Yacc. Lex and Yacc are essentially the
orginal lexers and parser creators. Bison supports LALR grammars and can easily support
complex grammars.

The code in this project is a C Program that implements the simple calculator.
The lexer and grammar rules are implemented in a library here:

* `lib/libcalc/lexer.l`
* `lib/libcalc/grammar.y`

The bulk of the code is in `grammar.y`.

The main program is located at `src/calc.c` In C-style convention, it parses input 
from `stdin`. This program makes use the `libfdr` library which supplies support for
generic maps and lists to the C language.

## Build Requirements

To build the Makefiles for this project, you need the `Autotools` installed.

On a Mac, this is the brew command:

```
brew install autoconf automake libtool
```

## Running Autoreconf

For the first time, run this command:

```
autoreconf --install
```

Afterward, say after adding new source file, run just this command:

```
autoreconf
```

## Building the App

To build the app, use these commands:

```
./configure
make
```

To run the test cases:

```
make check
```

To clean the project:

```
make clean
```

If you want to clean up all the AutoConf files, run this command:

```
./ac-cleanup.sh
```

## Running the App

To run the app, run this command:

```
./src/calc
```


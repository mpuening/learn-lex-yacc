Learn Lex and Yacc
===================

I named this project after Lex and Yacc in honor of the classic UNIX tools to build 
parsers. Obviously this is a Java project, so the UNIX tools are not used, but rather 
I use several Java parsing libraries to build simple example calculators.

Parsing input is a valuable skill to know, ranging from writing grammars to walking 
syntax trees. Knowing the types of parsing capabilities will help one select an appropriate 
library.

So what is in this project?
===========================

This project contains several modules that demonstrates parsing a simple language 
using different libraries. The grammar is simple. The input should be a list of simple 
assignments statements (e.g. "a = 2 * 3;"). The list can also reference previously 
assigned variables (e.g. "b = a * a;")

## [`learn-lex-yacc-antlr2`](./learn-lex-yacc-antlr2)

ANTLR2, an LL parser, is one of the oldest Java parsing libraries out there, and 
while no longer maintained, it is still oddly used in many places. So despite being 
old, I decided to show an example.

https://www.antlr2.org/

## [`learn-lex-yacc-antlr4`](./learn-lex-yacc-antlr4)

ANTLR4, an adaptive LL parser, supports direct forms of left recursion grammars (but 
not indirect grammars). This gives one plenty of flexibility to write easy to understand 
grammars.

https://www.antlr.org/

## [`learn-lex-yacc-cookcc`](./learn-lex-yacc-cookcc)

CookCC is an LALR parser. It is written in Java and has an interesting feature of 
allowing one to write the grammar as annotations to be processed by an annotation
processing tool (APT).

https://coconut2015.github.io/cookcc/index.html

## [`learn-lex-yacc-jparsec`](./learn-lex-yacc-jparsec)

JParsec is a combinator parser, basically a Java implementation of the Haskell 
Parsec project.

https://github.com/jparsec/jparsec/wiki/Tutorial

## [`learn-lex-yacc-jflex`](./learn-lex-yacc-jflex)

JFlex is just a lexical analyzer, based on deterministic finite automata (DFA). It 
can execute actions when certain rules are matched. JFlex does not support context 
free grammars. I show an example of tokenizing a properties file. 

https://www.jflex.de/

## Possible Next Steps

What can I do to support better error handling?

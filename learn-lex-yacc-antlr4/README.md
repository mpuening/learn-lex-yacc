ANTLR4
======

ANTLR4 (ANother Tool for Language Recognition) is obviously much more advanced than 
its predecessors in that it is able to handle direct left recursive grammars.

ANTLR4 parsing is also much better in that one can implement visitors without having 
to directly write code to walk the syntax tree.

In this project, there are two implementations of the calculator that uses two kinds 
of grammars. The `Antlr4ClassicGrammarCalculator` calculator uses a grammar that 
should be familiar to those people that have used ANTLR2. The visitors in the class 
roughly match what one would have done with ANTLR2 parsing, but the code is much 
clearer of what is going on (even though oddly might be more code?).

The other implementation `Antlr4LeftRecursiveGrammarCalculator` uses the left- recursive 
grammar. The grammar for this implementation shows how ANTLR4 is much better than 
ANTLR2. The code is also more direct to the point. Being able to declare which method 
to invoke for each rule is an excellent feature.

Here is the grammar (short and sweet, see LeftRecursiveCalc.g4):

```
statement
    :   (variable '=' expression ';')+
    ;

variable
    :   VARIABLE
    ;

expression
    :   lhs=expression op=('*'|'/') rhs=expression  #operation
    |   lhs=expression op=('+'|'-') rhs=expression  #operation
    |   '(' expression ')'                          #nestedExpression
    |   term=variable                               #variableTerm
    |   term=NUMBER                                 #numberTerm
    ;

NUMBER
    :   ('0' .. '9') + ('.' ('0' .. '9') +)?
    ;

VARIABLE
    :   ('a' .. 'z')+
    ;
```

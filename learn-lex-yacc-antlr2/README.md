ANTLR2
======

ANTLR2 (ANother Tool for Language Recognition) is an LL recursive-descent parser 
and thus you cannot express a seemingly simple rule such as:

```
expr : expr "+" term | term
```

Such a rule causes infinite left recursion. Therefore ANTLR2 grammars need to work 
around  that restriction.

For more documentation on writing grammars and parsers, see these links:

* https://www.antlr2.org/doc/sor.html
* https://www.antlr2.org/doc/trees.html

## Dealing with Left Recursion

With LL parsing, we write the grammar to split an expression into the left hand term 
and optionally adding an operator and recursive expression (tail recursion).

For example (see calc_tail.g):

```
stmt
    :   (variable ASSIGNMENT expr SEMI)+
    ;

variable
    :   VARIABLE;

expr
    :   product sum_tail
    ;

sum_tail
    :   ADD product sum_tail
    |   SUBTRACT product sum_tail
    |   // epsilon
    ;

product
    :   term product_tail
    ;

product_tail
    :   MULTIPLY term product_tail
    |   DIVIDE term product_tail
    |   // epsilon
    ;

term
    :   NUMBER
    |   VARIABLE
    |   LPAREN expr RPAREN
    ;
```

This project implements a class called `Antlr2TailGrammarCalculator` that has a method 
to parse and evaluate the statements passed into it.

There are also test cases demonstrate how to use the calculator.

When writing a grammar to infix the operators into an expression we need deal with 
ambiguity with other rules. The following grammar shows how that is done (see calc_infix.g):

```
stmt
    :   (variable ASSIGNMENT expr SEMI)+
    ;

variable
    :   VARIABLE;

expr
    :   sum
    ;

sum
    :   ( product ADD sum ) => addition_expr
    |   ( product SUBTRACT sum ) => subtraction_expr
    |   product
    ;

addition_expr
    :   product ADD sum
    ;

subtraction_expr
    :   product SUBTRACT sum
    ;

product
    :   ( term MULTIPLY product ) => multiplication_expr
    |   ( term DIVIDE product ) => division_expr
    |   term
    ;

multiplication_expr
    :   term MULTIPLY product
    ;

division_expr
    :   term DIVIDE product
    ;

term
    :   NUMBER
    |   VARIABLE
    |   LPAREN expr RPAREN
    ;
```

This project implements a class called `Antlr2InfixGrammarCalculator` that has a 
method to parse and evaluate the statements passed into it using the above grammar. 
Note that it uses the same syntax tree walker as the `Antlr2TailGrammarCalculator` 
class.

## Incorrect Parsing Approach for Syntax Trees

When parsing a syntax tree, it is important to know how that tree is represented. 
In the above grammars, the tree is flat (no children). Recursive methods are used 
to walk the syntax tree. ANTLR2 can do trees, but unfortunately it is not always 
easy to get the correct rules to produce children.

Here is a grammar and parser that does *not* evaluate all expressions correctly (see 
calc_incorrect.g):

```
stmt
    :   expr SEMI
    ;
  
expr
    :   sum
    ;

sum
    :   ( product ADD^ sum ) => addition_expr
    |   ( product SUBTRACT^ sum ) => subtraction_expr
    |   product
    ;

addition_expr
    :   product ADD^ sum
    ;

subtraction_expr
    :   product SUBTRACT^ sum
    ;

product
    :   ( term MULTIPLY^ product ) => multiplication_expr
    |   ( term DIVIDE^ product ) => division_expr
    |   term
    ;

multiplication_expr
    :   term MULTIPLY^ product
    ;

division_expr
    :   term DIVIDE^ product
    ;

term
    :   NUMBER
    |   LPAREN! expr RPAREN!
    ;

class CalcIncorrectTreeWalker extends TreeParser;

stmt returns [double result] {
        double e;
        result=0;
    }
    :   e=expr SEMI                     {result = e;}       
    ;

expr returns [double result] {
        double lhs,rhs;
        result=0;
    }
    :   LPAREN lhs=expr RPAREN          {result = lhs;}
    |   #(ADD lhs=expr rhs=expr)        {result = lhs + rhs;}
    |   #(SUBTRACT lhs=expr rhs=expr)   {result = lhs - rhs;}
    |   #(MULTIPLY lhs=expr rhs=expr)   {result = lhs * rhs;}
    |   #(DIVIDE lhs=expr rhs=expr)     {result = lhs / rhs;}
    |   n:NUMBER                        {result = (double)Double.parseDouble(n.getText());}
    ;
```

The problem is that the operations performed in a right associative fashion instead 
of left associative fashion.

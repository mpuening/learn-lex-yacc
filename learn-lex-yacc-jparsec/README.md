Jparsec
=======

JParsec is a recursive-descent parser combinator framework, similar to the Haskell 
Parsec project. In this framework, one simply writes and connects small parsers to 
build a large parser.

In this project, the `JParsecCalculator` class uses parsers to evaluate identifiers, 
expressions and variables.

And beyond the statements and expressions, there are uses of built-in parsers for 
numbers and identifiers. JParsec includes many convenient features such as building 
operator tables and writing rules that understand associativity. This makes it very 
easy to quickly build parsers.

It is interesting to note that the JParsec library requires the least amount of code 
to parse my little sample language. There is no external file to write the grammar. 
There is no need for a maven plugin or annotation processor to include in a build 
step. A trade-off might be that perhaps it is not clear what the grammar looks like.


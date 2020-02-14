CookCC
======

At the time of this writing, CookCC is only at version 0.4.x, and it seems as though 
work has stopped on it as there has not been a commit in a couple of years.

But the feature of embedding the rules as annotations is interesting. The CookCC 
project includes an annotation processor (APT), but I did not have much luck with 
it. I wonder if it uses the more modern APIs for annotation processing. I did not 
see the `services` file in the manifest directory, so perhaps not.

Furthermore, the code should not be generated in the `src/main/java` tree but rather 
`target/generate-sources` like all the other plugins. There are also small other improvements 
needed such as being able to have the generated code be under a package of my choosing, 
but these are small complaints.

This is using the maven plugin instead and copying the code to conventional location.

This project implements its calculator in `CookCCCalculator` and `CookCCCalculatorParser`.

This code is interesting to me because it has `Node` classes from the application 
to represent the syntax tree and not from the framework. This gives one all the flexibility 
to do whatever is necessary to parse the input, including not even building a node 
for unnecessary information such as parentheses.


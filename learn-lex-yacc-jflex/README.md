JFlex
=====

JFlex is a lexical analyzer generator, so it is usually only the first half of parsing 
information. Therefore there is not a calculator implementation like the other projects. 
But perhaps there might some day be a need to tokenize input without having to interpret 
it.

This project uses a very simple rules for Java properties files, (which is probably 
not complete; for example line continuation trimming is not supported). The test 
cases demonstrates iterating over the tokens of a short properties file.

For information about the properties file format, see this link:

https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Properties.html#load(java.io.Reader)

## TODO

How would one do a simple transformation? For example, can the lexer deal with the rules 
for line continuation to trim out the back slash and indentation?

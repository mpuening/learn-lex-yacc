package io.github.learnlexyacc.jflex;

%%

%class PropertiesLexer
%standalone

EOL=[\r\n]
WHITE_SPACE=[\ \t\r\n]
INLINE_WHITE_SPACE=[\ \t]

COMMENT=("#"|"!")[^\r\n]*

// Simple key, separator, and value regular expressions
KEY=[^:=\ \t\r\n\\] | "\\"{EOL} | "\\".
SEPARATOR=[=:]
VALUE=[^\t\r\n\\] | "\\"{EOL} | "\\".

// Deal with white space up to a value, includes support for line continuation
VALUES_BEFORE_SEPARATOR=([^=:\ \t\r\n\\] | "\\"{EOL} | "\\".)({VALUE}*)
VALUES_AFTER_SEPARATOR=([^\ \t\r\n\\] | "\\"{EOL} | "\\".)({VALUE}*)

// There are three states to accommodate rules (initial state supports the key)
%state VALUE
%state BEFORE_SEPARATOR
%state AFTER_SEPARATOR

%%

<YYINITIAL> {COMMENT}						{ yybegin(YYINITIAL); return TokenType.COMMENT; }

<YYINITIAL> {KEY}+							{ yybegin(BEFORE_SEPARATOR); return TokenType.KEY; }

<BEFORE_SEPARATOR> {
	{INLINE_WHITE_SPACE}+					{ yybegin(BEFORE_SEPARATOR); return TokenType.WHITE_SPACE; }
	{SEPARATOR}								{ yybegin(AFTER_SEPARATOR); return TokenType.SEPARATOR; }
	{VALUES_BEFORE_SEPARATOR}				{ yybegin(YYINITIAL); return TokenType.VALUE; }
	{EOL}{WHITE_SPACE}*						{ yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
}

<AFTER_SEPARATOR> {
	{INLINE_WHITE_SPACE}+					{ yybegin(AFTER_SEPARATOR); return TokenType.WHITE_SPACE; }
	{VALUES_AFTER_SEPARATOR}				{ yybegin(YYINITIAL); return TokenType.VALUE; }
	{EOL}{WHITE_SPACE}*						{ yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
}

<VALUE> {VALUE}+							{ yybegin(YYINITIAL); return TokenType.VALUE; }
<VALUE> {EOL}{WHITE_SPACE}*					{ yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
{WHITE_SPACE}+								{ return TokenType.WHITE_SPACE; }

[^]											{ return TokenType.ERROR; }
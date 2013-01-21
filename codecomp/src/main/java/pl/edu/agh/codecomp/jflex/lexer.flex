package pl.edu.agh.codecomp.jflex;

/* JFlex example */

import java_cup.runtime.*;

/**
* This class is a test lexer
*/
%%

%public
%class Lexer

%unicode
%line
%column
%cup

%{

StringBuffer string = new StringBuffer();

private Symbol symbol(int type) {
	return new Symbol(type, yyline, yycolumn);
}

private Symbol symbol(int type, Object value) {
	return new Symbol(type, yyline, yycolumn, value);
}

%}

LineTerminator			= \r|\n|\r\n
InputCharacter			= [^\r\n]
WhiteSpace				= {LineTerminator} | [ \t\f]

/* comments */
Comment 				= {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment 		= "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment		= "//" {InputCharacter}* {LineTerminator}
DocumentationComment	= "/**" {CommentContent} "*"+ "/"
CommentContent			= ( [^*] | \*+ [^/*] )*
Identifier				= [:jletter:] [:jletterdigit:]*
DecIntegerLiteral		= 0 | [1-9][0-9]*

%state STRING
%state NESTED_COMMENT

%%

/* keywords */
<YYINITIAL> "abstract"	{ return symbol(sym.ABSTRACT); }
<YYINITIAL>	"boolean"	{ return symbol(sym.BOOLEAN); }
<YYINITIAL>	"break"		{ return symbol(sym.BREAK); }

<YYINITIAL> {
	/* idendifiers */
	{Identifier}		{ return symbol(sym.IDENTIFIER); }
	
	/* literals */
	{DecIntegerLiteral}	{ return symbol(sym.INTEGER_LITERAL); }
	\"					{ string.setLength(0); yybegin(STRING); }
	
	/* operators */
	"="					{ return symbol(sym.EQ); }
	"=="				{ return symbol(sym.EQEQ); }
	"+"					{ return symbol(sym.PLUS); }
	
	/* comments */
	{Comment}			{ /* ignore */ }
	
	/* whitespace */	
	{WhiteSpace}		{ /* ignore */ }	
}

<STRING> {
	\"					{ yybegin(YYINITIAL);
						  return symbol(sym.LITERAL.STRING, string.toString()); }
	[^\n\r\"\\]+		{ string.append(yytext()); }
	\\t					{ string.append('\t'); }
	\\n					{ string.append('\n'); }
	\\r					{ string.append('\r'); }
	\\\"				{ string.append('\"'); }
	\\					{ string.append('\\'); }
}

/* error fallback */
<YYINITIAL>.|\n					{ throw new Error("Illegal character <"+ yytext() +">"); }
/* === USERCODE SECTION === */

package pl.edu.agh.codecomp.jflex;

/* JFlex example */

import java_cup.sym;
import java_cup.runtime.Symbol;

/**
* This class is a test lexer
*/
%%

/* === OPTIONS AND DECLARATIONS SECTION === */

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
CommentContent			= ( [^*] | \*+ [^/*] )*
TraditionalComment 		= "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment		= "//" {InputCharacter}* {LineTerminator}
DocumentationComment	= "/**" {CommentContent} "*"+ "/"

Identifier				= [:jletter:] [:jletterdigit:]*
DecIntegerLiteral		= ('+[0-9]*'+)|([0-9]*)


/* ASSEMBLER SPECIFICS */

Register				= al|ax|cx|dx|bx|sp|bp|si|di
Arithmetic				= [+-/\*]

LBraces					= \[|\(|\{
RBraces					= \]|\)|\}

/*
### !!! TODO !!! ###

/* comments */
AssemblerComment		= ";" {InputCharacter}* {LineTerminator}
 
/* registers */
Register				= (A|C|D|B)X|(S|B)P|(S|D)I

/* operations */
Transfer
Arithmetic
Logic
Misc
Jump
*/

%state STRING

%%

/* === LEXICAL RULES SECTION === */

/* keywords */
/*
<YYINITIAL> "abstract"	{ return symbol(sym.ABSTRACT); }
<YYINITIAL>	"boolean"	{ return symbol(sym.BOOLEAN); }
<YYINITIAL>	"break"		{ return symbol(sym.BREAK); }
*/

<YYINITIAL> {

	/* identifiers */
	{Register}			{ return symbol(sym.TERMINAL); }
	{Identifier}		{ return symbol(sym.ID); }
	
	/* literals */
	{DecIntegerLiteral}	{ return symbol(sym.NONTERMINAL); }
	\"					{ string.setLength(0); yybegin(STRING); }
	
	/* operators */
	"="					{ return symbol(sym.ACTION); }
	"=="				{ return symbol(sym.ACTION); }
	"."					{ return symbol(sym.DOT); }
	","					{ return symbol(sym.COMMA); }
	";"					{ return symbol(sym.SEMI); }
	":"					{ return symbol(sym.COLON); }
	"'"					{ return symbol(sym.NONTERMINAL); }
	{LBraces}			{ return symbol(sym.LBRACK); }
	{RBraces}			{ return symbol(sym.RBRACK); }
	{Arithmetic}		{ return symbol(sym.ACTION); }
	
	/* comments */
	{Comment}			{ /* ignore */ }
	
	/* whitespace */	
	{WhiteSpace}		{ /* ignore */ }
}

<STRING> {
	\"					{ yybegin(YYINITIAL);
						  return symbol(sym.CODE_STRING, string.toString()); }
	[^\n\r\"\\]+		{ string.append(yytext()); }
	\\t					{ string.append('\t'); }
	\\n					{ string.append('\n'); }
	\\r					{ string.append('\r'); }
	\\\"				{ string.append('\"'); }
	\\					{ string.append('\\'); }
}

/* error fallback */
<YYINITIAL>.|\n			{ System.err.println("Illegal character <"+ yytext() +">"); }
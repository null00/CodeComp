/* === USERCODE SECTION === */

package pl.edu.agh.codecomp.lexer;
import pl.edu.agh.codecomp.parser.Parser;

/* JFlex example */	

/**
* This class is an assembler lexer/scanner
*/

%%

/* === OPTIONS AND DECLARATIONS SECTION === */

%public
%class SimpleScanner
%implements IScanner
%type int

%unicode
%line
%column
%byaccj

%{

StringBuffer string = new StringBuffer();

/* store a reference to the parser object */
private Parser yyparser;

/* constructor taking an additional parser object */
public SimpleScanner(java.io.Reader r, Parser yyparser) {
	this(r);
	this.yyparser = yyparser;
}

%}

LineTerminator			= \r|\n|\r\n
InputCharacter			= [^\r\n]
WhiteSpace				= {LineTerminator} | [ \t\f]

Label					= ("."+ [:letter:]+[:digit:]*)|([:letter:]+[:digit:]* ":"+)
Register				= e?(al|ax|cx|dx|bx|sp|bp|ip|si|di|ss)
/* Identifier				= [:letter:]*[\_]*[:digit:]* */
Identifier				= [a-zA-Z0-9\_]*

Number					= {DecLiteral} | {HexLiteral}
DecLiteral				= "'"? [:digit:]* "'"?
HexLiteral				= (0x)?[0-9a-fA-F:]+(H|h)?

Operation				= {Arithmetic} | {Transfer} | {Misc} | {Logic} | {Jump}
Arithmetic				= [\+\-\/\*]|cmp|add|sub|sbb|div|idiv|mul|imul|inc|dec|sal|sar|rcl|rcr|rol|ror
Transfer				= mov|push|pushf|pusha|pop|popf|popa|in|out|xchg|stc|clc|cmc|std|cld|sti|cli|cbw|cwd|cwde
Misc					= nop|lea|int
Logic					= and|or|xor|not|neg
Jump					= call|jmp|je|jz|jcxz|jp|jpe|ret|jne|jnz|jecxz|jnp|jpo

LBraces					= \[|\(|\{
RBraces					= \]|\)|\}

%state STRING

%%

/* === LEXICAL RULES SECTION === */

/* keywords */

<YYINITIAL> {

	/* operators */
	"="					{ return Parser.EQ; }
	","					{ return Parser.COMMA; }
	"'"					{ return Parser.APOSTROPHE; }
/*	"?"					{ return Parser.QMARK; }*/
	
	/* operations */
	{LBraces}			{ return Parser.LBRACE; }
	{RBraces}			{ return Parser.RBRACE; }
	{Operation}			{ return Parser.OP; }
	
	/* literals */
	{Number}			{ return Parser.NUM; }
	
	/* identifiers */
	{Register}			{ return Parser.REG; }
	{Label}				{ return Parser.LAB; }
	{Identifier}		{ return Parser.ID; }
	
	\"					{ string.setLength(0); yybegin(STRING); }
	
	/* whitespace */	
	{WhiteSpace}		{ /* ignore */ }
	
}

<STRING> {
	\"					{ yybegin(YYINITIAL);
						  return Parser.TEXT; }
	[^\n\r\"\\]+		{ string.append(yytext()); }
	\\t					{ string.append('\t'); }
	\\n					{ string.append('\n'); }
	\\r					{ string.append('\r'); }
	\\\"				{ string.append('\"'); }
	\\					{ string.append('\\'); }
}

/* error fallback */
<YYINITIAL>.|\n			{ System.err.println("Illegal character <"+ yytext() +">"); }
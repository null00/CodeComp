/* === USERCODE SECTION === */

package pl.edu.agh.codecomp.lexer;

/* JFlex example */	

/**
* This class is an assembler lexer/scanner
*/
%%

/* === OPTIONS AND DECLARATIONS SECTION === */

%public
%class SimpleScanner
%implements IScanner
%type String

%unicode
%line
%column
%byaccj

%{

StringBuffer string = new StringBuffer();

%}

LineTerminator			= \r|\n|\r\n
InputCharacter			= [^\r\n]
WhiteSpace				= {LineTerminator} | [ \t\f]

Label					= ("."+ [:letter:]+[:digit:]*)|([:letter:]+[:digit:]* ":"+)
Register				= al|ax|cx|dx|bx|sp|bp|ip|si|di
Identifier				= [:letter:]*[:digit:]*

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
	"="					{ return "Equals"; }
	","					{ return "COMMA"; }
	"'"					{ return "APOSTROPHE"; }
	"?"					{ return "QMARK"; }
	
	/* operations */
	{LBraces}			{ return "LBrace"; }
	{RBraces}			{ return "RBrace"; }
	{Operation}			{ return "Operation"; }
	
	/* literals */
	{Number}			{ return "Number"; }
	
	/* identifiers */
	{Register}			{ return "Register"; }
	{Label}				{ return "Label"; }
	{Identifier}		{ return "ID"; }
	
	\"					{ string.setLength(0); yybegin(STRING); }
	
	/* whitespace */	
	{WhiteSpace}		{ /* ignore */ }
	
}

<STRING> {
	\"					{ yybegin(YYINITIAL);
						  return string.toString(); }
	[^\n\r\"\\]+		{ string.append(yytext()); }
	\\t					{ string.append('\t'); }
	\\n					{ string.append('\n'); }
	\\r					{ string.append('\r'); }
	\\\"				{ string.append('\"'); }
	\\					{ string.append('\\'); }
}

/* error fallback */
<YYINITIAL>.|\n			{ System.err.println("Illegal character <"+ yytext() +">"); }
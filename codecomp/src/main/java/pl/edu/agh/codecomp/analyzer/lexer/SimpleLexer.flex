/* === USERCODE SECTION === */

package pl.edu.agh.codecomp.lexer;
import pl.edu.agh.codecomp.parser.Parser;

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

%}

LineTerminator			= \r|\n|\r\n
WhiteSpace				= {LineTerminator} | [ \t\f]

Label					= ("."+ [:letter:]+[:digit:]*)|([:letter:]+[:digit:]* ":"+)
Register				= e?(al|ax|cx|dx|bx|sp|bp|ip|si|di|ss)
Identifier				= [a-zA-Z0-9\_]*

Number					= {DecLiteral} | {HexLiteral}
DecLiteral				= "'"? [:digit:]* "'"?
HexLiteral				= (0x)?[0-9a-fA-F:]+(H|h)?

SingleArithmetic		= aaa|daa|inc|dec
Arithmetic				= cmp|add|sub|sbb|div|idiv|mul|imul|sal|sar|rcl|rcr|rol|ror|adc
SingleTransfer			= clc|cmc|cld|cli|push|pushf|pusha|pop|popf|popa|stc|std
Transfer				= mov|in|out|xchg|sti|cbw|cwd|cwde
Misc					= nop|lea|int|rep|repne|repe
SingleLogic				= not|neg
Logic					= and|or|xor
Jump					= call|jmp|je|jz|jcxz|jp|jpe|ret|jne|jnz|jecxz|jnp|jpo
Store					= stosw|stosb|stosd
Compare					= scas|scasb|scasw|scasd

LBraces					= \[|\(|\{
RBraces					= \]|\)|\}

%state STRING

%%a small program that calculates and prints terms of the fibonacci series

/* === LEXICAL RULES SECTION === */

/* keywords */

<YYINITIAL> {

	/* operators */
	"="					{ return Parser.EQ; }
	","					{ return Parser.COMMA; }
	"'"					{ return Parser.APOSTROPHE; }
	
	/* operations */
	{Arithmetic}		{ return Parser.OP_AR; }
	{SingleArithmetic}	{ return Parser.OP_SAR; }
	{Transfer}			{ return Parser.OP_MOV; }
	{SingleTransfer}	{ return Parser.OP_SMOV; }	
	{Misc}				{ return Parser.OP_MISC; }
	{Logic}				{ return Parser.OP_LOG; }
	{SingleLogic}		{ return Parser.OP_SLOG; }
	{Jump}				{ return Parser.OP_JMP; }
	{Store}				{ return Parser.OP_STO; }
	{Compare}			{ return Parser.OP_COMP; }
	
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


	/* operators */
	"+" |
	"-" |
	"*" |
	"/" |
	"[" |
	"]" |
	{LBraces} |
	{RBraces} { return (int) yycharat(0); }
	
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
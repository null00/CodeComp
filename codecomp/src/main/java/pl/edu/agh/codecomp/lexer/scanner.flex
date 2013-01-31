/* === USERCODE SECTION === */

package pl.edu.agh.codecomp.lexer;

/* JFlex example */

import java_cup.sym;

/**
* This class is an assembler lexer/scanner
*/
%%

/* === OPTIONS AND DECLARATIONS SECTION === */

%public
%class Scanner
%implements IScanner
%type String

%unicode
%line
%column
%byaccj

%{

StringBuffer string = new StringBuffer();

private String symbol(int code, String label) {
	return code + ": " + label;
}

%}

LineTerminator			= \r|\n|\r\n
InputCharacter			= [^\r\n]
WhiteSpace				= {LineTerminator} | [ \t\f]

/* comments */

Comment 				= {TraditionalComment} | {AssemblerComment}
CommentContent			= ( [^*] | \*+ [^/*] )*
TraditionalComment 		= "/*" [^*] ~"*/" | "/*" "*"+ "/"

Identifier				= [:letter:]*[:digit:]*
DecIntegerLiteral		= "'"? [:digit:]* "'"?
HexLiteral				= (0x)?[0-9a-fA-F:]+(H|h)?

/* ASSEMBLER SPECIFICS */

/* comments */
AssemblerComment		= {WhiteSpace}* ";" {InputCharacter}* {LineTerminator}*

Label					= ("."+ [:letter:]+[:digit:]*)|([:letter:]+[:digit:]* ":"+)

/* registers */
Register				= al|ax|cx|dx|bx|sp|bp|ip|si|di

/* operations */
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
	"="					{ return symbol(sym.ACTION, "Equals"); }
	"=="				{ return symbol(sym.ACTION, "EQEQ"); }
	","					{ return symbol(sym.COMMA, "COMMA"); }
	"'"					{ return symbol(sym.NONTERMINAL, "APOSTROPHE"); }
	"?"					{ return symbol(sym.NONTERMINAL, "QMARK"); }
	
	/* operations */
	{LBraces}			{ return symbol(sym.LBRACK, "LBrace"); }
	{RBraces}			{ return symbol(sym.RBRACK, "RBrace"); }
	{Arithmetic}		{ return symbol(sym.ACTION, "Arithmetic"); }
	{Transfer}			{ return symbol(sym.ACTION, "Transfer"); }
	{Logic}				{ return symbol(sym.ACTION, "Logic"); }
	{Misc}				{ return symbol(sym.ACTION, "Misc"); }
	{Jump}				{ return symbol(sym.ACTION, "Jump"); }
	
	/* literals */
	{DecIntegerLiteral}	{ return symbol(sym.NONTERMINAL, "Number"); }
	{HexLiteral}		{ return symbol(sym.NONTERMINAL, "HexNumber"); }
	
	/* identifiers */
	{Register}			{ return symbol(sym.TERMINAL, "Register"); }
	{Label}				{ return symbol(sym.NONTERMINAL, "Label"); }
	{Identifier}		{ return symbol(sym.ID, "ID"); }
	
	\"					{ string.setLength(0); yybegin(STRING); }
	
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
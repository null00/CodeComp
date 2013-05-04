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
Arithmetic				= /*[\+\-\/\*]|*/cmp|add|sub|sbb|div|idiv|mul|imul|inc|dec|sal|sar|rcl|rcr|rol|ror
SingleTransfer			= clc|cmc|cld|cli
Transfer				= mov|push|pushf|pusha|pop|popf|popa|in|out|xchg|stc|std|sti|cbw|cwd|cwde
Misc					= nop|lea|int|rep|repne|repe
Logic					= and|or|xor|not|neg
Jump					= call|jmp|je|jz|jcxz|jp|jpe|ret|jne|jnz|jecxz|jnp|jpo
Store					= stosw|stosb|stosd
Compare					= scas|scasb|scasw|scasd

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
	{LBraces}			{  }
	{RBraces}			{  }
	/*{Operation}		{ return Parser.OP; }*/
	{Arithmetic}		{ return Parser.OP_AR; }
	{Transfer}			{ return Parser.OP_MOV; }
	{SingleTransfer}	{ return Parser.OP_SMOV; }	
	{Misc}				{ return Parser.OP_MISC; }
	{Logic}				{ return Parser.OP_LOG; }
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
/* === DECLARATIONS === */

%{

package pl.edu.agh.codecomp.parser;

import java.io.IOException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import pl.edu.agh.codecomp.lexer.IScanner;

%}

%start input 
%token TEXT, NUM, OP_MOV, OP_AR, OP_LOG, OP_MISC, OP_JMP, OP_STO, OP_COMP, REG, LAB, ID, EQ, COMMA, APOSTROPHE
%left '-' '+'
%left '*' '/'
%left NEG /* negation--unary minus */
%right '^' /* exponentiation */

%%

/* === RULES (GRAMMAR) === */

input: /* empty string */
 | input line
 ;

line: '\n'
 | exp { System.out.println(" # " + $1.sval + " "); }
 ;

exp: NUM			 		{ $$ = $1; }
 | OP_MOV			 		{ $$ = $1; }
 | OP_JMP			 		{ $$ = $1; }
 | OP_LOG			 		{ $$ = $1; }
 | OP_MISC			 		{ $$ = $1; }
 | OP_AR			 		{ $$ = $1; }
 | OP_STO					{ $$ = $1; }
 | OP_COMP					{ $$ = $1; }
 | LAB 				 		{ $$ = $1; }
 | REG				 		{ $$ = $1; }
 | EQ 				 		{ $$ = $1; }
 | COMMA 			 		{ $$ = $1; }
 | ID 				 		{ $$ = $1; }
 | APOSTROPHE 		 		{ $$ = $1; }
 | OP_AR REG				{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 | OP_AR ID					{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 | OP_MOV REG				{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 | OP_MOV LAB				{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 | OP_MOV ID				{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 | OP_JMP LAB 				{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 | OP_MISC REG				{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 | OP_MISC OP_STO			{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 | OP_LOG REG				{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 | OP_MISC OP_COMP			{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 | exp '+' exp				{ $$ = new ParserVal($1.sval + " + " + $3.sval); }
 | exp '-' exp				{ $$ = new ParserVal($1.sval + " - " + $3.sval); }
 | exp '*' exp				{ $$ = new ParserVal($1.sval + " * " + $3.sval); }
 | exp '/' exp				{ $$ = new ParserVal($1.sval + " / " + $3.sval); }
 | exp COMMA exp			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | '[' exp ']' 		 		{ $$ = $2; }
 ;

%%

/* === PROGRAM === */

	private RSyntaxTextArea left, right;
	private IScanner scanner;

	public Parser(RSyntaxTextArea left, RSyntaxTextArea right, IScanner scanner) {
		this.left = left;
		this.right = right;
		this.scanner = scanner;
	}
	
	public Parser(RSyntaxTextArea left, RSyntaxTextArea right, IScanner scanner, boolean debugMe) {
        this.left = left;
        this.right = right;
        this.scanner = scanner;
        this.yydebug = debugMe;
    }

	void yyerror(String s) {
		System.err.println("parser: " + s);
	}

	// TODO:
	private int yylex() {
		int tok = -1;
		try {
			tok = scanner.yylex();
			yylval = new ParserVal(scanner.yytext());
			String tmp = "tok: " + yyname[tok] + ": '" + scanner.yytext() + "'";
			right.append(tmp + "\n");
//			System.out.println(tmp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return tok;
	}

/* === DECLARATIONS === */

%{

package pl.edu.agh.codecomp.parser;

import java.io.IOException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import pl.edu.agh.codecomp.lexer.IScanner;

%}

%start input 
%token TEXT, NUM, OP_MOV, OP_AR, OP_LOG, OP_MISC, OP_JMP, REG, LAB, ID, EQ, COMMA, APOSTROPHE, LBRACE, RBRACE

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
 | LAB 				 		{ $$ = $1; }
 | REG				 		{ $$ = $1; }
 | EQ 				 		{ $$ = $1; }
 | COMMA 			 		{ $$ = $1; }
 | ID 				 		{ $$ = $1; }
 | APOSTROPHE 		 		{ $$ = $1; }
 | LBRACE 			 		{ $$ = $1; }
 | RBRACE 			 		{ $$ = $1; }
 | TEXT 					{ $$ = $1; }
 | OP_MOV REG				{ $$ = new ParserVal("OP_MOV 1: " + $1.sval + " " + $2.sval); }
 | OP_JMP LAB 				{ $$ = new ParserVal("OP_JMP 1: " + $1.sval + " " + $2.sval); }
 | ID OP_AR ID		 		{ $$ = new ParserVal("OP_AR 1: " + $1.sval + " " + $2.sval + " " + $3.sval); }
 | ID OP_AR NUM		 		{ $$ = new ParserVal("OP_AR 2: " + $1.sval + " " + $2.sval + " " + $3.sval); }
 | REG OP_AR NUM		 	{ $$ = new ParserVal("OP_AR 3: " + $1.sval + " " + $2.sval + " " + $3.sval); }
 | OP_MOV REG COMMA NUM  	{ $$ = new ParserVal("OP_MOV 1: " + $1.sval + " " + $2.sval + " " + $3.sval + " " + $4.sval); }
 | OP_MOV REG COMMA ID 	 	{ $$ = new ParserVal("OP_MOV 2: " + $1.sval + " " + $2.sval + " " + $3.sval + " " + $4.sval); }
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
			System.out.println(tmp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return tok;
	}

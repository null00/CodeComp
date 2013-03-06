/* === DECLARATIONS === */

%{

package pl.edu.agh.codecomp.parser;

import java.io.IOException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import pl.edu.agh.codecomp.lexer.IScanner;

%}

%token TEXT, NUM, OP, REG, LAB, ID, EQ, COMMA, APOSTROPHE, LBRACE, RBRACE

%%

/* === RULES (GRAMMAR) === */

input: /* empty string */
 | input line
 ;

line: '\n'
 | exp { System.out.println(" < " + $1 + " "); }
 | exp '\n' { System.out.println(" < " + $1 + " "); }
 ;
 
exp: NUM { System.out.println(" >>> " + $1 + " <<< "); }
 | OP { System.out.println(" > " + $1 + " < "); }
 | LAB { System.out.println(" > " + $1 + " < "); }
 | REG { System.out.println(" > " + $1 + " < "); }
 | ID { System.out.println(" > " + $1 + " < "); }
 | EQ { System.out.println(" > " + $1 + " < "); }
 | COMMA { System.out.println(" > " + $1 + " < "); }
 | APOSTROPHE { System.out.println(" > " + $1 + " < "); }
 | LBRACE { System.out.println(" > " + $1 + " < "); }
 | RBRACE { System.out.println(" > " + $1 + " < "); }
 | TEXT { System.out.println(" > " + $1.dval + " < "); }
 ;

/*
op_mov:
 ;
 
op_jmp:
 ;
 
op_arithmetic:
 ;
 
op_logic:
 ;
 
op_transfer:
 ;

op_misc: 
 ;
*/

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
			right.append("tok: " + yyname[tok] + ": '" + scanner.yytext() + "'\n");
			System.out.println(right.getText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return tok;
	}

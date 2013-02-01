/* === DECLARATIONS === */

%{

package pl.edu.agh.codecomp.parser;

import java.io.IOException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import pl.edu.agh.codecomp.lexer.IScanner;

%}

%token NUM
%right '='
%left '-' '+'
%left '*' '/'

%%

/* === RULES (GRAMMAR) === */

input: /* empty string */
 | input line
 ;

line: '\n'
 | exp '\n' { System.out.println(" " + $1.dval + " "); }
 ;
 
 exp: NUM { System.out.println(" >>> " + $1 + " <<< "); }

%%

/* === PROGRAM === */

	private RSyntaxTextArea left, right;
	private IScanner scanner;

	public Parser(RSyntaxTextArea left, RSyntaxTextArea right, IScanner scanner) {
		this.left = left;
		this.right = right;
		this.scanner = scanner;
	}

	void yyerror(String s) {
		System.err.println("par:" + s);
	}

	// TODO:
	private int yylex() {
		int tok = 10;
		try {
			String s = scanner.yylex();
			if(s == null) return 0;
			System.out.println("tok:" + s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tok;
	}

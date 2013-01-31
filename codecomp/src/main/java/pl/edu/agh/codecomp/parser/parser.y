/* === DECLARATIONS === */

%{

package pl.edu.agh.codecomp.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.comparator.CompareToken;
import pl.edu.agh.codecomp.lexer.IScanner;
import pl.edu.agh.codecomp.lexer.Scanner;
import pl.edu.agh.codecomp.lexer.SimpleScanner;

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

	public Parser(RSyntaxTextArea left, RSyntaxTextArea right) {
		this.left = left;
		this.right = right;
		this.scanner = getComparator();
	}

	private IScanner getComparator() {
		IScanner scanner = null;
		Reader reader = new StringReader(left.getText());

		try {

			switch (CompareToken.getSourceComparator().toLowerCase()) {
			case "simple": {
				scanner = new SimpleScanner(reader);
				break;
			}
			case "full": {
				scanner = new Scanner(reader);
				break;
			}
			default: {
				scanner = new SimpleScanner(reader);
				break;
			}
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return scanner;
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
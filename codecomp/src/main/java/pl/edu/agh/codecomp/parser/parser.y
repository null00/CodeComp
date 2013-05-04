/* === DECLARATIONS === */

%{

package pl.edu.agh.codecomp.parser;

import java.io.IOException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import pl.edu.agh.codecomp.lexer.IScanner;
import pl.edu.agh.codecomp.tree.*;

%}

%start input 
%token TEXT, NUM, OP_MOV, OP_SMOV, OP_AR, OP_LOG, OP_MISC, OP_JMP, OP_STO, OP_COMP, REG, LAB, ID, EQ, COMMA, APOSTROPHE
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
 | func { System.out.println(" # " + $1.sval + " "); }
 ;
 
desc: LAB					{ $$ = $1; }
 | REG				 		{ $$ = $1; }
 | ID 				 		{ $$ = $1; }
 ;

single: OP_SMOV				{ $$ = $1; };

num: NUM					{ $$ = $1; };

exp: OP_MOV			 		{ $$ = $1; }
 | OP_JMP			 		{ $$ = $1; }
 | OP_LOG			 		{ $$ = $1; }
 | OP_MISC			 		{ $$ = $1; }
 | OP_AR			 		{ $$ = $1; }
 | OP_STO					{ $$ = $1; }
 | OP_COMP					{ $$ = $1; }
 ;
 
opers: '+'					{ $$ = $1; }
 | '-'						{ $$ = $1; }
 | '*'						{ $$ = $1; }
 | '/'						{ $$ = $1; }
 ;
 
ops: exp desc				{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 | exp exp					{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 | exp num					{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 | desc num					{ $$ = new ParserVal($1.sval + " " + $2.sval); }
 
 | num opers num			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | num opers exp			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | num opers ops			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | num opers desc			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 
 | exp opers exp			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | exp opers num			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); } 
 | exp opers ops			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | exp opers desc			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 
 | ops opers ops 			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | ops opers num			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | ops opers exp			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | ops opers desc			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 
 | desc opers desc			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | desc opers num			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | desc opers exp			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | desc opers ops			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
  
 | num COMMA num			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | num COMMA exp			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | num COMMA ops			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | num COMMA desc			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 
 | exp COMMA num			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | exp COMMA exp			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | exp COMMA ops			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | exp COMMA desc			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 
 | ops COMMA num			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | ops COMMA exp			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | ops COMMA ops			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | ops COMMA desc			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 
 | desc COMMA num			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | desc COMMA exp			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | desc COMMA ops			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 | desc COMMA desc			{ $$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval); }
 
 | '[' exp ']' 		 		{ $$ = $2; }
 | '[' ops ']' 		 		{ $$ = $2; }
 ;
 
func:
 | single					{ $$ = $1; } // { list.add($1.sval); }
 | desc						{ $$ = $1; } // { list.add($1.sval); }
 | exp						{ $$ = $1; }
 | ops						{ $$ = $1; }
 ;

%%

/* === PROGRAM === */

	private RSyntaxTextArea left, right;
	private IScanner scanner;
	private Node root;
	private List<Map<String, String>> tree;

	public Parser(RSyntaxTextArea left, RSyntaxTextArea right, IScanner scanner) {
		this.left = left;
		this.right = right;
		this.scanner = scanner;
		this.root = new Node();
	}
	
	public Parser(RSyntaxTextArea left, RSyntaxTextArea right, IScanner scanner, boolean debugMe) {
        this(left, right, scanner);
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
			//System.out.println(yyname[tok]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return tok;
	}

/* === DECLARATIONS === */

%{

package pl.edu.agh.codecomp.parser;

import java.io.IOException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import pl.edu.agh.codecomp.lexer.IScanner;
import pl.edu.agh.codecomp.tree.Node;
import java.util.LinkedList;
import java.util.List;

%}

%start input 
%token TEXT, NUM, OP_MOV, OP_SMOV, OP_AR, OP_SAR, OP_LOG, OP_SLOG, OP_MISC, OP_JMP, OP_STO, OP_COMP, REG, LAB, ID, EQ, COMMA, APOSTROPHE
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
 | func 					{ 
 								Node<String,String> node = (Node<String, String>)$1.obj;
 								root.addChild(node);
 							}
 ;
 
desc: LAB					{ $$ = $1; }
 | REG				 		{ $$ = $1; }
 | ID 				 		{ $$ = $1; }
 ;

single: OP_SMOV				{ 
								$$ = $1; 
							}
 | OP_SAR					{ 
								$$ = $1; 
							}
 | OP_JMP desc				{ 
								Node<String, String> node = new Node<String, String>(((Node<String, String>)$1.obj).getKey(), ((Node<String, String>)$1.obj).getValue());
								node.addChild((Node)$2.obj);
								$$ = new ParserVal(node);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 | OP_MISC OP_STO			{ 
								Node<String, String> node = new Node<String, String>(((Node<String, String>)$1.obj).getKey(), ((Node<String, String>)$1.obj).getValue());
								node.addChild((Node)$2.obj);
								$$ = new ParserVal(node);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 | OP_SMOV desc				{ 
								Node<String, String> node = new Node<String, String>(((Node<String, String>)$1.obj).getKey(), ((Node<String, String>)$1.obj).getValue());
								node.addChild((Node)$2.obj);
								$$ = new ParserVal(node);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 | OP_SLOG desc				{ 
								Node<String, String> node = new Node<String, String>(((Node<String, String>)$1.obj).getKey(), ((Node<String, String>)$1.obj).getValue());
								node.addChild((Node)$2.obj);
								$$ = new ParserVal(node);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 | OP_MISC OP_COMP			{ 
								Node<String, String> node = new Node<String, String>(((Node<String, String>)$1.obj).getKey(), ((Node<String, String>)$1.obj).getValue());
								node.addChild((Node)$2.obj);
								$$ = new ParserVal(node);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 ;

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
 
ops: exp ops				{ 
								Node<String, String> node = new Node<String, String>(((Node<String, String>)$1.obj).getKey(), ((Node<String, String>)$1.obj).getValue());
								node.addChild((Node)$2.obj);
								$$ = new ParserVal(node);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 | exp num					{ 
								Node<String, String> node = new Node<String, String>(((Node<String, String>)$1.obj).getKey(), ((Node<String, String>)$1.obj).getValue());
								node.addChild((Node)$2.obj);
								$$ = new ParserVal(node);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 | desc num					{ 
								Node<String, String> node = new Node<String, String>(((Node<String, String>)$1.obj).getKey(), ((Node<String, String>)$1.obj).getValue());
								node.addChild((Node)$2.obj);
								$$ = new ParserVal(node);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 
 | num opers num 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | num opers exp 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | num opers ops 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | num opers desc 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 
 | exp opers exp			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | exp opers num 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | exp opers ops 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | exp opers desc 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 
 | ops opers ops 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | ops opers num 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | ops opers exp 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | ops opers desc 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 
 | desc opers desc 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | desc opers num 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | desc opers exp 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | desc opers ops 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
  
 | num COMMA num 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | num COMMA exp 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | num COMMA ops 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | num COMMA desc 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 
 | exp COMMA num 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | exp COMMA exp 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | exp COMMA ops 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | exp COMMA desc 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 
 | ops COMMA num 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | ops COMMA exp 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | ops COMMA ops 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | ops COMMA desc 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 
 | desc COMMA num 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | desc COMMA exp 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | desc COMMA ops 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}
 | desc COMMA desc 			{ 
 								Node<String, String> node = new Node<String, String>(((Node<String, String>)$2.obj).getKey(), ((Node<String, String>)$2.obj).getValue());
 								node.addChild((Node)$1.obj); node.addChild((Node)$3.obj);
 								$$ = new ParserVal(node);
 								//$$ = new ParserVal($1.sval + " " + $2.sval + " " + $3.sval);
 							}

 | '[' exp ']' 				{ $$ = $2; }
 | '[' ops ']' 				{ $$ = $2; }
 ;
 
func:
 | single					{ $$ = $1; }
 | desc						{ $$ = $1; }
 | exp						{ $$ = $1; }
 | ops						{ $$ = $1; }
 ;

%%

/* === PROGRAM === */

	private RSyntaxTextArea left, right;
	private IScanner scanner;
	private Node<String,String> root = new Node<String, String>("TREE", "ROOT");

	public Parser(RSyntaxTextArea left, RSyntaxTextArea right, IScanner scanner) {
		this.left = left;
		this.right = right;
		this.scanner = scanner;
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
			
			yylval = new ParserVal(new Node<String, String>(yyname[tok], scanner.yytext()));
			
			String tmp = "tok: " + yyname[tok] + ": '" + scanner.yytext() + "'";
			right.append(tmp + "\n");
			//System.out.println(yyname[tok]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return tok;
	}
 	
 	public Node<String,String> getTree() {
 		return root;
 	}

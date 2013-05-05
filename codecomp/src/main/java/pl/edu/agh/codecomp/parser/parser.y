/* === DECLARATIONS === */

%{

package pl.edu.agh.codecomp.parser;

import java.io.IOException;
import java.util.HashMap;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.lexer.IScanner;
import pl.edu.agh.codecomp.tree.Node;
import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.graph.SimpleEdge;
import edu.ucla.sspace.graph.SparseUndirectedGraph;

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
 								root.addChild((Node)$1.obj);
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
								Node p = (Node)$1.obj;
								Node c = (Node)$2.obj;
								p.addChild(c);
								graph.add(new SimpleEdge(allNodes.get(p),allNodes.get(c)));
								$$ = new ParserVal(p);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 | OP_MISC OP_STO			{ 
								Node p = (Node)$1.obj;
								Node c = (Node)$2.obj;
								p.addChild(c);
								graph.add(new SimpleEdge(allNodes.get(p),allNodes.get(c)));
								$$ = new ParserVal(p);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 | OP_SMOV desc				{ 
								Node p = (Node)$1.obj;
								Node c = (Node)$2.obj;
								p.addChild(c);
								graph.add(new SimpleEdge(allNodes.get(p),allNodes.get(c)));
								$$ = new ParserVal(p);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 | OP_SLOG desc				{ 
								Node p = (Node)$1.obj;
								Node c = (Node)$2.obj;
								p.addChild(c);
								graph.add(new SimpleEdge(allNodes.get(p),allNodes.get(c)));
								$$ = new ParserVal(p);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 | OP_MISC OP_COMP			{ 
								Node p = (Node)$1.obj;
								Node c = (Node)$2.obj;
								p.addChild(c);
								graph.add(new SimpleEdge(allNodes.get(p),allNodes.get(c)));
								$$ = new ParserVal(p);
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
								Node p = (Node)$1.obj;
								Node c = (Node)$2.obj;
								p.addChild(c);
								graph.add(new SimpleEdge(allNodes.get(p),allNodes.get(c)));
								$$ = new ParserVal(p);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 | exp num					{ 
								Node p = (Node)$1.obj;
								Node c = (Node)$2.obj;
								p.addChild(c);
								graph.add(new SimpleEdge(allNodes.get(p),allNodes.get(c)));
								$$ = new ParserVal(p);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 | desc num					{ 
								Node p = (Node)$1.obj;
								Node c = (Node)$2.obj;
								p.addChild(c);
								graph.add(new SimpleEdge(allNodes.get(p),allNodes.get(c)));
								$$ = new ParserVal(p);
								//$$ = new ParserVal($1.sval + " " + $2.sval);
							}
 
 | num opers num 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | num opers exp 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | num opers ops 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | num opers desc 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 
 | exp opers exp			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | exp opers num 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | exp opers ops 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | exp opers desc 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 
 | ops opers ops 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | ops opers num 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | ops opers exp 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | ops opers desc 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 
 | desc opers desc 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | desc opers num 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | desc opers exp 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | desc opers ops 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
  
 | num COMMA num 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | num COMMA exp 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | num COMMA ops 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | num COMMA desc 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 
 | exp COMMA num 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | exp COMMA exp 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | exp COMMA ops 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | exp COMMA desc 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 
 | ops COMMA num 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | ops COMMA exp 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | ops COMMA ops 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | ops COMMA desc 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 
 | desc COMMA num 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | desc COMMA exp 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | desc COMMA ops 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
							}
 | desc COMMA desc 			{ 
								$$ = compute((Node)$2.obj, (Node)$1.obj, (Node)$3.obj);
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

	private RSyntaxTextArea source;
	private IScanner scanner;
	private Node<String,String> root = new Node<String, String>("TREE", "ROOT");
	private SparseUndirectedGraph graph;
	private HashMap<Node, Integer> allNodes;

	public Parser(RSyntaxTextArea source, IScanner scanner) {
		this.source = source;
		this.scanner = scanner;
		this.graph = new SparseUndirectedGraph();
		this.allNodes = new HashMap<Node, Integer>();
	}
	
	public Parser(RSyntaxTextArea source, IScanner scanner, boolean debugMe) {
        this(source, scanner);
        this.yydebug = debugMe;
    }

	void yyerror(String s) {
		System.err.println("parser: " + s);
	}

	private int count = 0;

	// TODO:
	private int yylex() {
		int tok = -1;
		try {
			tok = scanner.yylex();
			Node node = new Node(yyname[tok], scanner.yytext());
			yylval = new ParserVal(node);
			allNodes.put(node, count);
			count++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return tok;
	}
 	
 	public Node<String,String> getTree() {
 		return root;
 	}
 	
 	public Graph getGraph() {
 		return graph;
 	}
 	
 	private ParserVal compute(Node p, Node... c) {
 	    for(Node child : c) {
 	        p.addChild(child);
 	        graph.add(new SimpleEdge(allNodes.get(p),allNodes.get(child)));
 	    }
		return new ParserVal(p);
 	}

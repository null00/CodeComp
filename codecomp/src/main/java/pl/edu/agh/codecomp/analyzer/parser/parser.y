/* === DECLARATIONS === */

%{

package pl.edu.agh.codecomp.parser;

import java.io.IOException;
import java.util.HashMap;

import no.roek.nlpged.graph.Edge;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.graph.SimpleEdge;
import pl.edu.agh.codecomp.graph.SparseUndirectedGraph;
import pl.edu.agh.codecomp.lexer.IScanner;
import pl.edu.agh.codecomp.tree.Node;

%}

%start input 
%token TEXT, NUM, OP_MOV, OP_SMOV, OP_AR, OP_SAR, OP_LOG, OP_SLOG, OP_MISC, OP_JMP, OP_STO, OP_COMP, REG, LAB, ID, EQ, COMMA, APOSTROPHE
%left '-' '+'
%left '*' '/'
%left NEG
%right '^'

%%

/* === RULES (GRAMMAR) === */

input: /* empty string */
 | input line
 ;

line: '\n'
 | func 					{ 

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
								$$ = mergedCompute($1.obj, $2.obj);
							}
 | OP_MISC OP_STO			{ 
								$$ = mergedCompute($1.obj, $2.obj);
							}
 | OP_SMOV desc				{ 
								$$ = mergedCompute($1.obj, $2.obj);
							}
 | OP_SLOG desc				{ 
								$$ = mergedCompute($1.obj, $2.obj);
							}
 | OP_MISC OP_COMP			{ 
								$$ = mergedCompute($1.obj, $2.obj);
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
								$$ = mergedCompute($1.obj, $2.obj);
							}
 | exp num					{ 
								$$ = mergedCompute($1.obj, $2.obj);
							}
 | desc num					{ 
								$$ = mergedCompute($1.obj, $2.obj);
							}
 
 | num opers num 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | num opers exp 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | num opers ops 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | num opers desc 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 
 | exp opers exp			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | exp opers num 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | exp opers ops 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | exp opers desc 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 
 | ops opers ops 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | ops opers num 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | ops opers exp 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | ops opers desc 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 
 | desc opers desc 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | desc opers num 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | desc opers exp 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | desc opers ops 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
  
 | num COMMA num 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | num COMMA exp 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | num COMMA ops 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | num COMMA desc 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 
 | exp COMMA num 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | exp COMMA exp 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | exp COMMA ops 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | exp COMMA desc 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 
 | ops COMMA num 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | ops COMMA exp 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | ops COMMA ops 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | ops COMMA desc 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 
 | desc COMMA num 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | desc COMMA exp 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | desc COMMA ops 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
							}
 | desc COMMA desc 			{ 
								$$ = mergedCompute($1.obj, $2.obj, $3.obj);
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
	private no.roek.nlpged.graph.Graph graph2;
	private HashMap<Node, Integer> allNodes;

	public Parser(RSyntaxTextArea source, IScanner scanner) {
		this.source = source;
		this.scanner = scanner;
		this.graph = new SparseUndirectedGraph();
		this.allNodes = new HashMap<Node, Integer>();
		this.graph2 = new no.roek.nlpged.graph.Graph();
	}
	
	public Parser(RSyntaxTextArea source, IScanner scanner, boolean debugMe) {
        this(source, scanner);
        this.yydebug = debugMe;
    }

	void yyerror(String s) {
		System.err.println("parser: " + s);
	}

	private int count = 0, edge = 0;

	private int yylex() {
		int tok = -1;
		try {
			
			tok = scanner.yylex();
			
			Node node = new Node(yyname[tok], scanner.yytext());
			no.roek.nlpged.graph.Node node2 = new no.roek.nlpged.graph.Node(String.valueOf(count), scanner.yytext(), new String[] {yyname[tok], scanner.yytext()});
			graph2.addNode(node2);
			allNodes.put(node, count);
			
			Object[] obj = {node, node2};
			
			yylval = new ParserVal(obj);
			
			count++;
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return tok;
	}
 	
 	public Node<String,String> getTree() {
 		return root;
 	}
 	
 	public SparseUndirectedGraph getGraph() {
 		return graph;
 	}
 	
 	public no.roek.nlpged.graph.Graph getGraph2() {
 	    return graph2;
 	}
 	
 	private ParserVal mergedCompute(Object... objs) {
 		ArrayList<Object[]> listOfNodes = new ArrayList<Object[]>();
 		for(int i = 0; i < objs.length; i++) {
 		    Object[] node = (Object[])objs[i];
 			listOfNodes.add(node);
		}
 		Object[] obj = new Object[2];
		if(listOfNodes.size() == 2) {
		    obj[0] = compute((Node)listOfNodes.get(0)[0], (Node)listOfNodes.get(1)[0]);
			obj[1] = compute2((no.roek.nlpged.graph.Node)listOfNodes.get(0)[1], (no.roek.nlpged.graph.Node)listOfNodes.get(1)[1]);
		} else if(listOfNodes.size() == 3) {
			obj[0] = compute((Node)listOfNodes.get(1)[0], (Node)listOfNodes.get(0)[0], (Node)listOfNodes.get(2)[0]);
			obj[1] = compute2((no.roek.nlpged.graph.Node)listOfNodes.get(1)[1], (no.roek.nlpged.graph.Node)listOfNodes.get(0)[1], (no.roek.nlpged.graph.Node)listOfNodes.get(2)[1]);
		}
		return new ParserVal(obj);
 	}
 	
 	private ParserVal compute(Node p, Node... child) {
 	    for(Node c : child) {
 	        p.addChild(c);
 	        graph.add(new SimpleEdge(allNodes.get(p),allNodes.get(c)));
 	    }
		return new ParserVal(p);
 	}
 	
 	private ParserVal compute2(no.roek.nlpged.graph.Node p, no.roek.nlpged.graph.Node... child) {
        for(no.roek.nlpged.graph.Node c : child) {
            graph2.addEdge(new Edge(String.valueOf(edge), p, c, p.getLabel() + " " + c.getLabel()));
            edge++;
        }
        return new ParserVal(p);
    }

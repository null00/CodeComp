package pl.edu.agh.codecomp.comparator;

import java.io.Reader;
import java.io.StringReader;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.lexer.IScanner;
import pl.edu.agh.codecomp.lexer.SimpleScanner;
import pl.edu.agh.codecomp.parser.Parser;
import pl.edu.agh.codecomp.tree.Node;
import edu.ucla.sspace.graph.isomorphism.VF2IsomorphismTester;

public class SourceComparator extends IComparator {
    
    private Node<String, String> treeLeft, treeRight;
    private final String indent = "\t";
	
	public SourceComparator(RSyntaxTextArea left, RSyntaxTextArea right) {
		super();
		this.left = left;
		this.right = right;
		start();
	}

	@Override
	public void run() {
		compare();
	}

	/**
	 * COMPARATOR
	 */

	private void compare() {
		/*treeLeft = getTree(left);
		treeRight = getTree(right);
		
		SparseUndirectedGraph graph = new SparseUndirectedGraph();
        graph.add(new SimpleEdge(1,2));
        graph.add(new SimpleEdge(4,3));
        
        SparseUndirectedGraph graph2 = new SparseUndirectedGraph();
        graph2.add(new SimpleEdge(3,1));*/
	    
	    IScanner scannerLeft = getScanner(left.getText());
	    IScanner scannerRight = getScanner(right.getText());
        Parser parserLeft = new Parser(left, scannerLeft);
        Parser parserRight = new Parser(right, scannerRight);
        parserLeft.run();
        parserRight.run();
        
        VF2IsomorphismTester test = new VF2IsomorphismTester();
        System.out.println("Graph: " + test.areIsomorphic(parserLeft.getGraph(), parserRight.getGraph()));
		
		System.out.println("Tree: " + parserLeft.getTree().equals2(parserRight.getTree()));
	}
	
	private IScanner getScanner(String sourceCode) {
		IScanner scanner = null;
		Reader reader = new StringReader(sourceCode);

		try {

			switch (CompareToken.getSourceComparator().toLowerCase()) {
			case "simple": {
				scanner = new SimpleScanner(reader);
				break;
			}
			/*case "full": {
				scanner = new Scanner(reader);
				break;
			}*/
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
	
	private Node<String, String> getTree(RSyntaxTextArea source) {
	    IScanner scanner = getScanner(source.getText());
        Parser parser = new Parser(source, scanner/*, true*/);
        parser.run();
        return parser.getTree();
	}
	
	private String printTree(Node<String, String> node, String space) {
	    String s = "";
	    s = space + node.toString();
	    for (Node<String, String> child : node.getChildren()) {
	        s += "\n" + printTree(child, space + indent);
	    }
	    return s;
	}
	
	private void compareSize(Node left, Node right) {
	    if(left.getDegree() != right.getDegree()) {
	        System.err.println("left and right not equal");
	        return;
	    }
	}
}

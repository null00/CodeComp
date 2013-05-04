package pl.edu.agh.codecomp.comparator;

import java.io.Reader;
import java.io.StringReader;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.lexer.IScanner;
import pl.edu.agh.codecomp.lexer.SimpleScanner;
import pl.edu.agh.codecomp.parser.Parser;
import pl.edu.agh.codecomp.tree.Node;

public class SourceComparator extends IComparator {
    
    private Node<String, String> tree;
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
		this.scanner = getScanner();
		Parser parser = new Parser(left, right, scanner/*, true*/);
		parser.run();
		tree = parser.getTree();
		System.out.println(printTree(tree, ""));
	}
	
	private IScanner getScanner() {
		IScanner scanner = null;
		Reader reader = new StringReader(left.getText());

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
	
	private String printTree(Node<String, String> node, String space) {
	    String s = "";
	    s = space + node.toString();
	    for (Node<String, String> child : node.getChildren()) {
	        s += "\n" + printTree(child, space + indent);
	    }
	    return s;
	}

}

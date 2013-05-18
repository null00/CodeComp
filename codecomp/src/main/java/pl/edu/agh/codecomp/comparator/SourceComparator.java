package pl.edu.agh.codecomp.comparator;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import no.roek.nlpged.algorithm.GraphEditDistance;
import no.roek.nlpged.application.Config;
import no.roek.nlpged.misc.EditWeightService;
import no.roek.nlpged.preprocessing.DependencyParser;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.analyzer.lexer.IScanner;
import pl.edu.agh.codecomp.analyzer.lexer.SimpleScanner;
import pl.edu.agh.codecomp.analyzer.parser.Parser;
import pl.edu.agh.codecomp.analyzer.tree.Node;
import pl.edu.agh.codecomp.comparator.graph.isomorphism.VF2IsomorphismTester;
import pl.edu.agh.codecomp.gui.dialogs.ScoreDialog;

public class SourceComparator extends IComparator {
    
    private final String indent = "\t";
    private Set<String> functions = createSet("OP_MOV","OP_SMOV","OP_AR","OP_SAR","OP_LOG","OP_SLOG","OP_MISC","OP_JMP","OP_STO","OP_COMP");
    private Set<String> ids = createSet("LAB", "ID");
	
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
        Parser parserLeft = analizeSource(left);
        Parser parserRight = analizeSource(right);
        
        parserLeft.run();
        parserRight.run();
        
        VF2IsomorphismTester test = new VF2IsomorphismTester();
        
        Config cs = new Config("app.properties");
        Map<String, Double> posEditWeights = EditWeightService.getEditWeights(cs.getProperty("POS_SUB_WEIGHTS"), cs.getProperty("POS_INSDEL_WEIGHTS"));
        Map<String, Double> deprelEditWeights = EditWeightService.getInsDelCosts(cs.getProperty("DEPREL_INSDEL_WEIGHTS"));
        GraphEditDistance ged = new GraphEditDistance(parserLeft.getGraph2(), parserRight.getGraph2(), posEditWeights, deprelEditWeights);
        
        new ScoreDialog("Graph2: " + ged.getDistance() + " / " + ged.getNormalizedDistance() + "%", 
                        "Graph: " + test.areIsomorphic(parserLeft.getGraph(), parserRight.getGraph()),
                        "Tree: " + parserLeft.getTree().equals2(parserRight.getTree()));
	}
	
	private Parser analizeSource(RSyntaxTextArea source) {
	    IScanner scanner = getScanner(source.getText());
	    Parser parser = new Parser(source, scanner);
	    return parser;
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
	
	private double analizeMaps(HashMap left, HashMap right) {
	    double score = 0, max = 0, size;
	    HashSet<Node> setLeft = new HashSet<Node>(left.keySet());
	    HashSet<Node> setRight = new HashSet<Node>(right.keySet());
	    if(setLeft.size() >= setRight.size()) {
	        max = countMax(setLeft);
	        size = setLeft.size();
	    } else {
	        max = countMax(setRight);
	        size = setRight.size();
	    }
	    for(Node leftNode: setLeft) {
	        for(Node rightNode : setRight) {
	            if(leftNode.getKey().equals(rightNode.getKey())) {
	                score += 1;
	                if(leftNode.getValue().equals(rightNode.getValue())) {
	                    score += 1;
	                    if(ids.contains(leftNode.getKey())) {
	                        score += 1;
	                    }
	                }
	                break;
	            }
	        }
	    }
	    System.out.println(score + " " + size + "*2 +" + max + " *100");
	    return (score/(max*3+(size-max)*2))*100;
	}
	
	private int countMax(Set<Node> s) {
	    int max = 0;
	    for(Node<String, String> node : s) {
	        if(ids.contains(node.getKey())) {
	            max++;
	        }
 	    }
	    return max;
	}
	
	private Set<String> createSet(String... elem) {
	    HashSet<String> s = new HashSet<String>();
	    for(String ss : elem) {
	        s.add(ss);
	    }
	    return s;
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

package pl.edu.agh.codecomp.comparator;

import java.io.Reader;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.Map;

import no.roek.nlpged.algorithm.GraphEditDistance;
import no.roek.nlpged.application.Config;
import no.roek.nlpged.misc.EditWeightService;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.analyzer.lexer.IScanner;
import pl.edu.agh.codecomp.analyzer.lexer.SimpleScanner;
import pl.edu.agh.codecomp.analyzer.parser.Parser;
import pl.edu.agh.codecomp.comparator.graph.isomorphism.VF2IsomorphismTester;
import pl.edu.agh.codecomp.gui.CodeCompGUI;
import pl.edu.agh.codecomp.gui.dialogs.ScoreDialog;

public class SourceComparator extends IComparator {
	
	public SourceComparator(RSyntaxTextArea left, RSyntaxTextArea right) {
		super();
		this.left = left;
		this.right = right;
		start();
	}

	@Override
	public void run() {
		compare();
		CodeCompGUI.getMainWin().setEnabled(true);
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
        
//        Config cs = new Config("app.properties");
//        Map<String, Double> posEditWeights = EditWeightService.getEditWeights(cs.getProperty("POS_SUB_WEIGHTS"), cs.getProperty("POS_INSDEL_WEIGHTS"));
//        Map<String, Double> deprelEditWeights = EditWeightService.getInsDelCosts(cs.getProperty("DEPREL_INSDEL_WEIGHTS"));
        GraphEditDistance ged = new GraphEditDistance(parserLeft.getGraph2(), parserRight.getGraph2()/*, posEditWeights, deprelEditWeights*/);
        
        DecimalFormat df = new DecimalFormat("#.##");
        double distance = ged.getDistance();
        String sDistance = df.format(distance);
        double pdistance = 100.0 - (distance / ((parserLeft.getGraph2().getSize() > parserRight.getGraph2().getSize()) ? parserLeft.getGraph2().getSize() : parserRight.getGraph2().getSize()) * 100);
        String pDistance = df.format(pdistance);
        new ScoreDialog("Graphs are" + (test.areIsomorphic(parserLeft.getGraph(), parserRight.getGraph()) ? " " : " not ") + "isomorphic",
                        "Distance: " + pDistance + "%" + " [ " + sDistance + " ]" );
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

			switch (getSourceComparator().toLowerCase()) {
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
}

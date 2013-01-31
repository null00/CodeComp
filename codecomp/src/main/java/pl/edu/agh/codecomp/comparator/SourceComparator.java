package pl.edu.agh.codecomp.comparator;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.parser.Parser;

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
	}

	/**
	 * COMPARATOR
	 */

	private void compare() {
		Parser parser = new Parser(left, right);
		parser.run();
	}

}

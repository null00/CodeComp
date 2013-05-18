package pl.edu.agh.codecomp.comparator;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.analyzer.lexer.IScanner;


public abstract class IComparator extends Thread {
	
	protected RSyntaxTextArea left, right;
	
}

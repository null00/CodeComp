package pl.edu.agh.codecomp.comparator;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.lexer.IScanner;


public abstract class IComparator extends Thread {
	
	protected RSyntaxTextArea left, right;
	protected IScanner scanner;
	
}

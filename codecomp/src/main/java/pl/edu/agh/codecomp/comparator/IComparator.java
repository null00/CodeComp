package pl.edu.agh.codecomp.comparator;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.algorithm.IAlgorithm;


public abstract class IComparator extends Thread {
	
	protected RSyntaxTextArea left, right;
	
	protected IAlgorithm getTextComparator() {
	    return CompareToken.getTextAlgorithm();
	}
	
	protected String getSourceComparator() {
	    return CompareToken.getSourceComparator();
	}
	
}

package pl.edu.agh.codecomp.comparator;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public interface IComparator {
	
//	public void showDiff(String text, String pattern);
	public void compare(RSyntaxTextArea left, RSyntaxTextArea right);
	
}

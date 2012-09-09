package pl.edu.agh.codecomp.comparator;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.algorithm.BoyerMoore;

public class Comparator implements IComparator {

	/*
	 * COMPARATOR
	 */

	@Override
	public void compare(RSyntaxTextArea left, RSyntaxTextArea right) {
		try {
			System.out.println(left.getText().isEmpty() + " / " + right.getText().isEmpty());
			if (!left.getText().isEmpty() && !right.getText().isEmpty()) {
				String text = right.getText();
				String words[] = text.split("\n");
				for (String word : words) {
					if (!word.isEmpty()) {
						BoyerMoore bm = new BoyerMoore(left.getText(), word);
						List<Integer> list = bm.match();
						System.out.println("word: " + word + "matches: " + list.size());
						Highlighter leftHL = left.getHighlighter();
						Highlighter rightHL = right.getHighlighter();
						Iterator<Integer> it = list.iterator();
						while (it.hasNext()) {
							int i = it.next();
							Color color = Color.getHSBColor((float) Math.random() * 255 + 1, (float) Math.random() * 255 + 1, (float) Math.random() * 255 + 1);
							leftHL.addHighlight(i, i + word.length(), new DefaultHighlighter.DefaultHighlightPainter(color));
							rightHL.addHighlight(i, i + word.length(), new DefaultHighlighter.DefaultHighlightPainter(color));
						}
					}
				}
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

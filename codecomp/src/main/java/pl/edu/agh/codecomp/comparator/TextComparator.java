package pl.edu.agh.codecomp.comparator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.algorithm.IAlgorithm;

public class TextComparator extends IComparator {
	
	public TextComparator(RSyntaxTextArea left, RSyntaxTextArea right) {
		super();
		this.left = left;
		this.right = right;
		start();
	}

	@Override
	public void run() {
		compare();
	}

	/*
	 * COMPARATOR
	 */

	// FIXME: SPRAWDZIC I POPRAWIC/ZMIENIC MECHANIZM POROWNYWANIA
	private void compare() {
		try {
			IAlgorithm algo = getTextComparator();
			
			if (algo != null && !left.getText().isEmpty() && !right.getText().isEmpty()) {
				String text = right.getText();
				
				StringTokenizer st = new StringTokenizer(left.getText(), " \t\r\f\n]");
				
				int lineNo = 0;
				while(st.hasMoreTokens()) {
				    ArrayList<Integer> l = new ArrayList<Integer>();
					String word = st.nextToken();
					if (!word.isEmpty() && !word.equals("") && !word.equals(" ") && !word.equals("\n")) {
						List<Integer> list = algo.match(text, word.trim());
						
						Highlighter leftHL = left.getHighlighter();
						Highlighter rightHL = right.getHighlighter();
						Iterator<Integer> it = list.iterator();
						
						while (it.hasNext()) {
                            int i = it.next();
                            if (!l.contains(i)) {
                                Color color = Color.orange;
                                leftHL.addHighlight(lineNo, lineNo + word.length(), new DefaultHighlighter.DefaultHighlightPainter(color));
                                rightHL.addHighlight(i, i + word.length(), new DefaultHighlighter.DefaultHighlightPainter(color));
                                l.add(i);
                            }
						}
						lineNo += word.length() + 1;
					}
				}
			}
		} catch (Exception e) {
			// TODO LOGGER
			e.printStackTrace();
		}
	}

	private Map<Integer, String> wordIndexPicker(String text) {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		
		int index = 0;
		String pattern = "";
		for(int i = 0; i < text.length(); ++i) {
			char c = text.charAt(i);
			if(c != ' ' || c != "\n".charAt(0)) {
				pattern += c;
			} else {
				map.put(index, pattern);
				index = index + i + 1;
				pattern = "";
			}
			if(i == text.length() - 1)
				map.put(index, pattern);
		}
		return map;
	}
}

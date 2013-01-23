package pl.edu.agh.codecomp.comparator;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.algorithm.IAlgorithm;
import pl.edu.agh.codecomp.gui.CodeCompGUI;

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
			IAlgorithm algo = CodeCompGUI.getSelectedAlgo();
			
			System.out.println(algo.getName() + ": " + left.getText().isEmpty() + " / " + right.getText().isEmpty());
			
			if (algo != null && !left.getText().isEmpty() && !right.getText().isEmpty()) {
				String text = left.getText();
				
//				Map<Integer, String> map = wordIndexPicker(right.getText());
				
				String words[] = right.getText().split("\n");
				
				int lineNo = 0;
				for (String word : words) {
//				for(Map.Entry<Integer, String> entry : map.entrySet()) {
//					Integer index = entry.getKey();
//					String word = entry.getValue();
					
					System.out.println("\nWord: " + word);
					if (!word.isEmpty() && !word.equals("") && !word.equals(" ") && !word.equals("\n")) {
						List<Integer> list = algo.match(text, word.trim());
						System.out.println("Matches: " + list.size());
						
						Highlighter leftHL = left.getHighlighter();
						Highlighter rightHL = right.getHighlighter();
						Iterator<Integer> it = list.iterator();
						
						while (it.hasNext()) {
							int i = it.next();
							
//							Color color = Color.getHSBColor((float) Math.random() * 255 + 1, (float) Math.random() * 255 + 1, (float) Math.random() * 255 + 1);
							Color color = Color.orange;
							
							leftHL.addHighlight(i, i + word.length(), new DefaultHighlighter.DefaultHighlightPainter(color));
							
							rightHL.addHighlight(lineNo, lineNo + word.length() + 1, new DefaultHighlighter.DefaultHighlightPainter(color));
//							rightHL.addHighlight(index, index + word.length(), new DefaultHighlighter.DefaultHighlightPainter(color));
							System.out.println("Text: " + i + " | Pat:" + lineNo);
						}
						lineNo += word.length() + 1;
					}
				}
				System.out.println("Stopped Comparing");
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

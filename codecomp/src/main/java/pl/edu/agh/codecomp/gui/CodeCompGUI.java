package pl.edu.agh.codecomp.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import pl.edu.agh.codecomp.algorithm.BoyerMoore;
import pl.edu.agh.codecomp.file.CCFileReader;
import pl.edu.agh.codecomp.file.actions.AddFileAction;
import pl.edu.agh.codecomp.file.actions.ChangeTextListener;
import pl.edu.agh.codecomp.file.actions.CompareAction;

public class CodeCompGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String TITLE = "CodeComp - Source Code Comparator";
	private static final Dimension MAIN_WIN_DIMENSION = new Dimension(800, 600);

	private static final Logger log = Logger.getLogger("CodeCompGUI");
	private static CodeCompGUI mainWin;

	public CodeCompGUI() {
		super();
	}

	public static void createGUI() {
		if (mainWin == null) {
			mainWin = new CodeCompGUI();
			initGUI();
			initMenu();
			initMainPanel();

			try {
				leftText.read(CCFileReader.read(new File("/home/null/Dokumenty/dieta").getAbsolutePath()), null);
				rightText.read(CCFileReader.read(new File("/home/null/Dokumenty/dieta2").getAbsolutePath()), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * UI SETTINGS
	 */

	private static void initGUI() {
		getMainWin().setTitle(TITLE);
		getMainWin().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getMainWin().setLayout(new BorderLayout());
		getMainWin().setSize(MAIN_WIN_DIMENSION);
		getMainWin().setVisible(true);
		getMainWin().setLocationRelativeTo(null);
	}

	private static void initMenu() {
		JMenuBar mainMenu = new JMenuBar();
		JMenu file = new JMenu("File");
		mainMenu.add(file);

		JMenuItem addLeftItem = new JMenuItem("Add left");
		addLeftItem.addActionListener(new AddFileAction());
		addLeftItem.setActionCommand("leftText");
		file.add(addLeftItem);

		JMenuItem addRightItem = new JMenuItem("Add right");
		addRightItem.addActionListener(new AddFileAction());
		addRightItem.setActionCommand("rightText");
		file.add(addRightItem);

		JMenu compare = new JMenu("Compare");
		compare.addActionListener(new CompareAction());
		mainMenu.add(compare);

		getMainWin().setJMenuBar(mainMenu);
	}

	private static RSyntaxTextArea leftText;
	private static RSyntaxTextArea rightText;

	private static void initMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 2));

		leftText = new RSyntaxTextArea();
		leftText.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		leftText.setCodeFoldingEnabled(true);
		leftText.setAntiAliasingEnabled(true);
		leftText.setBorder(BorderFactory.createLineBorder(Color.blue));
		leftText.setEditable(false);
		leftText.setCurrentLineHighlightColor(Color.white);
		leftText.addCaretListener(new ChangeTextListener());
		final RTextScrollPane leftScrollPane = new RTextScrollPane(leftText);
		leftScrollPane.setFoldIndicatorEnabled(true);
		leftScrollPane.setAutoscrolls(false);
		mainPanel.add(leftScrollPane);

		rightText = new RSyntaxTextArea();
		rightText.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		rightText.setCodeFoldingEnabled(true);
		rightText.setAntiAliasingEnabled(true);
		rightText.setBorder(BorderFactory.createLineBorder(Color.orange));
		rightText.setEditable(false);
		rightText.addCaretListener(new ChangeTextListener());
		
		final RTextScrollPane rightScrollPane = new RTextScrollPane(rightText);
		rightScrollPane.setFoldIndicatorEnabled(true);
//		rightScrollPane.getVerticalScrollBar().setModel(leftScrollPane.getVerticalScrollBar().getModel());

		mainPanel.add(rightScrollPane);

		getMainWin().add(mainPanel, BorderLayout.CENTER);
	}

	/*
	 * VIEW FILE OPTIONS
	 */

	public static void setLeftFile(Reader reader) {
		try {
			leftText.read(reader, null);
		} catch (IOException e) {
			// TODO Logger
			e.printStackTrace();
		}
	}

	public static void setRightFile(Reader reader) {
		try {
			rightText.read(reader, null);
		} catch (IOException e) {
			// TODO Logger
			e.printStackTrace();
		}
	}

	/*
	 * COMPARATOR
	 */

	public static void compareTextAreas() {
		compare(leftText, rightText);
	}

	private static void compare(RSyntaxTextArea left, RSyntaxTextArea right) {
		try {
			System.out.println(left.getText().isEmpty() + " / " + rightText.getText().isEmpty());
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

	/*
	 * GETTERS AND SETTERS
	 */

	public static JFrame getMainWin() {
		return mainWin;
	}

}
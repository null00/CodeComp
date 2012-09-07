package pl.edu.agh.codecomp.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import pl.edu.agh.codecomp.algorithm.BoyerMoore;
import pl.edu.agh.codecomp.file.actions.AddFileAction;

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

		JMenuItem addItem = new JMenuItem("Add");
		addItem.addActionListener(new AddFileAction());
		file.add(addItem);

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
		RTextScrollPane leftScrollPane = new RTextScrollPane(leftText);
		leftScrollPane.setFoldIndicatorEnabled(true);
		mainPanel.add(leftScrollPane);

		rightText = new RSyntaxTextArea();
		rightText.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		rightText.setCodeFoldingEnabled(true);
		rightText.setAntiAliasingEnabled(true);
		rightText.setBorder(BorderFactory.createLineBorder(Color.orange));
		rightText.setEditable(false);
		RTextScrollPane rightScrollPane = new RTextScrollPane(rightText);
		rightScrollPane.setFoldIndicatorEnabled(true);
		mainPanel.add(rightScrollPane);

		getMainWin().add(mainPanel, BorderLayout.CENTER);
	}

	/*
	 * VIEW FILE OPTIONS
	 */

	public static void setLeftFile(Reader reader) {
		try {
//			leftText.read(reader, null);
			
			leftText.read(reader, null);
			rightText.setText(leftText.getText(10, 30));
			BoyerMoore bm = new BoyerMoore(leftText.getText(), rightText.getText());
			List<Integer> list = bm.match();
			System.out.println("match: " + list.size());
			Highlighter h = leftText.getHighlighter();
			Iterator<Integer> it = list.iterator();
			while(it.hasNext()) {
				int i = it.next();
				h.addHighlight(i, i+rightText.getText().length(), new DefaultHighlighter.DefaultHighlightPainter(Color.red));
			}
			
			// Highlighter hl = leftText.getHighlighter();
			// hl.addHighlight(0, 10, new
			// BackgroundHighlighter.BackgroundHighlightPainter(Color.yellow));
			// hl.addHighlight(20, 30, new
			// BackgroundHighlighter.BackgroundHighlightPainter(Color.blue));
			
		} catch (IOException e) {
			// TODO Logger
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
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
	 * GETTERS AND SETTERS
	 */

	public static JFrame getMainWin() {
		return mainWin;
	}

}
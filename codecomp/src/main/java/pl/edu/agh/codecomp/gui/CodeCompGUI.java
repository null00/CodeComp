package pl.edu.agh.codecomp.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import pl.edu.agh.codecomp.algorithm.BoyerMoore;
import pl.edu.agh.codecomp.algorithm.IAlgorithm;
import pl.edu.agh.codecomp.algorithm.KarpRabin;
import pl.edu.agh.codecomp.file.CCFileReader;
import pl.edu.agh.codecomp.file.actions.AddFileAction;
import pl.edu.agh.codecomp.file.actions.CompareAction;
import pl.edu.agh.codecomp.filter.Filter;
import pl.edu.agh.codecomp.gui.actions.AddFilesAction;

public class CodeCompGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String TITLE = "CodeComp - Source Code Comparator";
	private static final Dimension MAIN_WIN_DIMENSION = new Dimension(1024, 768);
	private static final Logger log = Logger.getLogger("CodeCompGUI");

	private static CodeCompGUI mainWin;
	private static IAlgorithm[] algoList = { new BoyerMoore(), new KarpRabin() };
	private static JComboBox<IAlgorithm> switchAlgo;

	public CodeCompGUI() {
		super();
	}

	public static void createGUI() {
		if (mainWin == null) {
			mainWin = new CodeCompGUI();
			initGUI();
			initMenu();
			initMainPanel();

			String path = System.getProperty("user.dir") + "/src/main/java/";

			try {
				leftText.read(CCFileReader.read(new File(path + "source1.asm").getAbsolutePath()), null);
//				rightText.read(CCFileReader.read(new File(path + "source2.asm").getAbsolutePath()), null);
//				leftText.setText(Filter.parse(leftText.getText()));
				rightText.setText(Filter.parse(leftText.getText()));
			} catch (IOException e) {
				// TODO LOGGER
				// e.printStackTrace();
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
		getMainWin().setLocationRelativeTo(null);
		getMainWin().setVisible(true);
	}

	private static void initMenu() {
		JMenuBar mainMenu = new JMenuBar();
		JMenu file = new JMenu("File");
		mainMenu.add(file);

		JMenuItem addFilesItem = new JMenuItem("Add files");
		addFilesItem.addActionListener(new AddFilesAction());
		addFilesItem.setActionCommand("showDialog");
		file.add(addFilesItem);

		JMenuItem addLeftItem = new JMenuItem("Add left");
		addLeftItem.addActionListener(new AddFileAction());
		addLeftItem.setActionCommand("leftText");
		file.add(addLeftItem);

		JMenuItem addRightItem = new JMenuItem("Add right");
		addRightItem.addActionListener(new AddFileAction());
		addRightItem.setActionCommand("rightText");
		file.add(addRightItem);

		JMenu run = new JMenu("Run");
		mainMenu.add(run);
		
		JMenuItem compare = new JMenuItem("Compare");
		compare.addActionListener(new CompareAction());
		run.add(compare);
		
		mainMenu.add(Box.createHorizontalGlue());

		switchAlgo = new JComboBox<IAlgorithm>(algoList);
		mainMenu.add(switchAlgo);

		getMainWin().setJMenuBar(mainMenu);
	}

	private static RTextScrollPane rightScrollPane;
	private static RTextScrollPane leftScrollPane;
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
		// leftText.setEditable(false);
		leftText.setCurrentLineHighlightColor(Color.white);

		leftScrollPane = new RTextScrollPane(leftText);
		leftScrollPane.setFoldIndicatorEnabled(true);
		leftScrollPane.setAutoscrolls(false);
		mainPanel.add(leftScrollPane);

		rightText = new RSyntaxTextArea();
		rightText.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		rightText.setCodeFoldingEnabled(true);
		rightText.setAntiAliasingEnabled(true);
		rightText.setBorder(BorderFactory.createLineBorder(Color.orange));
		rightText.setCurrentLineHighlightColor(Color.white);
		// rightText.setEditable(false);

		rightScrollPane = new RTextScrollPane(rightText);
		rightScrollPane.setFoldIndicatorEnabled(true);
		// rightScrollPane.getVerticalScrollBar().setModel(leftScrollPane.getVerticalScrollBar().getModel());
		// rightScrollPane.getHorizontalScrollBar().setModel(leftScrollPane.getHorizontalScrollBar().getModel());

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
			// TODO LOGGER
			e.printStackTrace();
		} finally {
			JScrollBar bar = leftScrollPane.getVerticalScrollBar();
			bar.setValue(0);
		}
	}

	public static void setRightFile(Reader reader) {
		try {
			rightText.read(reader, null);
		} catch (IOException e) {
			// TODO LOGGER
			e.printStackTrace();
		} finally {
			JScrollBar bar = rightScrollPane.getVerticalScrollBar();
			bar.setValue(bar.getMinimum());
		}
	}

	/*
	 * GETTERS AND SETTERS
	 */

	public static JFrame getMainWin() {
		return mainWin;
	}

	public static RSyntaxTextArea getLeftText() {
		return leftText;
	}

	public static RSyntaxTextArea getRightText() {
		return rightText;
	}

	public static IAlgorithm getSelectedAlgo() {
		return (IAlgorithm) switchAlgo.getSelectedItem();
	}
}
package pl.edu.agh.codecomp.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Reader;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import pl.edu.agh.codecomp.comparator.actions.CompareAction;
import pl.edu.agh.codecomp.file.CCFileReader;
import pl.edu.agh.codecomp.file.actions.AddFileAction;
import pl.edu.agh.codecomp.filter.Filter;
import pl.edu.agh.codecomp.gui.actions.AddFilesAction;
import pl.edu.agh.codecomp.gui.actions.ClearHighlightAction;
import pl.edu.agh.codecomp.gui.actions.OptionsDialogAction;

public class CodeCompGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String TITLE = "CodeComp - Source Code Comparator";
	private static final Dimension MAIN_WIN_DIMENSION = new Dimension(1024, 768);
//	private static final Logger log = Logger.getLogger("CodeCompGUI");

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

			String path = System.getProperty("user.dir") + "/src/main/java/pl/edu/agh/codecomp/resources/";

			try {
//				setLeftFile(CCFileReader.read(new File(path + "source1.asm").getAbsolutePath()));
				setLeftFile(CCFileReader.read(new File(path + "source3.asm").getAbsolutePath()));
//				rightText.read(CCFileReader.read(new File(path + "source2.asm").getAbsolutePath()), null);
				
				setLeftFile(Filter.parse(leftText.getText()));
//				setRightFile(Filter.parse(leftText.getText()));
				
//				setLeftFile("gcatcgcagagagtatacagtacg");
//				setRightFile("gcagagag");
			} catch (Exception e) {
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

		file.addSeparator();
		
		JMenuItem optionsItem = new JMenuItem("Preferences");
		optionsItem.addActionListener(new OptionsDialogAction());
		optionsItem.setActionCommand("showDialog");
		file.add(optionsItem);
		
		file.addSeparator();
		
		JMenuItem closeItem = new JMenuItem("Close");
		closeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CodeCompGUI.getMainWin().dispose();
			}
		});
		file.add(closeItem);
		
		JMenu comparator = new JMenu("Comparator");
		mainMenu.add(comparator);
		
		JMenuItem compText = new JMenuItem("Compare text");
		compText.setActionCommand("text");
		compText.addActionListener(new CompareAction());
		comparator.add(compText);
		
		JMenuItem compSource = new JMenuItem("Compare source");
		compSource.setActionCommand("source");
		compSource.addActionListener(new CompareAction());
		comparator.add(compSource);
		
		comparator.addSeparator();
		
		JMenuItem clear = new JMenuItem("Clear");
		clear.addActionListener(new ClearHighlightAction());
		comparator.add(clear);
		
		mainMenu.add(Box.createHorizontalGlue());
		mainMenu.add(Box.createHorizontalGlue());

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
		leftText.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
		leftText.setCodeFoldingEnabled(true);
		leftText.setAntiAliasingEnabled(true);
		leftText.setBorder(BorderFactory.createLineBorder(Color.blue));
		// leftText.setEditable(false);
		leftText.setCurrentLineHighlightColor(Color.white);
		leftText.setDoubleBuffered(true);
		
		leftScrollPane = new RTextScrollPane(leftText);
		leftScrollPane.setFoldIndicatorEnabled(true);
		leftScrollPane.setAutoscrolls(false);
		mainPanel.add(leftScrollPane);

		rightText = new RSyntaxTextArea();
		rightText.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
		rightText.setCodeFoldingEnabled(true);
		rightText.setAntiAliasingEnabled(true);
		rightText.setBorder(BorderFactory.createLineBorder(Color.orange));
		rightText.setCurrentLineHighlightColor(Color.white);

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
			leftText.setCaretPosition(0);
			leftScrollPane.getVerticalScrollBar().setValue(0);
		}
	}

	public static void setRightFile(Reader reader) {
		try {
			rightText.read(reader, null);
		} catch (IOException e) {
			// TODO LOGGER
			e.printStackTrace();
		} finally {
			rightText.setCaretPosition(0);
			rightScrollPane.getVerticalScrollBar().setValue(0);
		}
	}
	
	public static void setLeftFile(String text) {
		leftText.setText(text);
		leftText.setCaretPosition(0);
		leftScrollPane.getVerticalScrollBar().setValue(0);
	}

	public static void setRightFile(String text) {
		rightText.setText(text);
		rightText.setCaretPosition(0);
		rightScrollPane.getVerticalScrollBar().setValue(0);
	}
	
	public static void clearAllHightlights() {
		leftText.getHighlighter().removeAllHighlights();
		rightText.getHighlighter().removeAllHighlights();
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
}
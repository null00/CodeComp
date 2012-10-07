package pl.edu.agh.codecomp.gui;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.jhlabs.awt.layouts.ParagraphLayout;

public class AddFilesDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final String TITLE = "Add files to compare";
	private final Dimension DIALOG_DIMENSION = new Dimension(500, 250);

	public AddFilesDialog() {
		super();
		initDialog();
	}

	private void initDialog() {
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLayout(new ParagraphLayout());
		this.setSize(DIALOG_DIMENSION);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		JLabel leftFileLabel = new JLabel("Left file path:");
		this.add(leftFileLabel, ParagraphLayout.NEW_PARAGRAPH);

		JTextField leftFilePath = new JTextField();
		this.add(leftFilePath, ParagraphLayout.NEW_LINE_STRETCH_H);
		
		JLabel rightFileLabel = new JLabel("Right file path:");
		this.add(rightFileLabel, ParagraphLayout.NEW_PARAGRAPH);

		JTextField rightFilePath = new JTextField();
		this.add(rightFilePath, ParagraphLayout.NEW_LINE_STRETCH_H);
	}
}

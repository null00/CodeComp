package pl.edu.agh.codecomp.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jhlabs.awt.layouts.ParagraphLayout;

public class AddFilesDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final String TITLE = "Add files to compare";
	private final Dimension DIALOG_DIMENSION = new Dimension(600, 200);
	private final Dimension BUTTON_DIMENSION = new Dimension(100, 25);

	private JTextField leftFilePath, rightFilePath;
	private File leftFile, rightFile;

	public AddFilesDialog() {
		super();
		initDialog();
	}

	private void initDialog() {
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setMinimumSize(DIALOG_DIMENSION);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		JPanel selectFilesPanel = new JPanel(new ParagraphLayout());
		selectFilesPanel.setBorder(BorderFactory.createTitledBorder("Files to compare"));
		
		JLabel leftFileLabel = new JLabel("Left file path:");
		selectFilesPanel.add(leftFileLabel, ParagraphLayout.NEW_PARAGRAPH);

		leftFilePath = new JTextField();
		selectFilesPanel.add(leftFilePath, ParagraphLayout.NEW_LINE_STRETCH_H);

		JButton leftFileButton = new JButton("Browse...");
		leftFileButton.setPreferredSize(BUTTON_DIMENSION);
		leftFileButton.addActionListener(this);
		leftFileButton.setActionCommand("leftFile");
		selectFilesPanel.add(leftFileButton);

		JLabel rightFileLabel = new JLabel("Right file path:");
		selectFilesPanel.add(rightFileLabel, ParagraphLayout.NEW_PARAGRAPH);

		rightFilePath = new JTextField();
		selectFilesPanel.add(rightFilePath, ParagraphLayout.NEW_LINE_STRETCH_H);

		JButton rightFileButton = new JButton("Browse...");
		rightFileButton.setPreferredSize(BUTTON_DIMENSION);
		rightFileButton.addActionListener(this);
		rightFileButton.setActionCommand("rightFile");
		selectFilesPanel.add(rightFileButton);
		
		this.getContentPane().add(selectFilesPanel, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JButton okButton = new JButton("OK");
		okButton.setPreferredSize(BUTTON_DIMENSION);
		okButton.addActionListener(this);
		okButton.setActionCommand("addFiles");
		buttonsPanel.add(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(BUTTON_DIMENSION);
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");
		buttonsPanel.add(cancelButton);
		
		this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		this.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		switch (actionCommand) {
		case "leftFile": {
			File tempFile = addFile();
			if (tempFile != null) {
				leftFile = tempFile;
				leftFilePath.setText(leftFile.getAbsolutePath());
			}
			break;
		}
		case "rightFile": {
			File tempFile = addFile();
			if (tempFile != null) {
				rightFile = tempFile;
				rightFilePath.setText(rightFile.getAbsolutePath());
			}
			break;
		}
		case "addFiles": {
			break;
		}
		case "cancel": {
			this.dispose();
			break;
		}
		}
	}

	private File addFile() {
		File file = null;
		try {
			JFileChooser jc = new JFileChooser("Add file");
			jc.setApproveButtonText("Add");
			// jc.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
			int ret = jc.showOpenDialog(this);
			if (ret == JFileChooser.APPROVE_OPTION) {
				file = jc.getSelectedFile();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return file;
	}
}

package pl.edu.agh.codecomp.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import pl.edu.agh.codecomp.file.CCFileReader;

import com.jhlabs.awt.layouts.ParagraphLayout;

public class OptionsDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final String TITLE = "Comparator options";
	private final Dimension DIALOG_DIMENSION = new Dimension(600, 400);
	private final Dimension BUTTON_DIMENSION = new Dimension(100, 25);

	public OptionsDialog() {
		super();
		initDialog();
	}
	
	private JCheckBox enableFilter;

	private void initDialog() {
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setMinimumSize(DIALOG_DIMENSION);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		JPanel optionsPanel = new JPanel(new ParagraphLayout());
		optionsPanel.setBorder(BorderFactory.createTitledBorder("Filter options"));

		enableFilter = new JCheckBox("Source filtering enabled");
		enableFilter.setSelected(CCFileReader.getFilter());
		optionsPanel.add(enableFilter);

		this.getContentPane().add(optionsPanel, BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton okButton = new JButton("OK");
		okButton.setPreferredSize(BUTTON_DIMENSION);
		okButton.addActionListener(this);
		okButton.setActionCommand("save");
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
		case "save": {
			CCFileReader.setFilter(enableFilter.isSelected());
			this.dispose();
			break;
		}
		case "cancel": {
			this.dispose();
			break;
		}
		}

	}
}

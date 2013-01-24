package pl.edu.agh.codecomp.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import pl.edu.agh.codecomp.algorithm.IAlgorithm;
import pl.edu.agh.codecomp.comparator.CompareToken;
import pl.edu.agh.codecomp.file.CCFileReader;

import com.jhlabs.awt.layouts.ParagraphLayout;

public class OptionsDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final String TITLE = "Comparator options";
	private final Dimension DIALOG_DIMENSION = new Dimension(600, 400);
	private final Dimension MAX_DIALOG_DIMENSION = new Dimension(1280, 1024);
	private final Dimension BUTTON_DIMENSION = new Dimension(100, 25);

	public OptionsDialog() {
		super();
		initDialog();
	}

	private JCheckBox enableFilter;
	private JComboBox<IAlgorithm> switchAlgo;

	private void initDialog() {
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setMinimumSize(DIALOG_DIMENSION);
		this.getContentPane().setMaximumSize(MAX_DIALOG_DIMENSION);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		JPanel optionsPanel = new JPanel(new GridLayout(1, 3));
		optionsPanel.setBorder(BorderFactory.createTitledBorder("Main options"));

		JPanel filterPanel = new JPanel(new ParagraphLayout());
		filterPanel.setBorder(BorderFactory.createTitledBorder("Filter options"));
		enableFilter = new JCheckBox("Source filtering enabled");
		enableFilter.setSelected(CCFileReader.getFilter());
		filterPanel.add(enableFilter);

		optionsPanel.add(filterPanel);

		JPanel textCompPanel = new JPanel(new ParagraphLayout());
		textCompPanel.setBorder(BorderFactory.createTitledBorder("Text comparator options"));

		switchAlgo = new JComboBox<IAlgorithm>(CompareToken.getAlgoList());
		switchAlgo.setSelectedItem(CompareToken.getTextAlgorithm());
//		switchAlgo.setMaximumSize(new Dimension(300, 20));
//		switchAlgo.setPreferredSize(new Dimension(100, 20));
		textCompPanel.add(switchAlgo);

		optionsPanel.add(textCompPanel);

		JPanel sourceCompPanel = new JPanel(new ParagraphLayout());
		sourceCompPanel.setBorder(BorderFactory.createTitledBorder("Source comparator options"));

		optionsPanel.add(sourceCompPanel);

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
			CompareToken.setTextAlgorithm((IAlgorithm) switchAlgo.getSelectedItem());
			this.dispose();
			break;
		}
		case "cancel": {
			this.dispose();
			break;
		}
		}

	}

	/*
	@Override
	public Dimension getPreferredSize() {
		int maxWidth = MAX_DIALOG_DIMENSION.width;
		int maxHeight = MAX_DIALOG_DIMENSION.height;
		Dimension dim = super.getPreferredSize();
		if (dim.width > maxWidth)
			dim.width = maxWidth;
		if (dim.height > maxHeight)
			dim.height = maxHeight;
		return dim;
	}
	*/
}

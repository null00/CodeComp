package pl.edu.agh.codecomp.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import pl.edu.agh.codecomp.algorithm.IAlgorithm;
import pl.edu.agh.codecomp.comparator.CompareToken;

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
	private JCheckBox lowerCaseFilter;
	
	private JComboBox<IAlgorithm> switchAlgo;
	private JComboBox<String> switchComp;

	private void initDialog() {
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setMinimumSize(DIALOG_DIMENSION);
		this.getContentPane().setMaximumSize(MAX_DIALOG_DIMENSION);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		/* ### MAIN OPTIONS PANEL ### */
		JPanel optionsPanel = new JPanel(new GridLayout(1, 6));
//		optionsPanel.setBorder(BorderFactory.createTitledBorder("Main options"));

		/* ### FILTER OPTIONS PANEL ### */
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
		filterPanel.setBorder(BorderFactory.createTitledBorder("Filter options"));
		/* Enable filter option */
		enableFilter = new JCheckBox("Source filtering enabled");
		enableFilter.setSelected(CompareToken.getFilter());
		filterPanel.add(enableFilter);
		/* Transform source code to lowercase option */
		lowerCaseFilter = new JCheckBox("Source code to lowercase");
		lowerCaseFilter.setSelected(CompareToken.getToLowerCase());
		filterPanel.add(lowerCaseFilter);

		optionsPanel.add(filterPanel);

		/* ### TEXT COMPARATOR OPTIONS ### */
		JPanel textCompPanel = new JPanel(new ParagraphLayout());
		textCompPanel.setBorder(BorderFactory.createTitledBorder("Text comparator options"));
		/* Text algorithms combobox */
		switchAlgo = new JComboBox<IAlgorithm>(CompareToken.getAlgoList());
		switchAlgo.setSelectedItem(CompareToken.getTextAlgorithm());
//		switchAlgo.setMaximumSize(new Dimension(300, 20));
//		switchAlgo.setPreferredSize(new Dimension(100, 20));
		textCompPanel.add(switchAlgo);

		optionsPanel.add(textCompPanel);

		/* ### SOURCE COMPARATOR OPTIONS ### */ 
		JPanel sourceCompPanel = new JPanel(new ParagraphLayout());
		sourceCompPanel.setBorder(BorderFactory.createTitledBorder("Source comparator options"));
		/* Source parser combobox */
		switchComp = new JComboBox<String>(CompareToken.getCompList());
		switchComp.setSelectedItem(CompareToken.getSourceComparator());
		sourceCompPanel.add(switchComp);
		
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
			CompareToken.setFilter(enableFilter.isSelected());
			CompareToken.setToLowerCase(lowerCaseFilter.isSelected());
			CompareToken.setSourceComparator((String) switchComp.getSelectedItem());
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

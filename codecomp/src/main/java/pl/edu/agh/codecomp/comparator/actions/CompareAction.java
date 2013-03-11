package pl.edu.agh.codecomp.comparator.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.edu.agh.codecomp.comparator.SourceComparator;
import pl.edu.agh.codecomp.comparator.TextComparator;
import pl.edu.agh.codecomp.gui.CodeCompGUI;

public class CompareAction implements ActionListener {//, CaretListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		switch(action) {
		case "text": {
			compareTextAction();
			break;
		}
		case "source": {
		    CodeCompGUI.getRightText().setText("");
			compareSourceAction();
			break;
		}
		}
	}

	private void compareTextAction() {
		new TextComparator(CodeCompGUI.getLeftText(), CodeCompGUI.getRightText());
	}
	
	private void compareSourceAction() {
		new SourceComparator(CodeCompGUI.getLeftText(), CodeCompGUI.getRightText());
	}
}

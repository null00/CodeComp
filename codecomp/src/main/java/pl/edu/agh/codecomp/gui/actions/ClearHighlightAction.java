package pl.edu.agh.codecomp.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.edu.agh.codecomp.gui.CodeCompGUI;

public class ClearHighlightAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		CodeCompGUI.clearAllHightlights();
	}

}

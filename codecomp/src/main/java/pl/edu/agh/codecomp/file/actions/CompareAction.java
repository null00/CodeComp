package pl.edu.agh.codecomp.file.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.edu.agh.codecomp.gui.CodeCompGUI;

public class CompareAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		CodeCompGUI.compareTextAreas();
	}

}

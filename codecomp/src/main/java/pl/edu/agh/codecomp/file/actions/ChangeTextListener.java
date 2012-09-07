package pl.edu.agh.codecomp.file.actions;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import pl.edu.agh.codecomp.gui.CodeCompGUI;

public class ChangeTextListener implements CaretListener {

	@Override
	public void caretUpdate(CaretEvent arg0) {
		CodeCompGUI.compareTextAreas();
	}

	

}

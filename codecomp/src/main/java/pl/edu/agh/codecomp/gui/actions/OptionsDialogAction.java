package pl.edu.agh.codecomp.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.edu.agh.codecomp.gui.OptionsDialog;

public class OptionsDialogAction implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		switch (actionCommand) {
		case "showDialog": {
			new OptionsDialog();
			break;
		}
		}
	}
	
}

package pl.edu.agh.codecomp.file.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import pl.edu.agh.codecomp.algorithm.KarpRabin;
import pl.edu.agh.codecomp.comparator.TextComparator;
import pl.edu.agh.codecomp.gui.CodeCompGUI;

public class CompareAction implements ActionListener, CaretListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		compareAction();
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		compareAction();
	}

	private void compareAction() {
		new TextComparator(CodeCompGUI.getLeftText(), CodeCompGUI.getRightText());
	}
}

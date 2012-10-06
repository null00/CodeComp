package pl.edu.agh.codecomp.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import pl.edu.agh.codecomp.algorithm.BoyerMoore;
import pl.edu.agh.codecomp.algorithm.IAlgorithm;
import pl.edu.agh.codecomp.algorithm.KarpRabin;
import pl.edu.agh.codecomp.gui.CodeCompGUI;

public class SwitchAlgorithmAction implements ActionListener {

	@Override
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		JComboBox<String> cb = (JComboBox<String>) e.getSource();
		String algo = (String) cb.getSelectedItem();
		IAlgorithm selectedAlgo = null;

		switch (algo) {
		case BoyerMoore.NAME: {
			selectedAlgo = new BoyerMoore();
			break;
		}
		case KarpRabin.NAME: {
			selectedAlgo = new KarpRabin();
			break;
		}
		}

		CodeCompGUI.algo = selectedAlgo;
	}
}

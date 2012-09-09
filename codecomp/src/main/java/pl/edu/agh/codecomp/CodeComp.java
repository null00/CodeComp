package pl.edu.agh.codecomp;

import javax.swing.UIManager;

import pl.edu.agh.codecomp.gui.CodeCompGUI;

/**
 * @author Jaroslaw Szczesniak (null@student.agh.edu.pl)
 * 
 */
public class CodeComp {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					CodeCompGUI.createGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

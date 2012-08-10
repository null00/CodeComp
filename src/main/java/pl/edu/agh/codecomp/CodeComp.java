package pl.edu.agh.codecomp;

import javax.swing.UIManager;

import pl.edu.agh.codecomp.gui.CodeCompGUI;

/**
 * @author Jarosław Szczęśniak
 * 
 */
public class CodeComp {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
					CodeCompGUI.createGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

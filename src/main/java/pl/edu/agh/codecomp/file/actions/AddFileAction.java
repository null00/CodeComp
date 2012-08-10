package pl.edu.agh.codecomp.file.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import pl.edu.agh.codecomp.file.CCFileReader;
import pl.edu.agh.codecomp.gui.CodeCompGUI;

public class AddFileAction implements ActionListener {

	public void actionPerformed(ActionEvent actionEvent) {
		try {
		JFileChooser jc = new JFileChooser("Add file");
		jc.setApproveButtonText("Add");
		//jc.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
		int ret = jc.showOpenDialog(CodeCompGUI.getMainWin());
		if (ret == JFileChooser.APPROVE_OPTION) {
			final File file = jc.getSelectedFile();
			CCFileReader.read(file.getAbsolutePath());
		}
		} catch(Exception ex) {
			//TODO: logger addfileaction
			ex.printStackTrace();
		}
	}
}

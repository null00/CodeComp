package pl.edu.agh.codecomp.file.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Reader;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import pl.edu.agh.codecomp.file.CCFileReader;
import pl.edu.agh.codecomp.gui.CodeCompGUI;

public class AddFileAction implements ActionListener {

	public void actionPerformed(ActionEvent ae) {
		Reader reader = addFile();
		if (reader != null) {
			switch (ae.getActionCommand()) {
			case "leftText": {
				CodeCompGUI.setLeftFile(reader);
				break;
			}
			case "rightText": {
				CodeCompGUI.setRightFile(reader);
				break;
			}
			default:
				break;
			}
		}
	}

	private Reader addFile() {
		Reader reader = null;
		try {
			JFileChooser jc = new JFileChooser("Add file");
			jc.setApproveButtonText("Add");
			jc.setFileFilter(new FileNameExtensionFilter("Source Code Files", "asm", "java", "c", "ccp", "py", "jy", "html", "css"));
			int ret = jc.showOpenDialog(CodeCompGUI.getMainWin());
			if (ret == JFileChooser.APPROVE_OPTION) {
				final File file = jc.getSelectedFile();
				reader = CCFileReader.read(file.getAbsolutePath());
			}
		} catch (Exception ex) {
			// TODO: LOGGER
			ex.printStackTrace();
		}
		return reader;
	}
}
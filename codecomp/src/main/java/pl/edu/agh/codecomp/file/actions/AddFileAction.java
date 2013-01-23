package pl.edu.agh.codecomp.file.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import pl.edu.agh.codecomp.file.CCFileReader;
import pl.edu.agh.codecomp.gui.CodeCompGUI;

public class AddFileAction implements ActionListener {

	private final String RESOURCES = System.getProperty("user.dir") + "/src/main/java/pl/edu/agh/codecomp/resources/";

	public void actionPerformed(ActionEvent ae) {
		String text = addFile();
		if (text != null) {
			switch (ae.getActionCommand()) {
			case "leftText": {
				CodeCompGUI.setLeftFile(text);
				break;
			}
			case "rightText": {
				CodeCompGUI.setRightFile(text);
				break;
			}
			default:
				break;
			}
		}
	}

	private String addFile() {
		String output = null;
		try {
			JFileChooser jc = new JFileChooser("Add file");
			jc.setApproveButtonText("Add");
			jc.setCurrentDirectory(new File(RESOURCES));
			jc.setFileFilter(new FileNameExtensionFilter("Source Code Files", "asm", "java", "c", "ccp", "py", "jy", "html", "css"));
			int ret = jc.showOpenDialog(CodeCompGUI.getMainWin());
			if (ret == JFileChooser.APPROVE_OPTION) {
				final File file = jc.getSelectedFile();
				output = CCFileReader.read(file.getAbsolutePath());
			}
		} catch (Exception ex) {
			// TODO: LOGGER
			ex.printStackTrace();
		}
		return output;
	}
}
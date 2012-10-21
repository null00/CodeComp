package pl.edu.agh.codecomp.file.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Reader;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import pl.edu.agh.codecomp.file.CCFileReader;
import pl.edu.agh.codecomp.filter.Filter;
import pl.edu.agh.codecomp.gui.CodeCompGUI;

public class AddFileAction implements ActionListener {

	private final String PROJECT_PATH = System.getProperty("user.dir") + "/src/main/java/";

	public void actionPerformed(ActionEvent ae) {
		Reader reader = addFile();
		String text = Filter.parse(readFile(reader));
		if (reader != null) {
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

	private Reader addFile() {
		Reader reader = null;
		try {
			JFileChooser jc = new JFileChooser("Add file");
			jc.setApproveButtonText("Add");
			jc.setCurrentDirectory(new File(PROJECT_PATH));
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

	private String readFile(Reader reader) {
		int c;
		String output = "";
		try {
			while ((c = reader.read()) != -1) {
				output += String.valueOf(c);
			}
		} catch (Exception ex) {
			//TODO: LOGGER
		}
		return output;
	}
}
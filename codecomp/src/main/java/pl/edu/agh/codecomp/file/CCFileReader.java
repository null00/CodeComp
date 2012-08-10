package pl.edu.agh.codecomp.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import pl.edu.agh.codecomp.gui.CodeCompGUI;

public class CCFileReader {

	public static void read(String path) {
		Path file = Paths.get(path);
		Charset charset = Charset.defaultCharset();
		try {
			BufferedReader reader = Files.newBufferedReader(file, charset);
		    CodeCompGUI.setLeftFile(reader);
		} catch (IOException x) {
		    System.err.format("CCFileReader - IOException: %s%n", x);
		}
	}
}

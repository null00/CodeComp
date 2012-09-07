package pl.edu.agh.codecomp.file;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CCFileReader {

	public static Reader read(String path) {
		Path file = Paths.get(path);
		Charset charset = Charset.defaultCharset();
		Reader reader = null;
		try {
			reader = Files.newBufferedReader(file, charset);
		} catch (IOException x) {
		    System.err.format("CCFileReader - IOException: %s%n", x);
		}
		return reader;
	}
}

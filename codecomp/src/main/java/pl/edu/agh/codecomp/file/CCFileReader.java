package pl.edu.agh.codecomp.file;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import pl.edu.agh.codecomp.filter.Filter;

public class CCFileReader {
	
	private static Boolean filter = true;

	public static String read(String path) throws IOException {
		Path file = Paths.get(path);
		Charset charset = Charset.forName("UTF8");
		Reader reader = Files.newBufferedReader(file, charset);
		String output = "";
		if(filter) {
			output = Filter.parse(readFile(reader));
		} else {
			output = readFile(reader);
		}
		return output;
	}
	
	private static String readFile(Reader reader) {
		StringBuilder output = new StringBuilder();
		try {
			int c;
			while ((c = reader.read()) != -1) {
				output.append((char) c);
			}
		} catch (Exception ex) {
			//TODO: LOGGER
			ex.printStackTrace();
		}
		return output.toString();
	}

	public static Boolean getFilter() {
		return filter;
	}

	public static void setFilter(Boolean filter) {
		CCFileReader.filter = filter;
	}
}

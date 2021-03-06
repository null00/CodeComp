package pl.edu.agh.codecomp.file;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import pl.edu.agh.codecomp.comparator.CompareToken;
import pl.edu.agh.codecomp.filter.Filter;

public class CCFileReader {

	public static String read(String path) throws IOException {
		Path file = Paths.get(path);
		Charset charset = Charset.forName("UTF8");
		Reader reader = Files.newBufferedReader(file, charset);
		String output = "";
		if(CompareToken.getFilter()) {
			output = Filter.parse(readFile(reader));
		} else {
			output = readFile(reader);
		}
		if(CompareToken.getToLowerCase()) {
			output = output.toLowerCase();
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
			ex.printStackTrace();
		}
		return output.toString(); 
	}
}

package pl.edu.agh.codecomp.filter;

public class Filter {

	public static String parse(String text) {
		String source = "";
		String lines[] = text.split("\n");
		for(String line : lines) {
			if(!line.startsWith(";")) {
				source += line + "\n";
			}
		}
		return source;
	}
	
}

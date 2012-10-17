package pl.edu.agh.codecomp.filter;

public class Filter {

	public static String parse(String text) {
		String source = "";
		String lines[] = text.split("\n");
		for(String line : lines) {
			String words[] = line.split(" ");
			for(String word : words) {
				if(word.startsWith(";")) {
					int stop = line.indexOf(word);
					source += line.substring(0, stop) + "\n";
					break;
				} else {
					source += word + " ";
				}
			}
			source += "\n";
		}
		return source.trim();
	}
	
}

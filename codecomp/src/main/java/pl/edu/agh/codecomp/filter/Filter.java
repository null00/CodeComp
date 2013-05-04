package pl.edu.agh.codecomp.filter;

public class Filter {

	public static String parse(String input) {
		String output = "";
		output = replaceWhiteChars(input);
		output = removeComments(output);
		return output.trim();
	}

	private static String replaceWhiteChars(String input) {
		return input.replaceAll("[\t\r\f ]+", " ").replaceAll("\n+", "\n");
	}

	private static String removeComments(String input) {
		String output = "";
		String lines[] = input.split("\n");
		for (String line : lines) {
			line = line.trim();
			if (!lines[0].equals(line) && !line.isEmpty() && !line.startsWith(";")) {
				output += "\n";
			}
			String temp = "";
			for (char c : line.trim().toCharArray()) {
				if (c == ';' || c == '?') {
					break;
				} else {
					temp += c;
				}
			}
			output += temp.trim();
		}
		return output;
	}

}

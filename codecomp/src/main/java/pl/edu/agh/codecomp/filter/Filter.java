package pl.edu.agh.codecomp.filter;

public class Filter {

	public static String parse(String input) {
		String output = "";
		output = replaceWhiteChars(input);
		output = removeComments(output);
		return output.trim();
	}

	private static String replaceWhiteChars(String input) {
		return input.replace("\t\t", " ").replace("\t", " ");
	}

	//FIXME: dodac znak nowej linii, jesli komentarz w srodku linii
	private static String removeComments(String input) {
		String output = "";
		String lines[] = input.split("\n");
		boolean comment = false;
		for (String line : lines) {
			if (!lines[0].equals(line) && !line.isEmpty() && !comment) {
				output += "\n";
			}
			comment = false;
			for (char c : line.trim().toCharArray()) {
				if (c == ';') {
					comment = true;
					break;
				} else {
					output += c;
				}
			}
		}
		return output;
	}

}

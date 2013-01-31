package pl.edu.agh.codecomp.comparator;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.lexer.IScanner;
import pl.edu.agh.codecomp.lexer.Scanner;
import pl.edu.agh.codecomp.lexer.SimpleScanner;

public class SourceComparator extends IComparator {

	public SourceComparator(RSyntaxTextArea left, RSyntaxTextArea right) {
		super();
		this.left = left;
		this.right = right;
		start();
	}

	@Override
	public void run() {
		compare();
	}

	/**
	 * COMPARATOR
	 */

	private void compare() {
		IScanner scanner = null;
		Reader reader = new StringReader(left.getText());

		try {

			switch (CompareToken.getSourceComparator().toLowerCase()) {
			case "simple": {
				scanner = new SimpleScanner(reader);
				break;
			}
			case "full": {
				scanner = new Scanner(reader);
				break;
			}
			default: {
				return;
			}
			}

			runComparator(scanner);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void runComparator(IScanner scanner) {
		right.setText("");
		while (true) {
			try {
				String token = scanner.yylex();
				if (token == null)
					break;
				right.append(token + ": " + scanner.yytext());
				right.append("\n");
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}

}

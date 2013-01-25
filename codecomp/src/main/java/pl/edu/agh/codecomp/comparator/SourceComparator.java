package pl.edu.agh.codecomp.comparator;

import java.io.StringReader;

import java_cup.runtime.Symbol;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.jflex.Lexer;

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
	
	/*
	 * COMPARATOR
	 */

	private void compare() {
		right.setText("");
		Lexer lexer = new Lexer(new StringReader(left.getText()));
		while (true) {
			try {
				Symbol sym = lexer.next_token();
				if (!sym.toString().equals("#0")) {
					String string = lexer.yytext();
					right.append(sym.value + ": " + string);
					right.append("\n");
//					System.out.println(sym + ": " + string);
				} else {
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

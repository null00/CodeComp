package pl.edu.agh.codecomp.lexer;

import java.io.StringReader;

import java_cup.runtime.Symbol;

public class LexerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Scanner scanner = new Scanner(new StringReader("private final String text = \"trele morele\" "));
		while (true) {
			try {
				Symbol sym = scanner.next_token();
				if (!sym.toString().equals("#0")) {
					String string = scanner.yytext();
					System.out.println(sym + ": " + string);

					/*
					 * parser p = new parser(new Scanner(System.in)); 
					 * Object result = p.parse().value;
					 * System.out.println(result.toString());
					 */
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

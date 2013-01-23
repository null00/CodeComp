package pl.edu.agh.codecomp.jflex;

import java.io.StringReader;

import java_cup.runtime.Symbol;

public class LexerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Lexer lexer = new Lexer(new StringReader("private final String text = \"trele morele\" "));
		while (true) {
			try {
				Symbol sym = lexer.next_token();
				if (!sym.toString().equals("#0")) {
					String string = lexer.yytext();
					System.out.println(sym + ": " + string);

					/*
					 * parser p = new parser(new Lexer(System.in)); Object
					 * result = p.parse().value;
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

package pl.edu.agh.codecomp.jflex;

import java.io.IOException;

import java_cup.runtime.Symbol;


public class LexerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Lexer lexer = new Lexer(System.in);
		while(true) {
			try {
				Symbol sym = lexer.next_token();
				System.out.println(sym);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

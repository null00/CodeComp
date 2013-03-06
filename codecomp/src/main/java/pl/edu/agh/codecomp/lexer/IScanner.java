package pl.edu.agh.codecomp.lexer;

import java.io.IOException;

public interface IScanner {

	public String yytext();

	public int yylex() throws IOException;

}

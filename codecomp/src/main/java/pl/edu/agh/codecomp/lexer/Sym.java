package pl.edu.agh.codecomp.lexer;

import JFlex.sym;

public class Sym implements sym {
	
	  /* terminals */
	  public static final int REGISTER 		= 1;
	  public static final int ARITHMETIC	= 2;
	  public static final int LOGIC 		= 3;
	  public static final int JUMP			= 4;
	  public static final int TRANSFER		= 5;
	  public static final int MISC			= 6;
	  public static final int ID			= 7;
	  public static final int LABEL			= 8;
	  public static final int LBRACE		= 9;
	  public static final int RBRACE		= 10;
	  public static final int NUMBER		= 11;
	  public static final int OPERATION		= 12;
	  public static final int EQUALS		= 13;
	  public static final int EQEQ			= 14;
	  
}

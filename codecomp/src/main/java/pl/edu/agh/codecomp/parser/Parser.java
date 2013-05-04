//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 4 "parser.y"

package pl.edu.agh.codecomp.parser;

import java.io.IOException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import pl.edu.agh.codecomp.lexer.IScanner;
import pl.edu.agh.codecomp.tree.*;

//#line 26 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short TEXT=257;
public final static short NUM=258;
public final static short OP_MOV=259;
public final static short OP_SMOV=260;
public final static short OP_AR=261;
public final static short OP_LOG=262;
public final static short OP_MISC=263;
public final static short OP_JMP=264;
public final static short OP_STO=265;
public final static short OP_COMP=266;
public final static short REG=267;
public final static short LAB=268;
public final static short ID=269;
public final static short EQ=270;
public final static short COMMA=271;
public final static short APOSTROPHE=272;
public final static short NEG=273;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    1,    3,    3,    3,    4,    5,    6,
    6,    6,    6,    6,    6,    6,    7,    7,    7,    7,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    2,    2,
    2,    2,    2,
};
final static short yylen[] = {                            2,
    0,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    2,    2,    2,    2,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    0,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         1,
    0,    9,   10,    8,   14,   12,   13,   11,   15,   16,
    6,    5,    7,    3,    0,    2,    4,    0,   60,    0,
    0,    0,    0,    0,    0,    0,   18,   17,   19,   20,
   24,    0,    0,    0,    0,   21,   23,   22,    0,    0,
    0,   57,   58,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yydgoto[] = {                          1,
   16,   17,   36,   19,   31,   38,   32,   22,
};
final static short yysindex[] = {                         0,
  588,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  729,    0,    0,  -40,    0,  -16,
  635,  161,  -40,  816,  -28,  729,    0,    0,    0,    0,
    0,  729,  729,  729,  729,    0,    0,    0,  729,  729,
  729,    0,    0,  -40,  -16,  635,  161,  -40,  -16,  635,
  161,  -40,  -16,  635,  161,  -40,  -16,  635,  161,  -40,
  -16,  635,  161,  -40,  -16,  635,  161,  -40,  -16,  635,
  161,  -40,  -16,  635,  161,
};
final static short yyrindex[] = {                         0,
    4,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  550,    0,    0,
  585,  451,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  462,    1,  561,   13,  473,   25,  562,
   37,  484,   49,  568,   61,  495,   73,  569,   85,  506,
   97,  573,  109,  517,  121,  574,  133,  528,  145,  580,
  157,  539,  427,  581,  439,
};
final static short yygindex[] = {                         0,
    0,    0,  603,    0,  890, 1017,  954,  845,
};
final static int YYTABLESIZE=1087;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                          0,
   53,   29,   28,   59,   27,    0,   30,    0,    0,    0,
   53,    0,   55,   29,   28,    0,   27,    0,   30,    0,
    0,    0,   55,    0,   38,   29,   28,    0,   27,    0,
   30,    0,    0,    0,   38,    0,   40,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   40,    0,   41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   41,    0,
   43,    0,    0,    0,   43,    0,    0,    0,    0,    0,
   43,    0,   25,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   25,    0,   27,    0,    0,    0,    0,    0,
    0,   53,    0,   53,   27,    0,   45,    0,    0,    0,
    0,    0,    0,   55,    0,   55,   45,    0,   47,    0,
    0,    0,    0,    0,    0,   38,    0,   38,   47,    0,
   30,    0,    0,    0,    0,    0,    0,   40,    0,   40,
   30,    0,   31,    0,    0,    0,    0,    0,    0,   41,
    0,   41,   31,    0,   49,    0,    0,    0,    0,    0,
    0,   43,    0,   43,   49,    0,   51,    0,    0,    0,
    0,    0,    0,   25,    0,   25,   51,    0,    0,    0,
    0,    0,    0,    0,    0,   27,    0,   27,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   45,    0,   45,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   47,
    0,   47,   29,   28,    0,   27,    0,   30,    0,    0,
    0,   30,    0,   30,    0,    0,    0,    2,    0,    0,
    0,    0,    0,   31,    0,   31,    0,    0,    0,    0,
   26,    0,    0,    0,    0,   49,    0,   49,    0,    0,
    0,    0,   40,    0,    0,    0,    0,   51,    0,   51,
    0,    0,    0,    0,   33,    0,    0,    0,   53,   53,
   53,   53,   53,   53,   53,   53,   53,   53,   53,   53,
   55,   55,   55,   55,   55,   55,   55,   55,   55,   55,
   55,   55,   38,   38,   38,   38,   38,   38,   38,   38,
   38,   38,   38,   38,   40,   40,   40,   40,   40,   40,
   40,   40,   40,   40,   40,   40,   41,   41,   41,   41,
   41,   41,   41,   41,   41,   41,   41,   41,   43,   43,
   43,   43,   43,   43,   43,   43,   43,   43,   43,   43,
   25,   25,   25,   25,   25,   25,   25,   25,   25,   25,
   25,   25,   27,   27,   27,   27,   27,   27,   27,   27,
   27,   27,   27,   27,   45,   45,   45,   45,   45,   45,
   45,   45,   45,   45,   45,   45,   47,   47,   47,   47,
   47,   47,   47,   47,   47,   47,   47,   47,   30,   30,
   30,   30,   30,   30,   30,   30,   30,   30,   30,   30,
   31,   31,   31,   31,   31,   31,   31,   31,   31,   31,
   31,   31,   49,   49,   49,   49,   49,   49,   49,   49,
   49,   49,   49,   49,   51,   51,   51,   51,   51,   51,
   51,   51,   51,   51,   51,   51,   34,    0,    0,    0,
    0,   40,    0,    0,    0,    0,   34,    0,   33,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   33,    0,
   63,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   63,   56,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   56,   37,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   37,   44,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   44,   28,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   28,   48,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   48,   32,   34,    0,   34,
    0,    0,    0,    0,    0,    0,   32,   52,    0,   33,
    0,   33,    0,    0,    0,    0,    0,   52,   36,    0,
    0,   63,    0,    0,    0,    0,    0,    0,   36,   61,
    0,    0,   56,    0,   56,    0,    0,    0,    0,   61,
   54,   39,    0,   37,    0,   37,    0,   42,   26,    0,
   54,   39,   46,   29,   44,    0,   44,   42,   26,   50,
   35,    0,   46,   29,   62,   28,    0,   28,    0,   50,
   35,    0,    0,    0,   62,    0,   48,   14,   48,    0,
    0,    0,    0,   18,    0,    0,    0,   32,    0,   32,
    0,    0,    0,    0,    0,    0,    0,   23,   52,    0,
   52,    0,    0,    0,    0,    0,    0,    0,   44,   36,
    0,   36,    0,    0,   48,   52,   56,   60,    0,    0,
   61,   64,   68,   72,    0,    0,    0,    0,    0,    0,
    0,   54,   39,   54,   39,    0,    0,    0,   42,   26,
   42,   26,    0,   46,   29,   46,   29,    0,    0,    0,
   50,   35,   50,   35,    0,   62,   29,   28,   15,   27,
    0,   30,    0,    0,   34,   34,   34,   34,   34,   34,
   34,   34,   34,   34,   34,   34,   33,   33,   33,   33,
   33,   33,   33,   33,   33,   33,   33,   33,   63,   63,
   63,   63,   63,   63,   63,   63,   63,   63,   63,   63,
   56,   56,   56,   56,   56,   56,   56,   56,   56,   56,
   56,   37,   37,   37,   37,   37,   37,   37,   37,   37,
   37,   37,   44,   44,   44,   44,   44,   44,   44,   44,
   44,   44,   44,   28,   28,   28,   28,   28,   28,   28,
   28,   28,   28,   28,   48,   48,   48,   48,   48,   48,
   48,   48,   48,   48,   48,   32,   32,   32,   32,   32,
   32,   32,   32,   32,   32,   32,   52,   52,   52,   52,
   52,   52,   52,   52,   52,   52,   52,   36,   36,   36,
   36,   36,   36,   36,   36,   36,   36,   36,   61,   61,
   61,   61,   61,   61,   61,   61,   61,   61,   61,   15,
   54,   39,    0,    0,    0,    0,    0,   42,   26,    0,
    0,    0,   46,   29,    0,    0,    0,    0,    0,   50,
   35,    0,    0,    0,   62,    2,    3,    4,    5,    6,
    7,    8,    9,   10,   11,   12,   13,   29,   28,   25,
   27,    0,   30,    0,    0,    0,    0,    0,    0,    0,
   47,    0,    0,    0,    0,    0,   51,   55,   59,   63,
    0,    0,    0,   67,   71,   75,    0,    0,    0,    0,
   20,    0,    2,    3,    0,    5,    6,    7,    8,    9,
   10,   11,   12,   13,   20,   35,    0,    0,   42,    0,
   37,    0,    0,   37,    0,   45,    0,    0,    0,    0,
    0,   49,   53,   57,   61,    0,    0,    0,   65,   69,
   73,    0,    0,    0,    0,   37,    0,    0,    0,   37,
    0,    0,    0,   37,    0,    0,    0,   37,    0,    0,
    0,   37,    0,    0,    0,   37,    0,    0,    0,   37,
    0,    0,    0,   37,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   34,   39,   41,    0,   39,   41,    0,
    0,    0,    0,    0,    0,    0,    2,    3,    0,    5,
    6,    7,    8,    9,   10,   11,   12,   13,   34,   39,
   41,    0,   34,   39,   41,    0,   34,   39,   41,    0,
   34,   39,   41,    0,   34,   39,   41,   21,   34,   39,
   41,    0,   34,   39,   41,    0,   34,   39,   41,    0,
    0,   24,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   46,    0,    0,    0,    0,    0,   50,   54,
   58,   62,    0,    0,    0,   66,   70,   74,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    2,    3,    0,    5,    6,    7,    8,
    9,   10,   11,   12,   13,    0,   35,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         -1,
    0,   42,   43,    0,   45,   -1,   47,   -1,   -1,   -1,
   10,   -1,    0,   42,   43,   -1,   45,   -1,   47,   -1,
   -1,   -1,   10,   -1,    0,   42,   43,   -1,   45,   -1,
   47,   -1,   -1,   -1,   10,   -1,    0,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   10,   -1,    0,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   10,   -1,
    0,   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,   -1,
   10,   -1,    0,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   10,   -1,    0,   -1,   -1,   -1,   -1,   -1,
   -1,   91,   -1,   93,   10,   -1,    0,   -1,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   93,   10,   -1,    0,   -1,
   -1,   -1,   -1,   -1,   -1,   91,   -1,   93,   10,   -1,
    0,   -1,   -1,   -1,   -1,   -1,   -1,   91,   -1,   93,
   10,   -1,    0,   -1,   -1,   -1,   -1,   -1,   -1,   91,
   -1,   93,   10,   -1,    0,   -1,   -1,   -1,   -1,   -1,
   -1,   91,   -1,   93,   10,   -1,    0,   -1,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   93,   10,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   91,   -1,   93,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   91,   -1,   93,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   91,
   -1,   93,   42,   43,   -1,   45,   -1,   47,   -1,   -1,
   -1,   91,   -1,   93,   -1,   -1,   -1,  258,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   93,   -1,   -1,   -1,   -1,
  271,   -1,   -1,   -1,   -1,   91,   -1,   93,   -1,   -1,
   -1,   -1,  271,   -1,   -1,   -1,   -1,   91,   -1,   93,
   -1,   -1,   -1,   -1,  271,   -1,   -1,   -1,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  267,  268,  269,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  258,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,  269,  258,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  268,  269,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  267,  268,  269,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  258,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,  269,  258,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  268,  269,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  267,  268,  269,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  258,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,  269,    0,   -1,   -1,   -1,
   -1,  271,   -1,   -1,   -1,   -1,   10,   -1,    0,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   10,   -1,
    0,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   10,    0,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   10,    0,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   10,    0,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   10,    0,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   10,    0,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   10,    0,   91,   -1,   93,
   -1,   -1,   -1,   -1,   -1,   -1,   10,    0,   -1,   91,
   -1,   93,   -1,   -1,   -1,   -1,   -1,   10,    0,   -1,
   -1,   91,   -1,   -1,   -1,   -1,   -1,   -1,   10,    0,
   -1,   -1,   91,   -1,   93,   -1,   -1,   -1,   -1,   10,
    0,    0,   -1,   91,   -1,   93,   -1,    0,    0,   -1,
   10,   10,    0,    0,   91,   -1,   93,   10,   10,    0,
    0,   -1,   10,   10,    0,   91,   -1,   93,   -1,   10,
   10,   -1,   -1,   -1,   10,   -1,   91,   10,   93,   -1,
   -1,   -1,   -1,    1,   -1,   -1,   -1,   91,   -1,   93,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   15,   91,   -1,
   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   26,   91,
   -1,   93,   -1,   -1,   32,   33,   34,   35,   -1,   -1,
   91,   39,   40,   41,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   91,   91,   93,   93,   -1,   -1,   -1,   91,   91,
   93,   93,   -1,   91,   91,   93,   93,   -1,   -1,   -1,
   91,   91,   93,   93,   -1,   91,   42,   43,   91,   45,
   -1,   47,   -1,   -1,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,  269,  258,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  268,  269,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  267,  268,  269,
  259,  260,  261,  262,  263,  264,  265,  266,  267,  268,
  269,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  259,  260,  261,  262,  263,  264,  265,  266,
  267,  268,  269,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  259,  260,  261,  262,  263,  264,
  265,  266,  267,  268,  269,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,  269,  259,  260,  261,  262,
  263,  264,  265,  266,  267,  268,  269,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  268,  269,  259,  260,
  261,  262,  263,  264,  265,  266,  267,  268,  269,   91,
  260,  260,   -1,   -1,   -1,   -1,   -1,  260,  260,   -1,
   -1,   -1,  260,  260,   -1,   -1,   -1,   -1,   -1,  260,
  260,   -1,   -1,   -1,  260,  258,  259,  260,  261,  262,
  263,  264,  265,  266,  267,  268,  269,   42,   43,   15,
   45,   -1,   47,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   26,   -1,   -1,   -1,   -1,   -1,   32,   33,   34,   35,
   -1,   -1,   -1,   39,   40,   41,   -1,   -1,   -1,   -1,
    1,   -1,  258,  259,   -1,  261,  262,  263,  264,  265,
  266,  267,  268,  269,   15,  271,   -1,   -1,   93,   -1,
   21,   -1,   -1,   24,   -1,   26,   -1,   -1,   -1,   -1,
   -1,   32,   33,   34,   35,   -1,   -1,   -1,   39,   40,
   41,   -1,   -1,   -1,   -1,   46,   -1,   -1,   -1,   50,
   -1,   -1,   -1,   54,   -1,   -1,   -1,   58,   -1,   -1,
   -1,   62,   -1,   -1,   -1,   66,   -1,   -1,   -1,   70,
   -1,   -1,   -1,   74,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   20,   21,   22,   -1,   24,   25,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  258,  259,   -1,  261,
  262,  263,  264,  265,  266,  267,  268,  269,   45,   46,
   47,   -1,   49,   50,   51,   -1,   53,   54,   55,   -1,
   57,   58,   59,   -1,   61,   62,   63,    1,   65,   66,
   67,   -1,   69,   70,   71,   -1,   73,   74,   75,   -1,
   -1,   15,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   26,   -1,   -1,   -1,   -1,   -1,   32,   33,
   34,   35,   -1,   -1,   -1,   39,   40,   41,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  258,  259,   -1,  261,  262,  263,  264,
  265,  266,  267,  268,  269,   -1,  271,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=273;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,"'\\n'",null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,"'*'","'+'",
null,"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'","'^'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"TEXT","NUM","OP_MOV","OP_SMOV","OP_AR",
"OP_LOG","OP_MISC","OP_JMP","OP_STO","OP_COMP","REG","LAB","ID","EQ","COMMA",
"APOSTROPHE","NEG",
};
final static String yyrule[] = {
"$accept : input",
"input :",
"input : input line",
"line : '\\n'",
"line : func",
"desc : LAB",
"desc : REG",
"desc : ID",
"single : OP_SMOV",
"num : NUM",
"exp : OP_MOV",
"exp : OP_JMP",
"exp : OP_LOG",
"exp : OP_MISC",
"exp : OP_AR",
"exp : OP_STO",
"exp : OP_COMP",
"opers : '+'",
"opers : '-'",
"opers : '*'",
"opers : '/'",
"ops : exp desc",
"ops : exp exp",
"ops : exp num",
"ops : desc num",
"ops : num opers num",
"ops : num opers exp",
"ops : num opers ops",
"ops : num opers desc",
"ops : exp opers exp",
"ops : exp opers num",
"ops : exp opers ops",
"ops : exp opers desc",
"ops : ops opers ops",
"ops : ops opers num",
"ops : ops opers exp",
"ops : ops opers desc",
"ops : desc opers desc",
"ops : desc opers num",
"ops : desc opers exp",
"ops : desc opers ops",
"ops : num COMMA num",
"ops : num COMMA exp",
"ops : num COMMA ops",
"ops : num COMMA desc",
"ops : exp COMMA num",
"ops : exp COMMA exp",
"ops : exp COMMA ops",
"ops : exp COMMA desc",
"ops : ops COMMA num",
"ops : ops COMMA exp",
"ops : ops COMMA ops",
"ops : ops COMMA desc",
"ops : desc COMMA num",
"ops : desc COMMA exp",
"ops : desc COMMA ops",
"ops : desc COMMA desc",
"ops : '[' exp ']'",
"ops : '[' ops ']'",
"func :",
"func : single",
"func : desc",
"func : exp",
"func : ops",
};

//#line 114 "parser.y"

/* === PROGRAM === */

	private RSyntaxTextArea left, right;
	private IScanner scanner;
	private Node root;

	public Parser(RSyntaxTextArea left, RSyntaxTextArea right, IScanner scanner) {
		this.left = left;
		this.right = right;
		this.scanner = scanner;
		this.root = new Node();
	}
	
	public Parser(RSyntaxTextArea left, RSyntaxTextArea right, IScanner scanner, boolean debugMe) {
        this(left, right, scanner);
        this.yydebug = debugMe;
    }

	void yyerror(String s) {
		System.err.println("parser: " + s);
	}

	// TODO:
	private int yylex() {
		int tok = -1;
		try {
			tok = scanner.yylex();
			yylval = new ParserVal(scanner.yytext());
			String tmp = "tok: " + yyname[tok] + ": '" + scanner.yytext() + "'";
			right.append(tmp + "\n");
			//System.out.println(yyname[tok]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
		return tok;
	}
//#line 527 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 4:
//#line 30 "parser.y"
{ System.out.println(" # " + val_peek(0).sval + " "); }
break;
case 5:
//#line 33 "parser.y"
{ yyval = val_peek(0); }
break;
case 6:
//#line 34 "parser.y"
{ yyval = val_peek(0); }
break;
case 7:
//#line 35 "parser.y"
{ yyval = val_peek(0); }
break;
case 8:
//#line 38 "parser.y"
{ yyval = val_peek(0); }
break;
case 9:
//#line 40 "parser.y"
{ yyval = val_peek(0); }
break;
case 10:
//#line 42 "parser.y"
{ yyval = val_peek(0); }
break;
case 11:
//#line 43 "parser.y"
{ yyval = val_peek(0); }
break;
case 12:
//#line 44 "parser.y"
{ yyval = val_peek(0); }
break;
case 13:
//#line 45 "parser.y"
{ yyval = val_peek(0); }
break;
case 14:
//#line 46 "parser.y"
{ yyval = val_peek(0); }
break;
case 15:
//#line 47 "parser.y"
{ yyval = val_peek(0); }
break;
case 16:
//#line 48 "parser.y"
{ yyval = val_peek(0); }
break;
case 17:
//#line 51 "parser.y"
{ yyval = val_peek(0); }
break;
case 18:
//#line 52 "parser.y"
{ yyval = val_peek(0); }
break;
case 19:
//#line 53 "parser.y"
{ yyval = val_peek(0); }
break;
case 20:
//#line 54 "parser.y"
{ yyval = val_peek(0); }
break;
case 21:
//#line 57 "parser.y"
{ yyval = new ParserVal(val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 22:
//#line 58 "parser.y"
{ yyval = new ParserVal(val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 23:
//#line 59 "parser.y"
{ yyval = new ParserVal(val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 24:
//#line 60 "parser.y"
{ yyval = new ParserVal(val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 25:
//#line 62 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 26:
//#line 63 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 27:
//#line 64 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 28:
//#line 65 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 29:
//#line 67 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 30:
//#line 68 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 31:
//#line 69 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 32:
//#line 70 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 33:
//#line 72 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 34:
//#line 73 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 35:
//#line 74 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 36:
//#line 75 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 37:
//#line 77 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 38:
//#line 78 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 39:
//#line 79 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 40:
//#line 80 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 41:
//#line 82 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 42:
//#line 83 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 43:
//#line 84 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 44:
//#line 85 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 45:
//#line 87 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 46:
//#line 88 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 47:
//#line 89 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 48:
//#line 90 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 49:
//#line 92 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 50:
//#line 93 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 51:
//#line 94 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 52:
//#line 95 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 53:
//#line 97 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 54:
//#line 98 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 55:
//#line 99 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 56:
//#line 100 "parser.y"
{ yyval = new ParserVal(val_peek(2).sval + " " + val_peek(1).sval + " " + val_peek(0).sval); }
break;
case 57:
//#line 102 "parser.y"
{ yyval = val_peek(1); }
break;
case 58:
//#line 103 "parser.y"
{ yyval = val_peek(1); }
break;
case 60:
//#line 107 "parser.y"
{ yyval = val_peek(0); }
break;
case 61:
//#line 108 "parser.y"
{ yyval = val_peek(0); }
break;
case 62:
//#line 109 "parser.y"
{ yyval = val_peek(0); }
break;
case 63:
//#line 110 "parser.y"
{ yyval = val_peek(0); }
break;
//#line 912 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################

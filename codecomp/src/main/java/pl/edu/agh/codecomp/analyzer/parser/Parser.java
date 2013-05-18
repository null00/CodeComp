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

package pl.edu.agh.codecomp.analyzer.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import no.roek.nlpged.graph.Edge;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import pl.edu.agh.codecomp.analyzer.lexer.IScanner;
import pl.edu.agh.codecomp.analyzer.tree.Node;
import pl.edu.agh.codecomp.comparator.graph.SimpleEdge;
import pl.edu.agh.codecomp.comparator.graph.SparseUndirectedGraph;

//#line 33 "Parser.java"




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
public final static short OP_SAR=262;
public final static short OP_LOG=263;
public final static short OP_SLOG=264;
public final static short OP_MISC=265;
public final static short OP_JMP=266;
public final static short OP_STO=267;
public final static short OP_COMP=268;
public final static short REG=269;
public final static short LAB=270;
public final static short ID=271;
public final static short EQ=272;
public final static short COMMA=273;
public final static short APOSTROPHE=274;
public final static short NEG=275;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    1,    3,    3,    3,    4,    4,    4,
    4,    4,    4,    4,    5,    6,    6,    6,    6,    6,
    6,    6,    7,    7,    7,    7,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    2,    2,    2,    2,    2,
};
final static short yylen[] = {                            2,
    0,    2,    1,    1,    1,    1,    1,    1,    1,    2,
    2,    2,    2,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    2,    2,    2,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    0,    1,    1,    1,    1,
};
final static short yydefred[] = {                         1,
    0,   15,   16,    0,   20,    9,   18,    0,    0,    0,
   21,   22,    6,    5,    7,    3,    0,    2,    4,    0,
   65,    0,    0,    0,   12,   13,   11,   14,   10,   19,
   17,    0,    0,    0,    0,   24,   23,   25,   26,   29,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   62,   63,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yydgoto[] = {                          1,
   18,   19,   32,   21,   45,   46,   47,   48,
};
final static short yysindex[] = {                         0,
  813,    0,    0, -261,    0,    0,    0, -261, -255, -261,
    0,    0,    0,    0,    0,    0,  394,    0,    0,  -40,
    0,  174,  827,  210,    0,    0,    0,    0,    0,    0,
    0,  -40,  -22,   -5,  394,    0,    0,    0,    0,    0,
  394,  394,  394,  394,  174,  827,  394,  210,  394,  394,
    0,    0,  -40,  174,  827,  210,  -40,  174,  827,  210,
  -40,  174,  827,  210,  -40,  174,  827,  210,  -40,  174,
  827,  210,  -40,  174,  827,  210,  -40,  174,  827,  210,
  -40,  174,  827,  210,
};
final static short yyrindex[] = {                         0,
    4,    0,    0,  943,    0,    0,    0,    0,    1,   17,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  929,
    0,    0,  177,  551,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   35,    0,    0,   51,    0,    0,
    0,    0,  565,   65,  180,   79,  579,   93,  952,  107,
  593,  121,  955,  135,  617,  149,  960,  163,  631,  195,
  961,  435,  865,  467,  966,  481,  903,  495,  967,  509,
  916,  523,  972,  537,
};
final static short yygindex[] = {                         0,
    0,    0,  563,    0, 1232,   55, 1238,  471,
};
final static int YYTABLESIZE=1322;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                          0,
   19,   38,   37,   64,   36,    0,   39,   13,   14,   15,
   19,   27,   28,    0,    0,    0,   17,    0,    0,   38,
   37,    0,   36,    0,   39,    0,   17,    0,    0,    0,
    0,    0,    0,    0,   28,    0,   38,   37,    0,   36,
    0,   39,   19,   19,   28,   19,    0,   19,    0,    0,
   27,    0,    0,    0,    0,   23,    0,    0,   17,   17,
   27,   17,    0,   17,   58,    0,    0,    0,   17,    0,
   51,   33,    0,    0,   58,    0,    0,    0,   60,    0,
    0,    0,    0,    0,    0,    0,    0,   52,   60,   55,
    0,   19,   43,    0,    0,   59,   63,   67,   71,    0,
    0,   75,   43,   79,   83,    0,   45,   17,    0,    0,
    0,    0,    0,    0,    0,    0,   45,    0,    0,    0,
   46,    0,    0,    0,    0,   28,    0,   28,    0,    0,
   46,    0,    0,    0,   48,    0,    0,    0,    0,    0,
    0,   27,    0,   27,   48,    0,    0,    0,   30,    0,
    0,    0,    0,    0,    0,   58,    0,   58,   30,    0,
    0,    0,   32,    0,    0,    0,    0,    0,    0,   60,
    0,   60,   32,    0,    0,    0,   67,    0,    0,   59,
    0,    0,    0,   43,    0,   43,   67,    0,    0,   59,
    0,    0,    0,    0,   50,    0,    0,   45,    0,   45,
    0,    0,    0,    0,   50,    0,    0,    0,    0,    0,
    0,   46,    0,   46,    0,   38,   37,    2,   36,    0,
   39,    0,    0,    0,    0,   48,    0,   48,    0,    0,
    0,    0,   35,    0,    0,    2,    3,    0,    5,   30,
    7,   30,   30,   31,   11,   12,   13,   14,   15,    0,
   44,   38,   37,   32,   36,   32,   39,    0,   19,   19,
   19,   19,   19,   19,   19,   19,   19,   49,    0,   19,
   19,   19,   59,   19,   17,   17,   17,   17,   17,   17,
   17,   17,   17,   17,   17,   50,    0,   50,    0,   17,
    0,    0,   28,   28,   28,   28,   28,   28,   28,   28,
   28,   28,   28,   28,   28,   28,    0,    0,   27,   27,
   27,   27,   27,   27,   27,   27,   27,   27,   27,   27,
   27,   27,   58,   58,   58,   58,   58,   58,   58,   58,
   58,   58,   58,   58,   58,   58,   60,   60,   60,   60,
   60,   60,   60,   60,   60,   60,   60,   60,   60,   60,
   43,   43,   43,   43,   43,   43,   43,   43,   43,   43,
   43,   43,   43,   43,   45,   45,   45,   45,   45,   45,
   45,   45,   45,   45,   45,   45,   45,   45,   46,   46,
   46,   46,   46,   46,   46,   46,   46,   46,   46,   46,
   46,   46,   48,   48,   48,   48,   48,   48,   48,   48,
   48,   48,   48,   48,   48,   48,   30,   30,   30,   30,
   30,   30,   30,   30,   30,   30,   30,   30,   30,   30,
   32,   32,   32,   32,   32,   32,   32,   32,   32,   32,
   32,   32,   32,   32,   52,    0,   67,    0,   67,   59,
   67,   59,    0,   59,   52,    0,   42,    0,    0,    0,
    0,    0,   50,   50,   50,   50,   50,   50,   50,   50,
   50,   50,   50,   50,   50,   50,   35,    0,    0,    0,
    0,   24,    0,    0,    0,    0,   35,    0,    0,    0,
   36,    0,   49,    0,   17,    0,    0,   34,    0,    0,
   36,    0,    0,    0,   54,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   54,   56,    0,    0,   56,    0,
    0,   60,   64,   68,   72,    0,    0,   76,   56,   80,
   84,    0,   39,    0,    0,   52,    0,   52,    0,    0,
    0,    0,   39,    0,    0,    0,   38,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   38,    0,    0,    0,
   68,    0,    0,    0,    0,    0,    0,   35,    0,   35,
   68,    0,    0,   20,   61,    0,   25,    0,    0,    0,
   26,   36,   29,   36,   61,    0,    0,    0,   42,    0,
    0,    0,    0,    0,    0,   54,    0,   54,   42,    0,
    0,    0,   49,    0,    0,    0,    0,   53,    0,   56,
    0,   56,   49,   57,   61,   65,   69,    0,    0,   73,
    0,   77,   81,   39,    0,   39,   33,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   33,   38,    0,   38,
   53,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   53,   68,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    2,    3,    0,    5,   61,    7,   61,   30,   31,
   11,   12,   13,   14,   15,    0,    0,    0,    0,   42,
    0,   42,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   49,    0,   49,    0,    0,    0,    0,
    0,    0,   52,   52,   52,   52,   52,   52,   52,   52,
   52,   52,   52,   52,   52,   52,    0,   33,    0,   33,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   53,    0,   53,   35,   35,   35,   35,   35,   35,
   35,   35,   35,   35,   35,   35,   35,   35,   36,   36,
   36,   36,   36,   36,   36,   36,   36,   36,   36,   36,
   36,   36,   54,   54,   54,   54,   54,   54,   54,   54,
   54,   54,   54,   54,   54,   54,   56,   56,   56,   56,
   56,   56,   56,   56,   56,   56,   56,   56,   56,   56,
   39,   39,   39,   39,   39,   39,   39,   39,   39,   39,
   39,   39,   39,   39,   38,   38,   38,   38,   38,   38,
   38,   38,   38,   38,   38,   38,   38,   38,   68,   68,
   68,   68,   68,   68,   68,   68,   68,   68,   68,   68,
   68,   68,   16,   61,   61,   61,   61,   61,   61,   61,
   61,   61,   61,   61,   61,   61,    0,   42,   42,   42,
   42,   42,   42,   42,   42,   42,   42,   42,   42,   42,
    0,   49,   49,   49,   49,   49,   49,   49,   49,   49,
   49,   49,   49,   49,   37,    0,    0,    0,   38,   37,
    0,   36,    0,   39,   37,   33,   33,   33,   33,   33,
   33,   33,   33,   33,   33,   33,   33,   33,    0,   53,
   53,   53,   53,   53,   53,   53,   53,   53,   53,   53,
   53,   53,   57,   17,    0,    0,    0,    0,    0,    0,
    0,    0,   57,    0,    0,   41,    0,   17,    0,    0,
    0,    0,    0,    0,    0,   41,    0,    0,   66,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   66,    0,
    0,    0,    8,    0,    0,    0,    0,    0,    0,    0,
    0,   44,    8,    0,   47,   37,    0,   37,    0,   31,
   51,   44,    0,    0,   47,   34,   55,    0,    0,   31,
   51,   40,    0,    0,    0,   34,   55,    0,    0,    0,
    0,   40,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   57,    0,   57,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   41,    0,   41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   66,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    8,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   44,    0,    0,   47,    0,    0,
    0,    0,   31,   51,    0,    0,    0,    0,   34,   55,
    0,    0,    0,    0,   40,    0,    0,    0,    0,    0,
    2,    3,    4,    5,    6,    7,    8,    9,   10,   11,
   12,   13,   14,   15,    2,    3,    0,    5,    0,    7,
    0,   30,   31,   11,   12,   13,   14,   15,    0,   44,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   37,   37,   37,   37,   37,   37,   37,
   37,   37,   37,   37,   37,   37,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   57,   57,   57,   57,   57,   57,   57,   57,   57,
   57,   57,   57,   57,   41,   41,   41,   41,   41,   41,
   41,   41,   41,   41,   41,   41,   41,   66,   66,   66,
   66,   66,   66,   66,   66,   66,   66,   66,   66,   66,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,   44,    0,   44,   47,   44,   47,    0,   47,   31,
   51,   31,   51,   31,   51,   34,   55,   34,   55,   34,
   55,   40,   22,   40,    0,   40,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   22,    0,
    0,   40,    0,    0,    0,    0,    0,   41,    0,   43,
    0,   50,    0,   40,    0,    0,   54,    0,    0,   41,
    0,   50,   58,   62,   66,   70,    0,    0,   74,    0,
   78,   82,   43,    0,   40,   50,    0,    0,   40,    0,
   41,   43,   40,   50,   41,   43,   40,   50,   41,   43,
   40,   50,   41,   43,   40,   50,   41,   43,   40,   50,
   41,   43,   40,   50,   41,   43,    0,   50,   41,   43,
    0,   50,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         -1,
    0,   42,   43,    0,   45,   -1,   47,  269,  270,  271,
   10,  267,  268,   -1,   -1,   -1,    0,   -1,   -1,   42,
   43,   -1,   45,   -1,   47,   -1,   10,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,    0,   -1,   42,   43,   -1,   45,
   -1,   47,   42,   43,   10,   45,   -1,   47,   -1,   -1,
    0,   -1,   -1,   -1,   -1,    1,   -1,   -1,   42,   43,
   10,   45,   -1,   47,    0,   -1,   -1,   -1,   91,   -1,
   93,   17,   -1,   -1,   10,   -1,   -1,   -1,    0,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   93,   10,   35,
   -1,   91,    0,   -1,   -1,   41,   42,   43,   44,   -1,
   -1,   47,   10,   49,   50,   -1,    0,   91,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   10,   -1,   -1,   -1,
    0,   -1,   -1,   -1,   -1,   91,   -1,   93,   -1,   -1,
   10,   -1,   -1,   -1,    0,   -1,   -1,   -1,   -1,   -1,
   -1,   91,   -1,   93,   10,   -1,   -1,   -1,    0,   -1,
   -1,   -1,   -1,   -1,   -1,   91,   -1,   93,   10,   -1,
   -1,   -1,    0,   -1,   -1,   -1,   -1,   -1,   -1,   91,
   -1,   93,   10,   -1,   -1,   -1,    0,   -1,   -1,    0,
   -1,   -1,   -1,   91,   -1,   93,   10,   -1,   -1,   10,
   -1,   -1,   -1,   -1,    0,   -1,   -1,   91,   -1,   93,
   -1,   -1,   -1,   -1,   10,   -1,   -1,   -1,   -1,   -1,
   -1,   91,   -1,   93,   -1,   42,   43,  258,   45,   -1,
   47,   -1,   -1,   -1,   -1,   91,   -1,   93,   -1,   -1,
   -1,   -1,  273,   -1,   -1,  258,  259,   -1,  261,   91,
  263,   93,  265,  266,  267,  268,  269,  270,  271,   -1,
  273,   42,   43,   91,   45,   93,   47,   -1,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  273,   -1,  269,
  270,  271,   93,  273,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,   91,   -1,   93,   -1,  273,
   -1,   -1,  258,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,   -1,   -1,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  267,  268,  269,
  270,  271,  258,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,  258,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  268,  269,  270,  271,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  270,  271,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,  269,  270,  271,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  267,  268,  269,
  270,  271,  258,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,  258,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  268,  269,  270,  271,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  270,  271,    0,   -1,  260,   -1,  262,  260,
  264,  262,   -1,  264,   10,   -1,  273,   -1,   -1,   -1,
   -1,   -1,  258,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,    0,   -1,   -1,   -1,
   -1,    1,   -1,   -1,   -1,   -1,   10,   -1,   -1,   -1,
    0,   -1,  273,   -1,   91,   -1,   -1,   17,   -1,   -1,
   10,   -1,   -1,   -1,    0,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   10,   35,   -1,   -1,    0,   -1,
   -1,   41,   42,   43,   44,   -1,   -1,   47,   10,   49,
   50,   -1,    0,   -1,   -1,   91,   -1,   93,   -1,   -1,
   -1,   -1,   10,   -1,   -1,   -1,    0,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   10,   -1,   -1,   -1,
    0,   -1,   -1,   -1,   -1,   -1,   -1,   91,   -1,   93,
   10,   -1,   -1,    1,    0,   -1,    4,   -1,   -1,   -1,
    8,   91,   10,   93,   10,   -1,   -1,   -1,    0,   -1,
   -1,   -1,   -1,   -1,   -1,   91,   -1,   93,   10,   -1,
   -1,   -1,    0,   -1,   -1,   -1,   -1,   35,   -1,   91,
   -1,   93,   10,   41,   42,   43,   44,   -1,   -1,   47,
   -1,   49,   50,   91,   -1,   93,    0,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   10,   91,   -1,   93,
    0,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   10,   91,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  258,  259,   -1,  261,   91,  263,   93,  265,  266,
  267,  268,  269,  270,  271,   -1,   -1,   -1,   -1,   91,
   -1,   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   93,   -1,   -1,   -1,   -1,
   -1,   -1,  258,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,   -1,   91,   -1,   93,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   91,   -1,   93,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,  269,  270,  271,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  267,  268,  269,
  270,  271,  258,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,  258,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  268,  269,  270,  271,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  270,  271,  258,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,  269,  270,  271,  258,  259,
  260,  261,  262,  263,  264,  265,  266,  267,  268,  269,
  270,  271,   10,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,   -1,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  268,  269,  270,  271,
   -1,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  270,  271,    0,   -1,   -1,   -1,   42,   43,
   -1,   45,   -1,   47,   10,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  268,  269,  270,  271,   -1,  259,
  260,  261,  262,  263,  264,  265,  266,  267,  268,  269,
  270,  271,    0,   91,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   10,   -1,   -1,    0,   -1,   91,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   10,   -1,   -1,    0,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   10,   -1,
   -1,   -1,    0,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,    0,   10,   -1,    0,   91,   -1,   93,   -1,    0,
    0,   10,   -1,   -1,   10,    0,    0,   -1,   -1,   10,
   10,    0,   -1,   -1,   -1,   10,   10,   -1,   -1,   -1,
   -1,   10,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   93,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   91,   -1,   93,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   91,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   93,   -1,   -1,   93,   -1,   -1,
   -1,   -1,   93,   93,   -1,   -1,   -1,   -1,   93,   93,
   -1,   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,   -1,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  270,  271,  258,  259,   -1,  261,   -1,  263,
   -1,  265,  266,  267,  268,  269,  270,  271,   -1,  273,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  259,  260,  261,  262,  263,  264,  265,
  266,  267,  268,  269,  270,  271,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,  269,  270,  271,  259,  260,  261,  262,  263,  264,
  265,  266,  267,  268,  269,  270,  271,  259,  260,  261,
  262,  263,  264,  265,  266,  267,  268,  269,  270,  271,
  258,  259,  260,  261,  262,  263,  264,  265,  266,  267,
  268,  260,   -1,  262,  260,  264,  262,   -1,  264,  260,
  260,  262,  262,  264,  264,  260,  260,  262,  262,  264,
  264,  260,    1,  262,   -1,  264,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   17,   -1,
   -1,   20,   -1,   -1,   -1,   -1,   -1,   20,   -1,   22,
   -1,   24,   -1,   32,   -1,   -1,   35,   -1,   -1,   32,
   -1,   34,   41,   42,   43,   44,   -1,   -1,   47,   -1,
   49,   50,   45,   -1,   53,   48,   -1,   -1,   57,   -1,
   53,   54,   61,   56,   57,   58,   65,   60,   61,   62,
   69,   64,   65,   66,   73,   68,   69,   70,   77,   72,
   73,   74,   81,   76,   77,   78,   -1,   80,   81,   82,
   -1,   84,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=275;
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
"OP_SAR","OP_LOG","OP_SLOG","OP_MISC","OP_JMP","OP_STO","OP_COMP","REG","LAB",
"ID","EQ","COMMA","APOSTROPHE","NEG",
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
"single : OP_SAR",
"single : OP_JMP desc",
"single : OP_MISC OP_STO",
"single : OP_SMOV desc",
"single : OP_SLOG desc",
"single : OP_MISC OP_COMP",
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
"ops : exp ops",
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

//#line 214 "parser.y"

/* === PROGRAM === */

	private RSyntaxTextArea source;
	private IScanner scanner;
	private Node<String,String> root = new Node<String, String>("TREE", "ROOT");
	private SparseUndirectedGraph graph;
	private no.roek.nlpged.graph.Graph graph2;
	private HashMap<Node, Integer> allNodes;

	public Parser(RSyntaxTextArea source, IScanner scanner) {
		this.source = source;
		this.scanner = scanner;
		this.graph = new SparseUndirectedGraph();
		this.allNodes = new HashMap<Node, Integer>();
		this.graph2 = new no.roek.nlpged.graph.Graph();
	}
	
	public Parser(RSyntaxTextArea source, IScanner scanner, boolean debugMe) {
        this(source, scanner);
        this.yydebug = debugMe;
    }

	void yyerror(String s) {
		System.err.println("parser: " + s);
	}

	private int count = 0, edge = 0;

	// TODO:
	private int yylex() {
		int tok = -1;
		try {
			
			tok = scanner.yylex();
			System.out.println(yyname[tok] + ": " + scanner.yytext());
			Node node = new Node(yyname[tok], scanner.yytext());
			no.roek.nlpged.graph.Node node2 = new no.roek.nlpged.graph.Node(String.valueOf(count), scanner.yytext(), new String[] {yyname[tok], scanner.yytext()});
			graph2.addNode(node2);
			allNodes.put(node, count);
			
			Object[] obj = {node, node2};
			
			yylval = new ParserVal(obj);
			
			count++;
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return tok;
	}
 	
 	public Node<String,String> getTree() {
 		return root;
 	}
 	
 	public SparseUndirectedGraph getGraph() {
 		return graph;
 	}
 	
 	public no.roek.nlpged.graph.Graph getGraph2() {
 	    return graph2;
 	}
 	
 	private ParserVal mergedCompute(Object... objs) {
 		ArrayList<Object[]> listOfNodes = new ArrayList<Object[]>();
 		for(int i = 0; i < objs.length; i++) {
 		    Object[] node = (Object[])objs[i];
 			listOfNodes.add(node);
		}
 		Object[] obj = new Object[2];
		if(listOfNodes.size() == 2) {
		    obj[0] = compute((Node)listOfNodes.get(0)[0], (Node)listOfNodes.get(1)[0]);
			obj[1] = compute2((no.roek.nlpged.graph.Node)listOfNodes.get(0)[1], (no.roek.nlpged.graph.Node)listOfNodes.get(1)[1]);
		} else if(listOfNodes.size() == 3) {
			obj[0] = compute((Node)listOfNodes.get(1)[0], (Node)listOfNodes.get(0)[0], (Node)listOfNodes.get(2)[0]).obj;
			obj[1] = compute2((no.roek.nlpged.graph.Node)listOfNodes.get(1)[1], (no.roek.nlpged.graph.Node)listOfNodes.get(0)[1], (no.roek.nlpged.graph.Node)listOfNodes.get(2)[1]).obj;
		}
		return new ParserVal(obj);
 	}
 	
 	private ParserVal compute(Node p, Node... child) {
 	    for(Node c : child) {
 	        p.addChild(c);
 	        graph.add(new SimpleEdge(allNodes.get(p),allNodes.get(c)));
 	    }
		return new ParserVal(p);
 	}
 	
 	private ParserVal compute2(no.roek.nlpged.graph.Node p, no.roek.nlpged.graph.Node... child) {
        for(no.roek.nlpged.graph.Node c : child) {
            graph2.addEdge(new Edge(String.valueOf(edge), p, c, p.getLabel() + " " + c.getLabel()));
            edge++;
        }
        return new ParserVal(p);
    }
//#line 652 "Parser.java"
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
//#line 37 "parser.y"
{ 
 								/*root.addChild((no.roek.nlpged.graph.Node)$1.obj);*/
 								/*root.addChild((Node)$1.obj);*/
 							}
break;
case 5:
//#line 43 "parser.y"
{ yyval = val_peek(0); }
break;
case 6:
//#line 44 "parser.y"
{ yyval = val_peek(0); }
break;
case 7:
//#line 45 "parser.y"
{ yyval = val_peek(0); }
break;
case 8:
//#line 48 "parser.y"
{ 
								yyval = val_peek(0); 
							}
break;
case 9:
//#line 51 "parser.y"
{ 
								yyval = val_peek(0); 
							}
break;
case 10:
//#line 54 "parser.y"
{ 
								yyval = mergedCompute(val_peek(1).obj, val_peek(0).obj);
							}
break;
case 11:
//#line 57 "parser.y"
{ 
								yyval = mergedCompute(val_peek(1).obj, val_peek(0).obj);
							}
break;
case 12:
//#line 60 "parser.y"
{ 
								yyval = mergedCompute(val_peek(1).obj, val_peek(0).obj);
							}
break;
case 13:
//#line 63 "parser.y"
{ 
								yyval = mergedCompute(val_peek(1).obj, val_peek(0).obj);
							}
break;
case 14:
//#line 66 "parser.y"
{ 
								yyval = mergedCompute(val_peek(1).obj, val_peek(0).obj);
							}
break;
case 15:
//#line 71 "parser.y"
{ yyval = val_peek(0); }
break;
case 16:
//#line 73 "parser.y"
{ yyval = val_peek(0); }
break;
case 17:
//#line 74 "parser.y"
{ yyval = val_peek(0); }
break;
case 18:
//#line 75 "parser.y"
{ yyval = val_peek(0); }
break;
case 19:
//#line 76 "parser.y"
{ yyval = val_peek(0); }
break;
case 20:
//#line 77 "parser.y"
{ yyval = val_peek(0); }
break;
case 21:
//#line 78 "parser.y"
{ yyval = val_peek(0); }
break;
case 22:
//#line 79 "parser.y"
{ yyval = val_peek(0); }
break;
case 23:
//#line 82 "parser.y"
{ yyval = val_peek(0); }
break;
case 24:
//#line 83 "parser.y"
{ yyval = val_peek(0); }
break;
case 25:
//#line 84 "parser.y"
{ yyval = val_peek(0); }
break;
case 26:
//#line 85 "parser.y"
{ yyval = val_peek(0); }
break;
case 27:
//#line 88 "parser.y"
{ 
								yyval = mergedCompute(val_peek(1).obj, val_peek(0).obj);
							}
break;
case 28:
//#line 91 "parser.y"
{ 
								yyval = mergedCompute(val_peek(1).obj, val_peek(0).obj);
							}
break;
case 29:
//#line 94 "parser.y"
{ 
								yyval = mergedCompute(val_peek(1).obj, val_peek(0).obj);
							}
break;
case 30:
//#line 98 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 31:
//#line 101 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 32:
//#line 104 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 33:
//#line 107 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 34:
//#line 111 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 35:
//#line 114 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 36:
//#line 117 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 37:
//#line 120 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 38:
//#line 124 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 39:
//#line 127 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 40:
//#line 130 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 41:
//#line 133 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 42:
//#line 137 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 43:
//#line 140 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 44:
//#line 143 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 45:
//#line 146 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 46:
//#line 150 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 47:
//#line 153 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 48:
//#line 156 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 49:
//#line 159 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 50:
//#line 163 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 51:
//#line 166 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 52:
//#line 169 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 53:
//#line 172 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 54:
//#line 176 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 55:
//#line 179 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 56:
//#line 182 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 57:
//#line 185 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 58:
//#line 189 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 59:
//#line 192 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 60:
//#line 195 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 61:
//#line 198 "parser.y"
{ 
								yyval = mergedCompute(val_peek(2).obj, val_peek(1).obj, val_peek(0).obj);
							}
break;
case 62:
//#line 202 "parser.y"
{ yyval = val_peek(1); }
break;
case 63:
//#line 203 "parser.y"
{ yyval = val_peek(1); }
break;
case 65:
//#line 207 "parser.y"
{ yyval = val_peek(0); }
break;
case 66:
//#line 208 "parser.y"
{ yyval = val_peek(0); }
break;
case 67:
//#line 209 "parser.y"
{ yyval = val_peek(0); }
break;
case 68:
//#line 210 "parser.y"
{ yyval = val_peek(0); }
break;
//#line 1144 "Parser.java"
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

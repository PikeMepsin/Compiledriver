/* This is the beginning of the lexer
 * Compilers CMPT 432
 * @author Mike Pepsin
 */

import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class lexer {
  public static void main(String[] args) {
    
    //hashmap for determining which column to use in the dfa matrix during lex
    HashMap<String, Integer> dfaColumns = new HashMap<>();
    dfaColumns.put("a", 0);
    dfaColumns.put("b", 1);
    dfaColumns.put("c", 2);
    dfaColumns.put("d", 3);
    dfaColumns.put("e", 4);
    dfaColumns.put("f", 5);
    dfaColumns.put("g", 6);
    dfaColumns.put("h", 7);
    dfaColumns.put("i", 8);
    dfaColumns.put("j", 9);
    dfaColumns.put("k", 10);
    dfaColumns.put("l", 11);
    dfaColumns.put("m", 12);
    dfaColumns.put("n", 13);
    dfaColumns.put("o", 14);
    dfaColumns.put("p", 15);
    dfaColumns.put("q", 16);
    dfaColumns.put("r", 17);
    dfaColumns.put("s", 18);
    dfaColumns.put("t", 19);
    dfaColumns.put("u", 20);
    dfaColumns.put("u", 21);
    dfaColumns.put("w", 22);
    dfaColumns.put("x", 23);
    dfaColumns.put("y", 24);
    dfaColumns.put("z", 25);
    dfaColumns.put("0", 26);
    dfaColumns.put("1", 27);
    dfaColumns.put("2", 28);
    dfaColumns.put("3", 29);
    dfaColumns.put("4", 30);
    dfaColumns.put("5", 31);
    dfaColumns.put("6", 32);
    dfaColumns.put("7", 33);
    dfaColumns.put("8", 34);
    dfaColumns.put("9", 35);
    dfaColumns.put("{", 36);
    dfaColumns.put("}", 37);
    dfaColumns.put("(", 38);
    dfaColumns.put(")", 39);
    dfaColumns.put("!", 40);
    dfaColumns.put("=", 41);
    dfaColumns.put("+", 42);
    dfaColumns.put("\"", 43);
    dfaColumns.put("/", 44);
    dfaColumns.put("*", 45);
    dfaColumns.put("$", 46);
    dfaColumns.put(" ", 47);
    
    //variables for lexing
    Scanner lex = new Scanner(System.in);
    String codeFragment = "";
    ArrayList<Token> tokenStream = new ArrayList<>();
    
    //counters
    int progCounter = 1;
    int warnings = 0;
    int errors = 0;
    int state = 1;
    
    //the following declarations are flags that we'll use later
    boolean inQuotes = false;
    boolean inComments = false;
    int inBrackets = 0;
    int inParens = 0;
        
    int dfa[][] = {/*a b c d e f g h i j k l m n o p q r s t u v w x y z 0 1 2 3 4 5 6 7 8 9 { } ( ) ! = + " / * $ space*/
    /* state 0 */   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 1 */   {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 2 */   {3,0,0,0,0,9,0,3,0,0,0,0,0,3,3,0,0,3,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 3 */   {0,0,0,0,0,0,0,0,4,0,0,4,0,0,5,0,0,4,0,9,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 4 */   {0,0,0,0,9,0,0,0,5,0,0,5,0,5,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 5 */   {0,0,0,0,9,0,0,0,0,0,0,6,0,6,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 6 */   {0,0,0,0,7,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 7 */   {8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 8 */   {0,0,0,0,0,0,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
    
    //position variables
    int line = 0;
    int pos = 0;
    
    //position variables for lexing
    int currentPos = 0;
    int lastPos = currentPos + 1;
    
    //main loop for lexing through the code
    while (lex.hasNextLine()) {
      while (lex.hasNext()) {
        String next = lex.next();
        pos++;
        if (!dfaColumns.containsKey(next)) {
          errors++;
          for (Token : tokenStream) {
            printStream(Token.type, Token.lexeme);
          }
          System.out.println("Lex terminated with " + errors + "error(s) at [" + line + "," + pos + "]");
          System.out.println("ERROR: Unrecognized character " + next);
          // TODO remove break, move on to next program once error is found after 1 is working
          break;
        }
        
        codeFragment += next;
        System.out.println(codeFragment);
      }
      line++;
    }
  }
  
  public static class Token {
    //class variables
    public String type;
    public String lexeme;
    
    public Token(String type, String lexeme) {
      this.type = type;
      this.lexeme = lexeme;
    }
  }
  
  public static void emit(Token t) {
    //method to emit tokens
  }
}
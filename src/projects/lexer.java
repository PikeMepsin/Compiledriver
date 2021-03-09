/* This is the beginning of the lexer
 * Compilers CMPT 432
 * @author Mike Pepsin
 */

import java.util.Scanner;
import java.util.HashMap;

public class lexer {
  
  //hashmap for determining which column to use in the dfa matrix during lex
  HashMap<Integer, String> dfaColumns = new HashMap<>();
  dfaColumns.put(0, "a");
  dfaColumns.put(1, "b");
  dfaColumns.put(2, "c");
  dfaColumns.put(3, "d");
  dfaColumns.put(4, "e");
  dfaColumns.put(5, "f");
  dfaColumns.put(6, "g");
  dfaColumns.put(7, "h");
  dfaColumns.put(8, "i");
  dfaColumns.put(9, "j");
  dfaColumns.put(10, "k");
  dfaColumns.put(11, "l");
  dfaColumns.put(12, "m");
  dfaColumns.put(13, "n");
  dfaColumns.put(14, "o");
  dfaColumns.put(15, "p");
  dfaColumns.put(16, "q");
  dfaColumns.put(17, "r");
  dfaColumns.put(18, "s");
  dfaColumns.put(19, "t");
  dfaColumns.put(20, "u");
  dfaColumns.put(21, "v");
  dfaColumns.put(22, "w");
  dfaColumns.put(23, "x");
  dfaColumns.put(24, "y");
  dfaColumns.put(25, "z");
  dfaColumns.put(26, "0");
  dfaColumns.put(27, "1");
  dfaColumns.put(28, "2");
  dfaColumns.put(29, "3");
  dfaColumns.put(30, "4");
  dfaColumns.put(31, "5");
  dfaColumns.put(32, "6");
  dfaColumns.put(33, "7");
  dfaColumns.put(34, "8");
  dfaColumns.put(35, "9");
  dfaColumns.put(36, "{");
  dfaColumns.put(37, "}");
  dfaColumns.put(38, "(");
  dfaColumns.put(39, ")");
  dfaColumns.put(40, "!");
  dfaColumns.put(41, "=");
  dfaColumns.put(42, "+");
  dfaColumns.put(43, "\"");
  dfaColumns.put(44, "/");
  dfaColumns.put(45, "*");
  dfaColumns.put(46, "$");
  dfaColumns.put(47, " ");
  
  public static void main(String[] args) {
    
    //variables for lexing
    Scanner lex = new Scanner(System.in);
    String lexeme = "";
    
    //counters
    int progCounter = 1;
    int warnings = 0;
    int errors = 0;
    
    //the following declarations are flags that we'll use later
    boolean inQuotes = false;
    boolean inComments = false;
    int inBrackets = 0;
    int inParens = 0;
        
    int dfa[][] = {/*a b c d e f g h i j k l m n o p q r s t u v w x y z 0 1 2 3 4 5 6 7 8 9 { } ( ) ! = + " / * $  */
    /* state 0 */   {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 1 */   {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 2 */   {3,0,0,0,0,9,0,5,0,0,0,0,0,3,3,0,0,3,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 3 */   {0,0,0,0,6,0,0,0,4,0,0,4,0,0,4,0,0,4,0,9,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 4 */   {0,0,0,0,9,0,0,0,5,0,0,3,0,5,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 5 */   {0,0,0,0,9,0,0,0,9,0,0,0,0,9,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 6 */   {7,0,0,0,0,0,9,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
    /* state 7 */   {0,0,0,0,0,0,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
    
    while (lex.hasNextLine()) {
      while (lex.hasNext()) {
        code = lex.next();
        System.out.println(code);
      }
    }
  }
  
  public static void emit(Token t) {
    //method to emit tokens
  }
}
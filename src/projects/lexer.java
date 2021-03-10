/* This is the beginning of the lexer
 * Compilers CMPT 432
 * @author Mike Pepsin
 */

import java.util.*;

public class lexer {
  public static void main(String[] args) {
    
    //hashmap for matching accepted states to their symbols
    HashMap<Integer, String> tokenMatch = new HashMap<>();
    tokenMatch.put(9, "PRINT");
    tokenMatch.put(10, "WHILE");
    tokenMatch.put(11, "IF");
    tokenMatch.put(12, "INT");
    tokenMatch.put(13, "STRING");
    tokenMatch.put(14, "BOOLEAN");
    tokenMatch.put(15, "BOOL_T");
    tokenMatch.put(16, "BOOL_F");
    tokenMatch.put(17, "CHAR");
    //TODO: Digits
    
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
        
    int dfa[][] = {/*a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z  0  1  2  3  4  5  6  7  8  9  {  }  (  )  !  =  +  "  /  *  $  space*/
    /* state 0 */   {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    /* state 1 */   {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    /* state 2 */   {3, 0, 0, 0, 0,11, 0, 3, 0, 0, 0, 0, 0, 3, 3, 0, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
    /* state 3 */   {0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 4, 0, 0, 5, 0, 0, 4, 0,12, 4, 0, 0, 0, 0, 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
    /* state 4 */   {0, 0, 0, 0,15, 0, 0, 0, 5, 0, 0, 5, 0, 5, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
    /* state 5 */   {0, 0, 0, 0,10, 0, 0, 0, 0, 0, 0, 6, 0, 6, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
    /* state 6 */   {0, 0, 0, 0, 7, 0,13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
    /* state 7 */   {8, 0, 0, 0,16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
    /* state 8 */   {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}};
    
    //position variables
    int line = 1;
    int currentPos = 0;
    int lastPos = currentPos + 1;    
    
    //rebuild the input as a String
    //this implementation means that lines are read one-at-a-time, so even with good formatting,
    //tokens cannot exist on separate lines.
    //grab the line loop
    StringBuffer sb = new StringBuffer();
    while (lex.hasNextLine()) {
      sb.append(lex.nextLine());
      codeFragment = sb.toString();
      String tokenSoFar = "";
      int pos = 0;
      
      //lex the line loop
      while (sb.capacity() > 0) {
        String next = codeFragment.substring(lastPos-1, lastPos);
        tokenSoFar += next;
        System.out.println(codeFragment + " " + tokenSoFar);
        if (dfaColumns.containsKey(next)) {
          int col = dfaColumns.get(next).intValue();
          state = dfa[state][col];
          System.out.println(col + " " + state);
          if (state > 0) {
            if (state == 2) { //2 is the accepted state for ID
              System.out.println("got here");
              Token token = new Token("ID", tokenSoFar, line, pos);
              
              //next character doesn't follow the path to a keyword, so we can emit it as an ID
              int temp = dfaColumns.get(codeFragment.substring(lastPos, lastPos+1)).intValue();
              if (dfa[state][temp] <= 0) {
                tokenStream.add(token);
                pos++;
                sb.delete(0, lastPos);
                codeFragment = sb.toString();
                System.out.println(codeFragment);
                lastPos++;
              }
              //else is does follow the path to a keyword so we keep going
              else {
                lastPos++;
              }
            }
            //accepted states for keywords
            else if (state >= 9 && state < 17) {
              Token token = new Token(tokenMatch.get(state), tokenSoFar, line, pos);
              tokenStream.add(token);
              pos += tokenMatch.get(state).length();
              sb.delete(0, lastPos);
              codeFragment = sb.toString();
              System.out.println(codeFragment);
              printStream(tokenStream, progCounter);
              tokenSoFar = "";
              lastPos = 1;
              state = 1;
            }
            else {
              System.out.println("got here 2");
              col = dfaColumns.get(codeFragment.substring(lastPos, lastPos+1)).intValue();
              state = dfa[state][col];
              lastPos++;
              System.out.println(col + " " + state);
            }
          }
          else if (state == -1) {
            Token token = new Token("Symbol", tokenSoFar, line, pos);
            tokenStream.add(token);
            pos += tokenSoFar.length();
            sb.delete(0, lastPos);
            codeFragment = sb.toString();
            lastPos = 1;
          }
          else {
            System.out.println("got here 3");
            col = dfaColumns.get(codeFragment.substring(lastPos, lastPos+1)).intValue();
            state = dfa[state][col];
            lastPos++;
            System.out.println(col + " " + state);
          }
          
        }
        
        /*currentPos = lastPos;
        lastPos++;
        codeFragment = sb.toString();*/
      }
      
      line++;
    }
    
    printStream(tokenStream, progCounter);
    /*
    while (lex.hasNextLine()) {
      while (lex.hasNext()) {
        String next = lex.next();
        pos++;
        //Unrecognized character error
        if (!dfaColumns.containsKey(next)) {
          errors++;
          printStream(tokenStream);
          System.out.println("Lex terminated with " + errors + "error(s) at [" + line + "," + pos + "]");
          System.out.println("ERROR: Unrecognized character " + next);
          // TODO remove break, move on to next program once error is found after 1 is working
          break;
        }
        else {
          if (dfaColumns.getValue(next)) {
            String tempType = "ID";
          } 
        }
        
        codeFragment += next;
        System.out.println(codeFragment);
      }
      line++;
      pos = 0;
    }
    */
  }
  
  public static class Token {
    //class variables
    public String type;
    public String lexeme;
    int lineNum;
    int position;
    
    public Token(String type, String lexeme, int lineNum, int position) {
      this.type = type;
      this.lexeme = lexeme;
      this.lineNum = lineNum;
      this.position = position;
    }
  }
  
  public static void printStream(ArrayList tokens, int progNum) {
    System.out.println("INFO - Compilation started");
    System.out.println("INFO - Compiling Program " + progNum);
    for (Object token: tokens) {
      /*String output = String.format("DEBUG - Lexer - %s [ %s ] found at (%d:%d)", token.type, token.lexeme, token.lineNum, token.position);
      System.out.println(output);*/
      System.out.println(token);
    }
  }
}
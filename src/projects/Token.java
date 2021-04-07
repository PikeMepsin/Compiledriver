/**
 * This is the representation of the accepted tokens in lex and onward  
 * @author Mike
 *
 */

public class Token {
    //class variables
    public String name;
    public String lexeme;
    public int lineNum;
    public int position;
    
    public Token(String name, String lexeme, int lineNum, int position) {
      this.name = name;
      this.lexeme = lexeme;
      this.lineNum = lineNum;
      this.position = position;
    }
  }
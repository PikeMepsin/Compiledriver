import java.util.*;
import java.io.*;

public class lexer1 {
  public enum TokenNames {
    //accepted tokens and their naming convention
    PRINT("print"),
    WHILE("while"),
    IF("if"),
    TYPEINT("int"),
    TYPESTRING("string"),
    TYPEBOOLEAN("boolean"),
    BOOLVALT("true"),
    BOOLVALF("false"),
    ID("[a-z]{1}"),
    EOP("[$]"),
    BOOLEQ("=="),
    BOOLINEQ("!="),
    QUOTE("\""),
    INCROP("[+]"),
    ASSIGNOP("="),
    OPENPAREN("[(]"),
    CLOSEPAREN("[)]"),
    LBRACE("[{]"),
    RBRACE("[}]"),
    NUM("[0-9]"),
    CHAR("[a-z]"),
    WHITESPACE("[\\s]+"),
    ERR(".");
    
    public String pattern;
    TokenNames(String pattern) {
      this.pattern = pattern;
    }
  }
  public static void main(String[] args) {
    Scanner lex = new Scanner(System.in);
    int progCounter = 1;
    int warnings = 0;
    int errors = 0;
    
    boolean inQuotes = false;
    boolean inComments = false;
    boolean inBrackets = false;
    boolean inParens = false;
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
  
  public static void printStream(ArrayList<Token> tokens, int progNum) {
    String output = "";
    System.out.println("INFO - Compilation started");
    System.out.println("INFO - Compiling Program " + progNum);
    for (Token token: tokens) {
      output = String.format("DEBUG - Lexer - %s [ %s ] found at (%d:%d", token.type, token.lexeme, token.lineNum, token.position);
    }
  }
}
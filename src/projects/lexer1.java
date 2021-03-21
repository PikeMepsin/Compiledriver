import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*;

public class lexer1 {
  
  public static enum TokenNames {
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
    CRLF("[\\n]"),
    WHITESPACE("[\\s]+"),
    ERR(".");
    
    public String pattern;
    TokenNames(String pattern) {
      this.pattern = pattern;
    }
  }
  public static void main(String[] args) {
    ArrayList<Token> tokens = new ArrayList<>();
    int progCounter = 1;
  
    Scanner lex = new Scanner(System.in);
    int warnings = 0;
    int errors = 0;
    int line = 1;
    
    boolean inQuotes = false;
    boolean inComments = false;
    boolean inBrackets = false;
    boolean inParens = false;
    
    while (lex.hasNextLine()) {
      StringBuffer input = new StringBuffer();
      input.append(lex.nextLine());
      
      StringBuffer patternBuilder = new StringBuffer();
      
      for (TokenNames tokenName : TokenNames.values()) {
        patternBuilder.append(String.format("|<%s>%s", tokenName.name(), tokenName.pattern));
      }
            
      Pattern lexemes = Pattern.compile(new String(patternBuilder.substring(1)), Pattern.DOTALL);
      Matcher snoop = lexemes.matcher(input);
      System.out.println(input);
      //System.out.println(patternBuilder);
      MatchResult result = snoop.toMatchResult();
      System.out.println(result);
      while(snoop.find()) {
        System.out.println("snoop found");
        if (snoop.group(TokenNames.PRINT.name()) != null) {
          tokens.add(new Token("PRINT", snoop.group(TokenNames.PRINT.name()),
          line, snoop.start()));
          System.out.println("print token added");
        }
        else if (snoop.group(TokenNames.EOP.name()) != null) {
          tokens.add(new Token("EOP", snoop.group(TokenNames.EOP.name()),
          line, snoop.start()));
          System.out.println("print token added");
          printStream(tokens, progCounter);
          progCounter++;
          line = 1;
          tokens.clear();
        }
      }
    }
    
  }
  
  public static class Token {
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
  
  public static void printStream(ArrayList<Token> tokens, int progNum) {
    String output = "";
    System.out.println("INFO - Compilation started");
    System.out.println("INFO - Compiling Program " + progNum);
    for (Token token: tokens) {
      output = String.format("DEBUG - Lexer - %s [ %s ] found at (%d:%d", token.name, token.lexeme, token.lineNum, token.position);
    }
  }
}
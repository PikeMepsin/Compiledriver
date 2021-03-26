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
    ID("[a-z]"),
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
    
    public final String pattern;
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
    boolean printed = false;
    
    while (lex.hasNextLine()) {
      printed = false;
      String input = lex.nextLine();
      
      StringBuffer patternBuilder = new StringBuffer();
      
      for (TokenNames tokenName : TokenNames.values()) {
        patternBuilder.append(String.format("|(?<%s>%s)", tokenName.name(), tokenName.pattern));
      }
            
      Pattern lexemes = Pattern.compile(patternBuilder.substring(1), Pattern.DOTALL);
      Matcher snoop = lexemes.matcher(input);
      
      while(snoop.find()) {
    	//for testing purposes
        //System.out.println("snoop found");
        //System.out.println(snoop.group());
        
        if (snoop.group(TokenNames.WHITESPACE.name()) != null) {
          // process whitespace, do nothing with it
        }
        else if (snoop.group(TokenNames.PRINT.name()) != null) {
          tokens.add(new Token("PRINT", snoop.group(TokenNames.PRINT.name()),
          line, snoop.start()));
          //System.out.println("print token added");
        }
        else if (snoop.group(TokenNames.WHILE.name()) != null) {
          tokens.add(new Token("WHILE", snoop.group(TokenNames.WHILE.name()),
          line, snoop.start()));
        }
        else if (snoop.group(TokenNames.IF.name()) != null) {
          tokens.add(new Token("IF", snoop.group(TokenNames.IF.name()),
          line, snoop.start()));
        }
        else if (snoop.group(TokenNames.TYPEINT.name()) != null) {
          tokens.add(new Token("TYPEINT", snoop.group(TokenNames.TYPEINT.name()),
          line, snoop.start()));
        }
        else if (snoop.group(TokenNames.TYPESTRING.name()) != null) {
          tokens.add(new Token("TYPESTRING", snoop.group(TokenNames.TYPESTRING.name()),
          line, snoop.start()));
        }
        else if (snoop.group(TokenNames.TYPEBOOLEAN.name()) != null) {
          tokens.add(new Token("TYPEBOOLEAN", snoop.group(TokenNames.TYPEBOOLEAN.name()),
          line, snoop.start()));
        }
        else if (snoop.group(TokenNames.BOOLVALT.name()) != null) {
          tokens.add(new Token("BOOLVALT", snoop.group(TokenNames.BOOLVALT.name()),
          line, snoop.start()));
        }
        else if (snoop.group(TokenNames.BOOLVALF.name()) != null) {
          tokens.add(new Token("BOOLVALF", snoop.group(TokenNames.BOOLVALF.name()),
          line, snoop.start()));
        }
        else if (snoop.group(TokenNames.ID.name()) != null) {
          tokens.add(new Token("ID", snoop.group(TokenNames.ID.name()),
          line, snoop.start()));
        }
        else if (snoop.group(TokenNames.EOP.name()) != null) {
          tokens.add(new Token("EOP", snoop.group(TokenNames.EOP.name()),
          line, snoop.start()));
          //System.out.println("print token added");
          printStream(tokens, progCounter);
          printed = true;
          progCounter++;
          line = 1;
          tokens.clear();
        }
      }
      line++;
    }
    if (!printed) {
      Token lastToken = tokens.get(tokens.size() -1);
      String lastLexeme = lastToken.lexeme;
      if (!lastLexeme.equals("$") && !printed) {
        warnings++;
        printStream(tokens, progCounter);
        System.out.println("Finished lexing with " + warnings + " warnings and " + errors + " errors");
        System.out.println("No end-of-program symbol found");
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
      output = String.format("DEBUG - Lexer - %s [ %s ] found at (%d:%d)", token.name, token.lexeme, token.lineNum, token.position);
      System.out.println(output);
    }
  }
}
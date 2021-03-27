import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*;
import java.lang.Math;

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
    VALIDCOMMENT("(?<=)\\/\\*.*?(?=)\\*\\/"),
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
        else if (inQuotes && !(snoop.group(TokenNames.ID.name()) != null || snoop.group(TokenNames.QUOTE.name()) != null)) {
          // for the life of me, I don't know why I have to use TokenNames.ID.name() here instead of CHAR.
          // they have identical regex but CHAR doesn't work for some reason
          // anyway, this processes anything in quotes as an error if its not a "char" or another quote
          tokens.add(new Token("ERROR", snoop.group(), line, snoop.start()));
          errors++;
        }
        else if (snoop.group(TokenNames.VALIDCOMMENT.name()) != null) {
          // process comment, do nothing with it
        }
        else if (snoop.group(TokenNames.PRINT.name()) != null) {
          tokens.add(new Token("PRINT", snoop.group(TokenNames.PRINT.name()),
              line, snoop.start()));
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
          if (inQuotes) {
            tokens.add(new Token("CHAR", snoop.group(TokenNames.ID.name()),
                line, snoop.start()));
          }
          else {
            tokens.add(new Token("ID", snoop.group(TokenNames.ID.name()),
                line, snoop.start()));
          }
        }
        else if (snoop.group(TokenNames.BOOLEQ.name()) != null) {
        tokens.add(new Token("BOOLEQ", snoop.group(TokenNames.BOOLEQ.name()),
            line, snoop.start()));
        }
        else if (snoop.group(TokenNames.BOOLINEQ.name()) != null) {
          tokens.add(new Token("BOOLINEQ", snoop.group(TokenNames.BOOLINEQ.name()),
              line, snoop.start()));
        }
        else if (snoop.group(TokenNames.ASSIGNOP.name()) != null) {
          tokens.add(new Token("ASSIGNOP", snoop.group(TokenNames.ASSIGNOP.name()),
              line, snoop.start()));
        }
        else if (snoop.group(TokenNames.INCROP.name()) != null) {
          tokens.add(new Token("INCROP", snoop.group(TokenNames.INCROP.name()),
              line, snoop.start()));
        }
        else if (snoop.group(TokenNames.QUOTE.name()) != null) {
          if (!inQuotes) {
            inQuotes = true;
          }
          else {
            inQuotes = false;
          }
          tokens.add(new Token("QUOTE", snoop.group(TokenNames.QUOTE.name()),
              line, snoop.start()));
        }
        else if (snoop.group(TokenNames.OPENPAREN.name()) != null) {
          tokens.add(new Token("OPENPAREN", snoop.group(TokenNames.OPENPAREN.name()),
              line, snoop.start()));
        }
        else if (snoop.group(TokenNames.CLOSEPAREN.name()) != null) {
          tokens.add(new Token("CLOSEPAREN", snoop.group(TokenNames.CLOSEPAREN.name()),
              line, snoop.start()));
        }
        else if (snoop.group(TokenNames.LBRACE.name()) != null) {
          tokens.add(new Token("LBRACE", snoop.group(TokenNames.LBRACE.name()),
              line, snoop.start()));
        }
        else if (snoop.group(TokenNames.RBRACE.name()) != null) {
          tokens.add(new Token("RBRACE", snoop.group(TokenNames.RBRACE.name()),
              line, snoop.start()));
        }
        else if (snoop.group(TokenNames.NUM.name()) != null) {
          tokens.add(new Token("NUM", snoop.group(TokenNames.NUM.name()),
              line, snoop.start()));
        }
        else if (snoop.group(TokenNames.CHAR.name()) != null) {
          tokens.add(new Token("CHAR", snoop.group(TokenNames.CHAR.name()),
              line, snoop.start()));
        }
        //TODO add effects for inBrackets, inParens
        else if (snoop.group(TokenNames.EOP.name()) != null) {
          tokens.add(new Token("EOP", snoop.group(TokenNames.EOP.name()),
              line, snoop.start()));
          printStream(tokens, progCounter, errors, warnings);
          printed = true;
          progCounter++;
          line = 1;
          tokens.clear();
        }
        //TODO fix error messaging, maybe add as token and have special output
        else if (snoop.group(TokenNames.ERR.name()) != null) {
          tokens.add(new Token("ERROR", snoop.group(TokenNames.ERR.name()),
              line, snoop.start()));
          errors++;
          
        }
      }
      line++;
    }
    if (!printed) {
      Token lastToken = tokens.get(tokens.size() -1);
      String lastLexeme = lastToken.lexeme;
      if (!lastLexeme.equals("$") && !printed) {
        warnings++;
        printStream(tokens, progCounter, errors, warnings);
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
  
  public static void printStream(ArrayList<Token> tokens, int progNum, int err, int warn) {
    String output = "";
    System.out.println("INFO - Compilation started");
    System.out.println("INFO - Compiling Program " + progNum);
    for (Token token: tokens) {
      output = String.format("DEBUG - Lexer - %s [ %s ] found at (%d:%d)", token.name, token.lexeme, token.lineNum, token.position);
      if (token.name == "ERROR") {
        output = String.format("ERROR at line (%d:%d) - %s is not an accepted character", token.lineNum, token.position, token.lexeme);
      }
      System.out.println(output);
    }
    System.out.println("Lex completed with " + err + " error(s) and " + warn + " warning(s)");
  }
}
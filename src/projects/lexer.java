import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*;
import java.lang.Math;

/**
 * Project 1 for Compilers: The Lexer
 * Professor Alan Labouseur
 * CMPT432
 * @author Michael Pepsin
 */

public class lexer {
  
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
    INVALIDCOMMENT("(/\\*)"),
    CRLF("[\\r\\n]+"),
    SPACE(" "),
    WHITESPACE("[\\s]+"),
    ERR(".");
    
    public final String pattern;
    TokenNames(String pattern) {
      this.pattern = pattern;
    }
  }
  public static void main(String[] args) {
    ArrayList<Token> tokens = new ArrayList<>();
    Scanner lex = new Scanner(System.in);
    
    // counters
    int progCounter = 1;
    int warnings = 0;
    int errors = 0;
    int line = 1;
    
    // flags
    boolean inQuotes = false;
    boolean inComments = false;
    boolean printed = false;
    
    // this is the lex loop, reading line by line from a text file
    while (lex.hasNextLine()) {
      printed = false;
      String input = lex.nextLine();
      
      // construct the pattern
      StringBuffer patternBuilder = new StringBuffer();
      for (TokenNames tokenName : TokenNames.values()) {
        patternBuilder.append(String.format("|(?<%s>%s)", tokenName.name(), tokenName.pattern));
      }
      
      // initialize pattern and matcher objects
      Pattern lexemes = Pattern.compile(patternBuilder.substring(1), Pattern.DOTALL);
      Matcher snoop = lexemes.matcher(input);
      
      while(snoop.find()) {
    	//for testing purposes
        //System.out.println("snoop found");
        //System.out.println(snoop.group());
        
        
        if (snoop.group(TokenNames.WHITESPACE.name()) != null) {
          // process whitespace, do nothing with it
        }
        else if (snoop.group(TokenNames.EOP.name()) != null) {
          // the EOP symbol is the be-all end-all, we check for it early
          tokens.add(new Token("EOP", snoop.group(TokenNames.EOP.name()),
              line, snoop.start()+1));
          printStream(tokens, progCounter, errors, warnings, inQuotes, inComments);
          printed = true;
          progCounter++;
          tokens.clear();
          errors = 0;
          warnings = 0;
        }
        else if (inComments) {
          // you're in a comment, silly, nothing is going to happen
          // seriously though, this is the endpoint only if there was an unclosed comment block
          // that's what makes it different from the next
        }
        else if (snoop.group(TokenNames.VALIDCOMMENT.name()) != null) {
          // process comment, do nothing with it
        }
        else if (snoop.group(TokenNames.INVALIDCOMMENT.name()) != null) {
          inComments = true;
        }
        else if (inQuotes) {
          // for the life of me, I don't know why I have to use TokenNames.ID.name() here instead of CHAR.
          // they have identical regex but CHAR doesn't work for some reason
          // anyway, this processes anything in quotes as an error if its not a "char" or another quote
          if (snoop.group(TokenNames.ID.name()) != null) {
            tokens.add(new Token("CHAR", snoop.group(TokenNames.ID.name()),
                line, snoop.start()+1));
          }
          else if (snoop.group(TokenNames.QUOTE.name()) != null) {
            tokens.add(new Token("QUOTE", snoop.group(TokenNames.QUOTE.name()),
                line, snoop.start()+1));
            inQuotes = false;
          }
          else if (snoop.group(TokenNames.CRLF.name()) != null) {
            // doesn't work
            // System.out.println("CRLF found");
            tokens.add(new Token("ERROR", "\n", line, snoop.start()+1));
          }
          else if (snoop.group(TokenNames.SPACE.name()) != null) {
            tokens.add(new Token("SPACE", snoop.group(TokenNames.SPACE.name()),
                line, snoop.start()+1));
          }
          else if (snoop.group(TokenNames.PRINT.name()) != null || snoop.group(TokenNames.IF.name()) != null || snoop.group(TokenNames.WHILE.name()) != null || 
              snoop.group(TokenNames.TYPEINT.name()) != null || snoop.group(TokenNames.TYPESTRING.name()) != null || snoop.group(TokenNames.TYPEBOOLEAN.name()) != null || 
              snoop.group(TokenNames.BOOLVALT.name()) != null || snoop.group(TokenNames.BOOLVALF.name()) != null) {
            // this is admittedly a bruce force tactic, but it tokenizes keywords without accepting invalid characters
            String keyword = snoop.group();
            int index = snoop.start()+1;
            for (int z=0; z<keyword.length(); z++) {
              tokens.add(new Token("CHAR", keyword.substring(z, z+1), line, index+z));
            }
          }
          else {
            tokens.add(new Token("ERROR", snoop.group(), line, snoop.start()+1));
            errors++;
          }
        }
        // keyword matching
        else if (snoop.group(TokenNames.PRINT.name()) != null) {
          tokens.add(new Token("PRINT", snoop.group(TokenNames.PRINT.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.WHILE.name()) != null) {
          tokens.add(new Token("WHILE", snoop.group(TokenNames.WHILE.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.IF.name()) != null) {
          tokens.add(new Token("IF", snoop.group(TokenNames.IF.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.TYPEINT.name()) != null) {
          tokens.add(new Token("TYPEINT", snoop.group(TokenNames.TYPEINT.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.TYPESTRING.name()) != null) {
          tokens.add(new Token("TYPESTRING", snoop.group(TokenNames.TYPESTRING.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.TYPEBOOLEAN.name()) != null) {
          tokens.add(new Token("TYPEBOOLEAN", snoop.group(TokenNames.TYPEBOOLEAN.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.BOOLVALT.name()) != null) {
          tokens.add(new Token("BOOLVALT", snoop.group(TokenNames.BOOLVALT.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.BOOLVALF.name()) != null) {
          tokens.add(new Token("BOOLVALF", snoop.group(TokenNames.BOOLVALF.name()),
              line, snoop.start()+1));
        }
        // ID matching
        else if (snoop.group(TokenNames.ID.name()) != null) {
          if (inQuotes) {
            tokens.add(new Token("CHAR", snoop.group(TokenNames.ID.name()),
                line, snoop.start()+1));
          }
          else {
            tokens.add(new Token("ID", snoop.group(TokenNames.ID.name()),
                line, snoop.start()+1));
          }
        }
        // symbol, num, and char matching
        else if (snoop.group(TokenNames.BOOLEQ.name()) != null) {
        tokens.add(new Token("BOOLEQ", snoop.group(TokenNames.BOOLEQ.name()),
            line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.BOOLINEQ.name()) != null) {
          tokens.add(new Token("BOOLINEQ", snoop.group(TokenNames.BOOLINEQ.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.ASSIGNOP.name()) != null) {
          tokens.add(new Token("ASSIGNOP", snoop.group(TokenNames.ASSIGNOP.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.INCROP.name()) != null) {
          tokens.add(new Token("INCROP", snoop.group(TokenNames.INCROP.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.QUOTE.name()) != null) {
          if (!inQuotes) {
            inQuotes = true;
          }
          else {
            inQuotes = false;
          }
          tokens.add(new Token("QUOTE", snoop.group(TokenNames.QUOTE.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.OPENPAREN.name()) != null) {
          tokens.add(new Token("OPENPAREN", snoop.group(TokenNames.OPENPAREN.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.CLOSEPAREN.name()) != null) {
          tokens.add(new Token("CLOSEPAREN", snoop.group(TokenNames.CLOSEPAREN.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.LBRACE.name()) != null) {
          tokens.add(new Token("LBRACE", snoop.group(TokenNames.LBRACE.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.RBRACE.name()) != null) {
          tokens.add(new Token("RBRACE", snoop.group(TokenNames.RBRACE.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.NUM.name()) != null) {
          tokens.add(new Token("NUM", snoop.group(TokenNames.NUM.name()),
              line, snoop.start()+1));
        }
        else if (snoop.group(TokenNames.CHAR.name()) != null) {
          tokens.add(new Token("CHAR", snoop.group(TokenNames.CHAR.name()),
              line, snoop.start()+1));
        }
        // anything that we haven't caught yet is not part of the grammar, it is added as an error token
        // error tokens are handled slightly differently later for the sake of the output
        else if (snoop.group(TokenNames.ERR.name()) != null) {
          tokens.add(new Token("ERROR", snoop.group(TokenNames.ERR.name()),
              line, snoop.start()+1));
          errors++;
          
        }
      }
      line++;
    }
    // this statement catches any program(s) that don't end in EOP
    if (!printed && !tokens.isEmpty()) {
      printStream(tokens, progCounter, errors, warnings, inQuotes, inComments);
    }
    
  }
  
  public static void printStream(ArrayList<Token> tokens, int progNum, int err, int warn, boolean openQ, boolean openC) {
    // print method, called after EOP is reached, or at the end of input otherwise
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
    
    if (openQ) {
      System.out.println("Program ended inside a quote");
      warn++;
    }
    if (openC) {
      System.out.println("Program ended inside comments");
      warn++;
    }
    Token lastToken = tokens.get(tokens.size()-1);
    String lastLexeme = lastToken.lexeme;
    if (!lastLexeme.equals("$")) {
      System.out.println("No end-of-program symbol found");
      warn++;
    }
    if (err == 0) {
      System.out.println("Lex completed with " + err + " error(s) and " + warn + " warning(s)");
      System.out.println("INFO - Parsing Program " + progNum);
      parser parse = new parser(tokens);
      parse.parseProgram();
    }
    else {
      System.out.println("Lex failed with " + err + " error(s) and " + warn + " warnings(s)\n"
          + "INFO- Not skipping PARSE because of LEX errors.");
    }
    
    // for formatting multi-program lex
    System.out.println();
  }
}
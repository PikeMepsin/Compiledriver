import java.util.ArrayList;

public class parser {
  
  int errors = 0;
  ArrayList<Token> stream = new ArrayList<Token>();
  CST tree = new CST();
  
  public parser() {
    // default constructor
  }
  
  public parser(ArrayList<Token> lexOutput) {
    stream = lexOutput;
    int index = 0;
    
    if (stream.size() < 3) {
      System.out.println("PARSE ERROR: a program must at least consist of {}$");
    }
    parseProgram();
  }
  
  public boolean parseProgram() {
    boolean isMatch = false;
    System.out.println("PARSE: Program");
    
    tree.growBranch("Program");
    parseBlock();
    isMatch = match("$");
    // for formatting
    System.out.println();
    
    return isMatch;
  }
  
  public boolean parseBlock() {
    boolean isMatch = false;
    // TODO
    return isMatch;
  }
  
  public boolean parseStatementList() {
    boolean isMatch = false;
    // TODO
    return isMatch;
  }
  public boolean parseEOP() {
    boolean isMatch = false;
    // TODO
    return isMatch;
  }
  
  public boolean match(String expectedToken) {
    boolean isMatch = false;
    if (errors == 0) {
      // TODO
    }
    return isMatch;
  }
  
}
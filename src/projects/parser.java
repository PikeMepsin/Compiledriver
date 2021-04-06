import java.util.ArrayList;

public class parser {
  
  int index = 0;
  int errors = 0;
  ArrayList<Token> input = new ArrayList<Token>();
  CST tree = new CST();
  
  public parser() {
    // default constructor
  }
  
  public parser(ArrayList<Token> lexOutput) {
    input = lexOutput;
    
    if (input.size() < 3) {
      System.out.println("PARSE ERROR: a program must at least consist of {}$");
    }
    parseProgram();
  }
  
  public boolean parseProgram() {
    boolean isMatch = false;
    System.out.println("PARSE: Program");
    
    tree.growBranch("Program");
    parseBlock();
    isMatch = match("EOP");
    // for formatting
    System.out.println();
    
    return isMatch;
  }
  
  public boolean parseBlock() {
    boolean isMatch = false;
    if (errors == 0) {
      match("LBRACE");
      parseStatementList();
      isMatch = match("RBRACE");
    }
    return isMatch;
  }
  
  public boolean parseStatementList() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parsePrintStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseAssignmentStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseVarDecl() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseWhileStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseIfStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseExpr() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseIntExpr() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseStringExpr() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }

  public boolean parseBooleanExpr() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseId() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseCharList() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseType() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseChar() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseSpace() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }
  
  public boolean parseDigit() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }

  public boolean parseBoolOp() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }

  public boolean parseBoolVal() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }

  public boolean parseIntOp() {
    boolean isMatch = false;
    if (errors == 0) {
      
    }
    return isMatch;
  }




  
  public boolean match(String expectedToken) {
    boolean isMatch = false;
    if (errors == 0) {
      if (input.get(index).name.equals(expectedToken)) {
        tree.sproutLeaf(input.get(index).lexeme);
        isMatch = true;
      }
      
      if (index < input.size()) {
        index++;
      }
      
      if (!isMatch) {
        String errMessage = String.format("ERROR at (%d:%d) : PARSER expected %s, found $s", input.get(index).lineNum, input.get(index).position, expectedToken, input.get(index).lexeme);
        System.out.println(errMessage);
        errors++;
      }
    }
    return isMatch;
  }
  
}
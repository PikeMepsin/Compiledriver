import java.util.ArrayList;

public class parser {
  
  int index = 0;
  int errors = 0;
  
  // flag for printing in verbose mode
  boolean verbose = true;
  
  ArrayList<Token> input = new ArrayList<Token>();
  CST tree = new CST();
  
  public parser() {
    // default constructor
  }
  
  public parser(ArrayList<Token> lexOutput) {
    input = lexOutput;
    
  }
  
  public boolean parseProgram() {
    boolean isMatch = false;
    // flag for deciding whether or not to print
    boolean validProg = false;
    
    if (input.size() < 3) {
      System.out.println("PARSE ERROR: a program must at least consist of {}$");
      errors++;
    }
    if (verbose) {
      System.out.println("PARSE: Program");
    }
    
    tree.growBranch("Program");
    parseBlock();
    isMatch = match("EOP");
    validProg = isMatch;
    if (validProg) {
      treeTrace();
      // call for project 3 will go here
    }
    return isMatch;
  }
  
  public boolean parseBlock() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Block");
      }
      tree.growBranch("Block");
      isMatch = match("LBRACE");
      parseStatementList();
      isMatch = match("RBRACE");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseStatementList() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: StatementList");
      }
      if (input.get(index).name.equals("PRINT") || input.get(index).name.equals("ID") || input.get(index).name.equals("TYPEINT") ||
          input.get(index).name.equals("TYPESTRING") || input.get(index).name.equals("TYPEBOOLEAN") || input.get(index).name.equals("WHILE") ||
          input.get(index).name.equals("IF") || input.get(index).name.equals("LBRACE")) {
        isMatch = parseStatement();
        isMatch = parseStatementList();
      }
      else {
        // epsilon production
      }
      tree.growBranch("StatementList");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Statement");
      }
      tree.growBranch("Statement");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parsePrintStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: PrintStatement");
      }
      tree.growBranch("PrintStatement");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseAssignmentStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: AssignmentStatement");
      }
      tree.growBranch("AssignmentStatement");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseVarDecl() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: VarDecl");
      }
      tree.growBranch("VarDecl");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseWhileStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: WhileStatement");
      }
      tree.growBranch("WhileStatement");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseIfStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: IfStatement");
      }
      tree.growBranch("IfStatement");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseExpr() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Expr");
      }
      tree.growBranch("Expr");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseIntExpr() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: IntExpr");
      }
      tree.growBranch("IntExpr");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseStringExpr() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: StringExpr");
      }
      tree.growBranch("StringExpr");
      tree.climb();
    }
    return isMatch;
  }

  public boolean parseBooleanExpr() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: BooleanExpr");
      }
      tree.growBranch("BooleanExpr");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseId() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Id");
      }
      tree.growBranch("Id");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseCharList() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: CharList");
      }
      tree.growBranch("CharList");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseType() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Type");
      }
      tree.growBranch("Type");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseChar() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Char");
      }
      tree.growBranch("Char");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseSpace() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Space");
      }
      tree.growBranch("Space");
      tree.climb();
    }
    return isMatch;
  }
  
  public boolean parseDigit() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Digit");
      }
      tree.growBranch("Digit");
      tree.climb();
    }
    return isMatch;
  }

  public boolean parseBoolOp() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: BoolOp");
      }
      tree.growBranch("BoolOp");
      tree.climb();
    }
    return isMatch;
  }

  public boolean parseBoolVal() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: BoolVal");
      }
      tree.growBranch("BoolVal");
      tree.climb();
    }
    return isMatch;
  }

  public boolean parseIntOp() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: IntOp");
      }
      tree.growBranch("IntOp");
      tree.climb();
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
  
  public void treeTrace() {
    tree.printCST(tree.root, 0);
  }
  
}
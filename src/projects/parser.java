import java.util.ArrayList;

/**
 * Project 2 for Compilers: The Parser
 * Professor Alan Labouseur
 * CMPT 432
 * @author Mike
 */

public class parser {
  
  int index = 0; // arraylist pointer
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
  
  // start here, by parsing a program (called from Compiledriver.java)
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
      System.out.println("Parse completed bug-free");
      // call for project 3 will go here
    }
    else {
      System.out.println("\nCST skipped due to PARSER errors");
    }
    return isMatch;
  }
  
  // block
  public boolean parseBlock() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Block");
      }
      tree.growBranch("Block");
      isMatch = match("LBRACE");
      isMatch = parseStatementList();
      isMatch = match("RBRACE");
      tree.climb();
    }
    return isMatch;
  }
  
  // statement list
  public boolean parseStatementList() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: StatementList");
      }
      tree.growBranch("StatementList");
      
      if (input.get(index).name.equals("PRINT") || input.get(index).name.equals("ID") || input.get(index).name.equals("TYPEINT") ||
          input.get(index).name.equals("TYPESTRING") || input.get(index).name.equals("TYPEBOOLEAN") || input.get(index).name.equals("WHILE") ||
          input.get(index).name.equals("IF") || input.get(index).name.equals("LBRACE")) {
        isMatch = parseStatement();
        isMatch = parseStatementList();
      }
      else {
        // epsilon production
      }
      tree.climb();
    }
    return isMatch;
  }
  
  // statement
  public boolean parseStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Statement");
      }
      tree.growBranch("Statement");
      
      // handle the various statement modes
      if (input.get(index).name.equals("PRINT")) {
        isMatch = parsePrintStatement();
      }
      else if (input.get(index).name.equals("ID")) {
        isMatch = parseAssignmentStatement();
      }
      else if (input.get(index).name.equals("TYPEINT") || input.get(index).name.equals("TYPESTRING") || input.get(index).name.equals("TYPEBOOLEAN")) {
        isMatch = parseVarDecl();
      }
      else if (input.get(index).name.equals("WHILE")) {
        isMatch = parseWhileStatement();
      }
      else if (input.get(index).name.equals("IF")) {
        isMatch = parseIfStatement();
      }
      else {
        isMatch = parseBlock();
      }
      tree.climb();
    }
    return isMatch;
  }
  
  // print statement
  public boolean parsePrintStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: PrintStatement");
      }
      tree.growBranch("PrintStatement");
      isMatch = match("PRINT");
      isMatch = match("OPENPAREN");
      isMatch = parseExpr();
      isMatch = match("CLOSEPAREN");
      tree.climb();
    }
    return isMatch;
  }
  
  // assignment statement
  public boolean parseAssignmentStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: AssignmentStatement");
      }
      tree.growBranch("AssignmentStatement");
      parseId();
      isMatch = match("ASSIGNOP");
      parseExpr();
      tree.climb();
    }
    return isMatch;
  }
  
  // variable declaration
  public boolean parseVarDecl() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: VarDecl");
      }
      tree.growBranch("VarDecl");
      isMatch = parseType();
      isMatch = parseId();
      tree.climb();
    }
    return isMatch;
  }
  
  // while loop
  public boolean parseWhileStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: WhileStatement");
      }
      tree.growBranch("WhileStatement");
      isMatch = match("WHILE");
      parseBooleanExpr();
      isMatch = parseBlock();
      tree.climb();
    }
    return isMatch;
  }
  
  // if statement
  public boolean parseIfStatement() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: IfStatement");
      }
      tree.growBranch("IfStatement");
      isMatch = match("IF");
      parseBooleanExpr();
      isMatch = parseBlock();
      tree.climb();
    }
    return isMatch;
  }
  
  // parse expressions into smaller expressions
  public boolean parseExpr() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Expr");
      }
      tree.growBranch("Expr");
      
      // integer expressions
      if (input.get(index).name.equals("NUM")) {
        isMatch = parseIntExpr();
      }
      // boolean expressions
      else if (input.get(index).name.equals("OPENPAREN") || input.get(index).name.equals("BOOLVALT") || input.get(index).name.equals("BOOLVALF")) {
        isMatch = parseBooleanExpr();
      }
      // string expressions
      else if (input.get(index).name.equals("QUOTE")) {
        isMatch = parseStringExpr();
      }
      // IDs
      else {
        isMatch = parseId();
      }
        
      tree.climb();
    }
    return isMatch;
  }
  
  // integer expressions
  public boolean parseIntExpr() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: IntExpr");
      }
      tree.growBranch("IntExpr");
      
      // a digit, alone in an unforgiving compiler
      parseDigit();
      
      // digit + Expr
      if (input.get(index).name.equals("INCROP")) {
        isMatch = match("INCROP");
        parseExpr();
      }
      tree.climb();
    }
    return isMatch;
  }
  
  // string expressions
  public boolean parseStringExpr() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: StringExpr");
      }
      tree.growBranch("StringExpr");
      isMatch = match("QUOTE");
      parseCharList();
      isMatch = match("QUOTE");
      tree.climb();
    }
    return isMatch;
  }

  // boolean expressions
  public boolean parseBooleanExpr() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: BooleanExpr");
      }
      tree.growBranch("BooleanExpr");
      
      // boolean expressions in parens()
      if (input.get(index).name.equals("OPENPAREN")) {
        isMatch = match("OPENPAREN");
        parseExpr();
        parseBoolOp();
        parseExpr();
        isMatch = match("CLOSEPAREN");
      }
      // boolean values
      else {
        parseBoolVal();
      }
      tree.climb();
    }
    return isMatch;
  }
  
  // parse IDs
  public boolean parseId() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Id");
      }
      tree.growBranch("Id");
      isMatch = match("ID");
      tree.climb();
    }
    return isMatch;
  }
  
  // character list
  public boolean parseCharList() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: CharList");
      }
      tree.growBranch("CharList");
    }
    // character
    if (input.get(index).name.equals("CHAR")) {
      if (errors == 0) {
        if (verbose) {
          System.out.println("PARSE: Char");
        }
        tree.growBranch("Char");
        isMatch = match("CHAR");
        tree.climb();
        isMatch = parseCharList();
      }
    }
    // space
    else if (input.get(index).name.equals("SPACE")) {
      if (errors == 0) {
        if (verbose) {
          System.out.println("PARSE: Space");
        }
        tree.growBranch("Char");
        isMatch = match("SPACE");
        tree.climb();
        isMatch = parseCharList();
      }
    }
    else {
      // epsilon production
    }
    tree.climb();
    return isMatch;
  }
  
  // parse types, int string and bool
  public boolean parseType() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Type");
      }
      tree.growBranch("Type");
      
      // int declaration
      if (input.get(index).name.equals("TYPEINT")) {
        isMatch = match("TYPEINT");
      }
      // boolean declaration
      else if (input.get(index).name.equals("TYPEBOOLEAN")) {
        isMatch = match("TYPEBOOLEAN");
      }
      // string declaration
      else {
        isMatch = match("TYPESTRING");
      }
      tree.climb();
    }
    return isMatch;
  }
  
  // parse numbers
  public boolean parseDigit() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: Digit");
      }
      tree.growBranch("Digit");
      isMatch = match("NUM");
      tree.climb();
    }
    return isMatch;
  }

  // bool ops, equality and inequality
  public boolean parseBoolOp() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: BoolOp");
      }
      tree.growBranch("BoolOp");
      
      // ==
      if (input.get(index).name.equals("BOOLEQ")) {
        isMatch = match("BOOLEQ");
      }
      // !=
      else {
        isMatch = match("BOOLINEQ");
      }
      tree.climb();
    }
    return isMatch;
  }

  // bool values, true and false terminals
  public boolean parseBoolVal() {
    boolean isMatch = false;
    if (errors == 0) {
      if (verbose) {
        System.out.println("PARSE: BoolVal");
      }
      tree.growBranch("BoolVal");
      
      // true
      if (input.get(index).name.equals("BOOLVALT")) {
        isMatch = match("BOOLVALT");
      }
      // false
      else {
        isMatch = match("BOOLVALF");
      }
      tree.climb();
    }
    return isMatch;
  }


  // match method for checking expected vs. actual
  public boolean match(String expectedToken) {
    boolean isMatch = false;
    if (errors == 0) {
      if (input.get(index).name.equals(expectedToken)) {
        tree.sproutLeaf(input.get(index).lexeme);
        isMatch = true;
      }
      
      // prevents index out-of-bounds and moves pointer to next token
      if (index < input.size()-1) {
        index++;
      }
      
      // catch errors, expected vs. actual tokens
      if (!isMatch) {
        String errMessage = String.format("ERROR at (%d:%d) : PARSER expected %s, found %s", input.get(index).lineNum, input.get(index).position, expectedToken, input.get(index).lexeme);
        System.out.println(errMessage);
        errors++;
      }
    }
    return isMatch;
  }
  
  // print method for errorless parse
  public void treeTrace() {
    tree.printCST(tree.root, 0);
  }
  
}
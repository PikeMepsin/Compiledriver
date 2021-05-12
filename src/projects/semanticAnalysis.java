import java.util.*;

public class semanticAnalysis {
  // ADT declarations
  CST AST = new CST();
  ArrayList<Scope> symbolTable = new ArrayList<Scope>();
  
  // counters
  int currentScope = -1;
  int typeErrs = 0;
  
  // flags
  boolean errors = false;
  boolean exists = false;
  
  public semanticAnalysis() {
    // default constructor
  }
  
  public CSTNode plant(CSTNode node) {
    // TODO
    
    if (node.token.equals("LBRACE")) {
      AST.growBranch("Block");
      
      if (currentScope == -1) {
        symbolTable.add(new Scope());
      }
      else {
        symbolTable.add(new Scope(currentScope));
      }
      
      currentScope = symbolTable.size()-1;
    }
    else if (node.token.equals("RBRACE")) {
      AST.climb();
      if (symbolTable.get(currentScope).prevScope != -2) {
        currentScope = symbolTable.get(currentScope).prevScope;
      }
    }
    else if (node.token.equals("PrintStatement")) {
      AST.growBranch("Print");
      
      CSTNode expression = null;
      expression = node.tree.get(2);
      
      exprBranches(expression, true);
    }
    
    
    return node;
  }
  
  public String exprBranches(CSTNode exp, boolean ret) {
    // print statement
    if (exp.tree.get(0).token.equals("StringExpr")) {
      String word = "";
      CSTNode charlist = exp.tree.get(0).tree.get(1);
      
      while (charlist.tree.size() != 0) {
        word = word + charlist.tree.get(0).tree.get(0).token;
        charlist = charlist.tree.get(1);
      }
      
      AST.sproutLeaf(word);
      
      return "String";
    }
    return "Error";
  }
}

class scopeNode {
  // represents variable scope
  String vName = "";
  String vType = "";
  int vLine = 0;
  int vPos = 0;
  int vScope = -1;
  boolean inUse = false;
  boolean initialized = false;
  ArrayList<Integer> varScope = new ArrayList<>();
  
  public scopeNode() {
    // default constructor
  }
  
  public scopeNode(String name, String type, int line, int pos, int scope) {
    vName = name;
    vType = type;
    vLine = line;
    vPos = pos;
    vScope = scope;
  }
}

class Scope {
  // arraylist abstraction of symbol table
  int prevScope = -2;
  
  // one for each letter of the alphabet, because IDs must be length 1
  scopeNode[] vars = new scopeNode[26];
  
  // the base scope
  public Scope() {
    for (int n=0; n<26; n++) {
      vars[n] = null;
    }
  }
  
  // additional scopes are created as needed and are linked in a 
  // stack-like fashion via prevScope
  public Scope(int p) {
    prevScope = p;
    for (int n=0; n<26; n++) {
      vars[n] = null;
    }
  }
  
  
}
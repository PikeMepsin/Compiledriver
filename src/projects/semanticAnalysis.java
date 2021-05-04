import java.util.*;

public class semanticAnalysis {
  // ADT declarations
  CST AST = new CST();
  ArrayList<Scope> symbolTable = new ArrayList<Scope>();
  
  // counters
  int currentScope = -1;
  int typeErr = 0;
  
  // flags
  boolean errors = false;
  boolean exists = false;
  
  public semanticAnalysis() {
    // default constructor
  }
  
  public CSTNode plant(CSTNode node) {
    // TODO
    return node;
  }
}

class scopeNode {
  // represents variable scope
  String vName = "";
  String vType = "";
  int vLine = 0;
  int vPos = 0;
  int vScope = 0;
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
  int prevScope = -1;
  
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
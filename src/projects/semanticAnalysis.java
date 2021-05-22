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
  boolean end = false;
  
  public semanticAnalysis() {
    // default constructor
  }
  
  public void plant(CSTNode node) {
    // TODO
    System.out.println(node.token);
    
    if (node.token.equals("$")) {
      end = true;
    }
    else if (node.token.equals("{")) {
      AST.growBranch("Block");
      
      if (currentScope == -1) {
        symbolTable.add(new Scope());
      }
      else {
        symbolTable.add(new Scope(currentScope));
      }
      
      currentScope = symbolTable.size()-1;
    }
    else if (node.token.equals("}")) {
      AST.climb();
      if (symbolTable.get(currentScope).prevScope != -2) {
        currentScope = symbolTable.get(currentScope).prevScope;
      }
    }
    // print statement
    else if (node.token.equals("PrintStatement")) {
      AST.growBranch("Print");
      
      CSTNode expression = null;
      expression = node.tree.get(2);
      
      exprBranches(expression, false);
      AST.climb();
    }
    // variable declaration
    else if (node.token.equals("VarDecl")) {
      // assignment statement doesn't need the '=' or another expression
      // it will always need only Id and type
      AST.growBranch("VarDecl");
      AST.sproutLeaf(node.tree.get(0).tree.get(0).token);
      AST.sproutLeaf(node.tree.get(1).tree.get(0).token, node.tree.get(0).tree.get(0).token, currentScope);
      
      // consult the symbol table for Id availability
      errors = symbolTable.get(currentScope).inTable(node.tree.get(1).tree.get(0).token, node.tree.get(0).tree.get(0).token, currentScope);
      
      if (errors) {
        typeErrs++;
      }
      errors = false;
      AST.climb();
    }
    else if (node.token.equals("AssignmentStatement")) {
      String typ = "";
      AST.growBranch("Assign");
      
      AST.sproutLeaf(node.tree.get(0).tree.get(0).token, node.tree.get(0).tree.get(0).type, currentScope);
      
      typ = exprBranches(node.tree.get(2), true);
      
      errors = symbolTable.get(currentScope).typeMisCheck(node.tree.get(0).tree.get(0).token, typ, currentScope, symbolTable, currentScope);
      
      if (errors) {
        typeErrs++;
      }
      errors = false;
      AST.climb();
    }
    else if (node.token.equals("IfStatement")) {
      AST.growBranch("If");
      if (node.tree.get(1).tree.size() == 1) {
        AST.sproutLeaf(node.tree.get(1).tree.get(0).tree.get(0).token, node.tree.get(1).tree.get(0).tree.get(0).token, currentScope);
      }
      else {
        String type1 = "";
        String type2 = "";
        
        if (node.tree.get(1).tree.get(2).tree.get(0).token.equals("BOOLINEQ")) {
          AST.growBranch("Not Equal");
        }
        else {
          AST.growBranch("Equal");
        }
        
        type1 = exprBranches(node.tree.get(1).tree.get(1), false);
        type2 = exprBranches(node.tree.get(1).tree.get(3), true);
        
        if (!(type1.equals(type2))) {
          typeErrs++;
          System.out.println("SEMANTIC ERROR: Type mismatch in if statement, cannot compare " + type1 + " and " + type2);
        }
      }
    }
    else if (node.token.equals("WhileStatement")) {
      AST.growBranch("While");
      
      if (node.tree.get(1).tree.size() == 1) {
        AST.sproutLeaf(node.tree.get(1).tree.get(0).tree.get(0).token, node.tree.get(1).tree.get(0).tree.get(0).token, currentScope);
      }
      else {
        String type1 = "";
        String type2 = "";
        
        if (node.tree.get(1).tree.get(2).tree.get(0).token.equals("BOOLINEQ")) {
          AST.growBranch("Not Equal");
        }
        else {
          AST.growBranch("Equal");
        }
        
        type1 = exprBranches(node.tree.get(1).tree.get(1), false);
        type2 = exprBranches(node.tree.get(1).tree.get(3), true);
        
        if (!(type1.equals(type2))) {
          typeErrs++;
          System.out.println("SEMANTIC ERROR: Type mismatch in boolean comparison");
        }
      }
    }
    
    if (end) {
      AST.printCST(AST.root, 0);
    }
    else if (node.tree.size() != 0) {
      for (int b=0; b<node.tree.size(); b++) {
        plant(node.tree.get(b));
      }
    }
    
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
      
      AST.sproutLeaf(word, "String", currentScope);
      
      return "String";
    }
    else if (exp.tree.get(0).token.equals("IntExpr")) {
      if (exp.tree.get(0).tree.size() == 1) {
        AST.sproutLeaf(exp.tree.get(0).tree.get(0).tree.get(0).token, exp.tree.get(0).tree.get(0).tree.get(0).token, currentScope);
        
        if (ret) {
          AST.climb();
        }
      }
      else {
        String ttype = "";
        AST.growBranch("Add");
        AST.sproutLeaf(exp.tree.get(0).tree.get(0).tree.get(0).token, exp.tree.get(0).tree.get(0).tree.get(0).type, currentScope);
        
        ttype = exprBranches(exp.tree.get(0).tree.get(2), true);
        
        if (!(ttype.equals("Int"))) {
          typeErrs++;
          System.out.println("SEMANTIC ERROR: Expected Int when adding, got " + ttype);
        }
        
        if (ret) {
          AST.climb();
        }
      }
      
      return "Int";
    }
    else if (exp.tree.get(0).token.equals("BooleanExpr")) {
      if (exp.tree.get(0).tree.size() == 1) {
        AST.sproutLeaf(exp.tree.get(0).tree.get(0).tree.get(0).token, exp.tree.get(0).tree.get(0).tree.get(0).token, currentScope);
        
        if (!ret) {
          AST.climb();
        }
      }
      else {
        String ttype1 = "";
        String ttype2 = "";
        if (exp.tree.get(0).tree.get(2).tree.get(0).token.equals("BOOLINEQ")) {
          AST.growBranch("Not Equal");
        }
        else {
          AST.growBranch("Equal");
        }
        
        ttype1 = exprBranches(exp.tree.get(0).tree.get(1), true);
        ttype2 = exprBranches(exp.tree.get(0).tree.get(1), false);
        if (!(ttype1.equals(ttype2))) {
          typeErrs++;
          System.out.println("SEMANTIC ERROR: Type mismatch in boolean comparison");
        }
        AST.climb();
      }
      
      return "Bool";
    }
    else if (exp.tree.get(0).token.equals("Id")) {
      AST.sproutLeaf(exp.tree.get(0).tree.get(0).token);
      
      exists = symbolTable.get(currentScope).doesExist(exp.tree.get(0).tree.get(0).token, currentScope, symbolTable);
      if (exists) {
        errors = true;
      }
      else {
        exists = false;
        typeErrs++;
        return "Id";
      }
    }
    return "Error";
  }
}

class scopeNode {
  // represents variable scope
  String vName = "";
  String vType = "";
  int vScope = -1;
  boolean inUse = false;
  boolean initialized = false;
  ArrayList<Integer> varScope = new ArrayList<>();
  
  public scopeNode() {
    // default constructor
  }
  
  public scopeNode(String name, String type, int scope) {
    vName = name;
    vType = type;
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
  
  public boolean inTable(String id, String type, int scope) {
    boolean exists = false;
    char[] alphabet = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    
    int col = 0;
    char check = id.charAt(0);
    
    for (int i=0; i<alphabet.length; i++) {
      if (check == alphabet[i]) {
        col = i;
      }
    }
    scopeNode temp = new scopeNode(id, type, scope);
    
    if (vars[col] == null) {
      vars[col] = temp;
    }
    else {
      exists = true;
      System.out.println("SEMANTIC ERROR: Variable " + id + " in scope " + scope + " already exists");
    }
    
    return exists;
  }
  
  public boolean typeMisCheck(String id, String type, int scope, ArrayList<Scope> table, int ogScope) {
    boolean err = false;
    char[] alphabet = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    
    int col = 0;
    char check = id.charAt(0);
    
    for (int q=0; q<alphabet.length; q++) {
      if (check == alphabet[q]) {
        col = q;
      }
    }
    
    if (vars[col] == null && prevScope != -2) {
      err = table.get(prevScope).typeMisCheck(id, type, prevScope, table, ogScope);
    }
    else if (vars[col] == null && prevScope == -2) {
      System.out.println("SEMANTIC ERROR: Variable " + id + " used before declared");
      err = true;
    }
    else if (vars[col] != null) {
      if (!(vars[col].vType.equals(type))) {
        System.out.println("SEMANTIC ERROR: Type mismatch, var " + id + " of type " + vars[col].vType + " cannot be compared to " + type);
        err = true;
      }
      else {
        vars[col].initialized = true;
        vars[col].varScope.add(ogScope);
      }
    }
    
    return err;
  }
  
  public boolean doesExist(String id, int scope, ArrayList<Scope> table) {
    boolean exists = false;
    boolean initialized = false;
    char[] alphabet = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    
    int col = 0;
    char check = id.charAt(0);
    
    for (int j=0; j<alphabet.length; j++) {
      if (check == alphabet[j]) {
        col = j;
      }
    }
    
    if (vars[col] == null) {
      exists = false;
      System.out.println("SEMANTIC ERROR: Variable " + id + " in scope " + scope + " used before declared");
    }
    else {
      exists = true;
      vars[col].inUse = true;
      // ok, it's declared, check if it has a value bound to it
      initialized = table.get(scope).isInitialized(id, scope, table);
    }
    
    return exists;
  }
  
  public boolean isInitialized(String id, int scope, ArrayList<Scope> table) {
    boolean inited = false;
    char[] alphabet = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    
    int col = 0;
    char check = id.charAt(0);
    
    for (int k=0; k<alphabet.length; k++) {
      if (check == alphabet[k]) {
        col = k;
      }
    }
    
    if (vars[col] != null) {
      for (int i=0; i<vars[col].varScope.size(); i++) {
        if (vars[col].varScope.get(i) == scope) {
          inited = true;
        }
        else {
          System.out.println("SEMANTIC ERROR: Variable " + id + " in scope "+ scope + " is not initialized");
        }
      }
    }
    
    return inited;
  }
  
}
import java.util.*;

public class semanticAnalysis {
  // ADT declarations
  CST AST = new CST();
  ArrayList<Scope> symbolTable = new ArrayList<Scope>();
  ArrayList<ErrMessage> errList = new ArrayList<ErrMessage>();
  
  // counters
  int currentScope = -1;
  int typeErrs = 0;
  
  // flags
  boolean errors = false;
  boolean exists = false;
  boolean end = false;
  
  // for knowing when to climb
  boolean inBlock = false;
  boolean inIf = false;
  
  public semanticAnalysis() {
    // default constructor
  }
  
  public void plant(CSTNode node, int progNum) {
    //System.out.println(node.token);
    
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
      errors = symbolTable.get(currentScope).inTable(node.tree.get(1).tree.get(0).token, node.tree.get(0).tree.get(0).token, currentScope, errList);
      
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
      
      errors = symbolTable.get(currentScope).typeMisCheck(node.tree.get(0).tree.get(0).token, typ, currentScope, symbolTable, currentScope, errList);
      
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
        
        inIf = true;
        
        if (node.tree.get(1).tree.get(2).tree.get(0).token.equals("!=")) {
          AST.growBranch("Not Equal");
        }
        else {
          AST.growBranch("Equal");
        }
        
        type1 = exprBranches(node.tree.get(1).tree.get(1), false);
        type2 = exprBranches(node.tree.get(1).tree.get(3), true);
        
        if (!(type1.equals(type2))) {
          typeErrs++;
          String e = "SEMANTIC ERROR: Type mismatch in if statement, cannot compare " + type1 + " and " + type2;
          errList.add(new ErrMessage(e));
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
        
        if (node.tree.get(1).tree.get(2).tree.get(0).token.equals("!=")) {
          AST.growBranch("Not Equal");
        }
        else {
          AST.growBranch("Equal");
        }
        
        type1 = exprBranches(node.tree.get(1).tree.get(1), false);
        type2 = exprBranches(node.tree.get(1).tree.get(3), true);
        
        inBlock = true;
        
        if (!(type1.equals(type2))) {
          typeErrs++;
          String e = "SEMANTIC ERROR: Type mismatch in boolean comparison";
          errList.add(new ErrMessage(e));
        }
      }
    }
    
    if (end) {
      System.out.println("INFO - Semantic Analysis Program " + progNum);
      System.out.println("SEMANTIC - Printing AST");
      AST.printCST(AST.root, 0);
      if (typeErrs > 0) {
        System.out.println("\n" + typeErrs + " type errors");
      }
      for (ErrMessage r : errList) {
        System.out.println(r.message);
      }
      System.out.println();
      if (errList.size() == 0 && typeErrs == 0) {
        System.out.println("SEMANTIC - AST Error-free, printing Symbol Table");
        printSymTable();
      }
      else {
        System.out.println("SEMANTIC - Error - Skipping Symbol Table");
        System.out.println("SEMANTIC - Error - Skipping CodeGen");
      }
    }
    else if (node.tree.size() != 0) {
      for (int b=0; b<node.tree.size(); b++) {
        plant(node.tree.get(b), progNum);
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
      
      if (!inBlock) {
        if (ret) {
          AST.climb();
        }
      }
      
      return "string";
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
        
        if (!(ttype.equals("int"))) {
          typeErrs++;
          String e = "SEMANTIC ERROR: Expected Int when adding, got " + ttype;
          errList.add(new ErrMessage(e));
        }
        
        if (ret) {
          AST.climb();
        }
      }
      
      return "int";
    }
    else if (exp.tree.get(0).token.equals("BooleanExpr")) {
      if (exp.tree.get(0).tree.size() == 1) {
        AST.sproutLeaf(exp.tree.get(0).tree.get(0).tree.get(0).token, exp.tree.get(0).tree.get(0).tree.get(0).token, currentScope);
        
        if (ret) {
          AST.climb();
        }
      }
      else {
        String ttype1 = "";
        String ttype2 = "";
        if (exp.tree.get(0).tree.get(2).tree.get(0).token.equals("!=")) {
          AST.growBranch("Not Equal");
        }
        else {
          AST.growBranch("Equal");
        }
        
        ttype1 = exprBranches(exp.tree.get(0).tree.get(1), true);
        ttype2 = exprBranches(exp.tree.get(0).tree.get(1), false);
        if (!(ttype1.equals(ttype2))) {
          typeErrs++;
          String e= "SEMANTIC ERROR: Type mismatch in boolean comparison";
          errList.add(new ErrMessage(e));
        }
        AST.climb();
      }
      
      return "boolean";
    }
    else if (exp.tree.get(0).token.equals("Id")) {
      AST.sproutLeaf(exp.tree.get(0).tree.get(0).token);
      
      exists = symbolTable.get(currentScope).doesExist(exp.tree.get(0).tree.get(0).token, currentScope, symbolTable, currentScope, errList);
      
      if (!inBlock) {
        if (ret) {
          AST.climb();
        }
      }
      if (inIf && ret) {
        AST.climb();
        AST.climb();
        inIf = false;
      }
    }
    return "Error";
  }
  
  public void printSymTable() {
    for (int c=0; c<symbolTable.size(); c++) {
      for (int d=0; d<26; d++) {
        if (symbolTable.get(c).vars[d] != null) {
          if (symbolTable.get(c).vars[d].vType.equals("int")) {
            System.out.println("Id: " + symbolTable.get(c).vars[d].vName + "   Type: " + symbolTable.get(c).vars[d].vType + "   Scope: " + symbolTable.get(c).vars[d].vScope);
          }
          else if (symbolTable.get(c).vars[d].vType.equals("string")) {
            System.out.println("Id: " + symbolTable.get(c).vars[d].vName + "   Type: " + symbolTable.get(c).vars[d].vType + "   Scope: " + symbolTable.get(c).vars[d].vScope);
          }
          else if (symbolTable.get(c).vars[d].vType.equals("boolean")) {
            System.out.println("Id: " + symbolTable.get(c).vars[d].vName + "   Type: " + symbolTable.get(c).vars[d].vType + "   Scope: " + symbolTable.get(c).vars[d].vScope);
          }
        }
      }
    }
  }
}


// ErrMessage classe are used for error reporting
// I used a new class here because error messages are
// added both in exprBranches and Scope methods
class ErrMessage {
  String message = "";
  
  public ErrMessage() {
    // default constructor
  }
  
  public ErrMessage(String errMess) {
    message = errMess;
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
  
  public boolean inTable(String id, String type, int scope, ArrayList<ErrMessage> er) {
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
      String e = "SEMANTIC ERROR: Variable " + id + " in scope " + scope + " already exists";
      er.add(new ErrMessage(e));
    }
    
    return exists;
  }
  
  public boolean typeMisCheck(String id, String type, int scope, ArrayList<Scope> table, int ogScope, ArrayList<ErrMessage> er) {
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
      err = table.get(prevScope).typeMisCheck(id, type, prevScope, table, ogScope, er);
    }
    else if (vars[col] == null && prevScope == -2) {
      String e = "SEMANTIC ERROR: Variable " + id + " used before declared";
      er.add(new ErrMessage(e));
      err = true;
    }
    else if (vars[col] != null) {
      if (!(vars[col].vType.equals(type))) {
        String e = "SEMANTIC ERROR: Type mismatch, var " + id + " of type " + vars[col].vType + " cannot be assigned a " + type;
        er.add(new ErrMessage(e));
        err = true;
      }
      else {
        vars[col].initialized = true;
        vars[col].varScope.add(ogScope);
      }
    }
    
    return err;
  }
  
  public String retType(String id, ArrayList<Scope> table) {
    String reType = "";
    char[] alphabet = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    
    int col = 0;
    char check = id.charAt(0);
    
    for (int u=0; u<alphabet.length; u++) {
      if (check == alphabet[u]) {
        col = u;
      }
    }
    
    if (vars[col] != null) {
      reType = vars[col].vType;
    }
    else if (vars[col] == null && prevScope != -2) {
      reType = table.get(prevScope).retType(id, table);
    }
    else {
      reType = "Error";
    }
    
    return reType;
  }
  
  public boolean doesExist(String id, int scope, ArrayList<Scope> table, int ogScope, ArrayList<ErrMessage> er) {
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
    
    if (vars[col] == null && prevScope != -2) {
      exists = table.get(prevScope).doesExist(id, prevScope, table, ogScope, er);
    }
    else if (vars[col] == null && prevScope != -2) {
      String e = "SEMANTIC ERROR: Variable " + id + " used before declared";
      er.add(new ErrMessage(e));
    }
    else if (vars[col] != null) {
      vars[col].inUse = true;
      initialized = table.get(ogScope).isInitialized(id, ogScope, table, er);
      exists = true;
    }
    
    return exists;
  }
  
  public boolean isInitialized(String id, int scope, ArrayList<Scope> table, ArrayList<ErrMessage> er) {
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
          String e = "SEMANTIC ERROR: Variable " + id + " in scope "+ scope + " is not initialized";
          er.add(new ErrMessage(e));
        }
      }
    }
    
    return inited;
  }
  
}
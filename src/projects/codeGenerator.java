import java.util.*;

public class codeGenerator {
  int currentScope = -1;
  int prevScope = -2;
  int prevBlock = -2;
  int scopeTotal = -1;
  
  // data structures
  String[] opCodes = new String[255]; // for storing the machine code
  ArrayList<staticVar> staticTable = new ArrayList<staticVar>();
  ArrayList<bList> blockTable = new ArrayList<bList>();
  ArrayList<stringLoc> words = new ArrayList<stringLoc>();
  
  // positional counters
  int pos = 0;
  int staticTemp = 0;
  
  // flags
  boolean override = false;
  boolean inIf = false;
  boolean ignoreNext = false;
  
  public codeGenerator() {
    for(int i=0; i<255; i++) {
      opCodes[i] = "00";
    }
  };
  
  public void generate(CSTNode node, int prog) {
    currentScope = node.sc0pe;
    
    if (node.token.equals("VarDecl")) {
      opCodes[pos] = "A9";
      pos++;
      
      opCodes[pos] = "00";
      pos++;
      
      opCodes[pos] = "8D";
      pos++;
      
      if (staticTemp <10) {
        String tname = "T" + staticTemp;
        staticVar temp;
        if (node.tree.get(0).token.equals("string")) {
          temp = new staticVar(tname, "XX", node.tree.get(1).token, node.tree.get(1).sc0pe, staticTemp, true);
        }
        else {
          temp = new staticVar(tname, "XX", node.tree.get(1).token, node.tree.get(1).sc0pe, staticTemp, false);
        }
        staticTable.add(temp);
        staticTemp++;
        
        opCodes[pos] = tname;
        pos++;
        
        opCodes[pos] = "XX";
        pos++;
      }
      else {
        String tname = Integer.toString(staticTemp);
        staticVar temp;
        if (node.tree.get(0).token.equals("string")) {
          temp = new staticVar("T0", tname, node.tree.get(1).token, node.tree.get(1).sc0pe, staticTemp, true);
        }
        else {
          temp = new staticVar("T0", tname, node.tree.get(1).token, node.tree.get(1).sc0pe, staticTemp, false);
        }
        staticTable.add(temp);
        staticTemp++;
        
        opCodes[pos] = "T0";
        pos++;
        
        opCodes[pos] = tname;
        pos++;
      }
      
      staticTemp++;
      
    }
    else if (node.token.equals("Block")) {
      prevScope = currentScope;
    }
    else if (node.token.equals("Assign")) {
      opCodes[pos] = "A9";
      pos++;
      
      if(!node.tree.get(0).token.equals("string")) {
        if (node.tree.get(1).token.equals("true") || node.tree.get(1).token.equals("false")) {
          if (node.tree.get(1).token.equals("true")) {
            opCodes[pos] = "01";
            pos++;
          }
          else {
            opCodes[pos] = "00";
            pos++;
          }
        }
      }
      else if (node.tree.get(1).token.equals("Digit")) {
        String num = "0" + node.tree.get(1).token;
        opCodes[pos] = num;
        pos++;
      }
      else if (node.tree.get(1).token.equals("Add")) {
        this.intSequence(node.tree.get(1), "", "", true);
        ignoreNext = true;
      }
      else if (node.tree.get(1).token.equals("string")) {
        System.out.println(node.tree.get(1).token);
        System.out.println(node.tree.get(1).type);
      }
    }
  }
  
  public void intSequence(CSTNode node, String tmp1, String tmp2, boolean first) {
    String temp1 = "";
    String temp2 = "";
    
    if (!node.tree.get(1).token.equals("Id")) {
      if (first) {
        if (override && !inIf) {
          opCodes[pos] = "A9";
          pos++;
        }
        String num = "0" + node.tree.get(0).token;
        opCodes[pos] = num;
        pos++;
        
        opCodes[pos] = "8D";
        pos++;
        
        if (staticTemp < 10) {
          String tname = "T" + staticTemp;
          staticVar temp;
          temp = new staticVar(tname, "XX", node.tree.get(1).token, node.tree.get(1).sc0pe, staticTemp, false);
          staticTable.add(temp);
          staticTemp++;
          
          opCodes[pos] = tname;
          temp1 = tname;
          pos++;
          
          opCodes[pos] = "XX";
          temp2 = "XX";
          pos++;
        }
        else {
          String tname = Integer.toString(staticTemp);
          staticVar temp;
          temp = new staticVar("T0", tname, node.tree.get(1).token, node.tree.get(1).sc0pe, staticTemp, false);
          staticTable.add(temp);
          staticTemp++;
          
          opCodes[pos] = "T0";
          temp1 = "T0";
          pos++;
          
          opCodes[pos] = tname;
          temp1 = tname;
          pos++;
        }
      }
      if (node.tree.get(1).token.equals("Add")) {
        opCodes[pos] = "A9";
        pos++;
        
        String num = "0" + node.tree.get(1).tree.get(0).token;
        opCodes[pos] = num;
        pos++;
        
        opCodes[pos] = "6D";
        pos++;
        
        opCodes[pos] = temp1;
        pos++;
        opCodes[pos] = temp2;
        pos++;
        
        opCodes[pos] = "8D";
        pos++;
        
        opCodes[pos] = temp1;
        pos++;
        opCodes[pos] = temp2;
        pos++;
        
        this.intSequence(node.tree.get(1), temp1, temp2, false);       
      }
      else {
        opCodes[pos] = "A9";
        pos++;
        
        String num = "0" + node.tree.get(1).token;
        opCodes[pos] = num;
        pos++;
        
        opCodes[pos] = "6D";
        pos++;
        
        if (!first) {
          opCodes[pos] = tmp1;
          pos++;
          opCodes[pos] = tmp2;
          pos++;
        }
        else {
          opCodes[pos] = temp1;
          pos++;
          opCodes[pos] = temp2;
          pos++;
        }
      }
    }
    else {
      if (!first) {
        opCodes[pos] = "AE";
        pos++;
        
        opCodes[pos] = tmp1;
        pos++;
        opCodes[pos] = tmp2;
        pos++;
      }
      else {
        String num = "0" + node.tree.get(0).token;
        opCodes[pos] = num;
        pos++;
      }
      
      opCodes[pos] = "6D";
      pos++;
      
      String temp001 = "";
      String temp002 = "";
      boolean found = false;
      boolean firstAlpha = false;
      staticVar neoVar = null;
      for (int c=0; c<staticTable.size(); c++) {
        if (staticTable.get(c).id != null) {
          if (staticTable.get(c).id.equals(node.tree.get(1).token)) {
            if (!firstAlpha) {
              neoVar = staticTable.get(c);
              firstAlpha = true;
            }
            if (staticTable.get(c).scope == currentScope) {
              found = true;
              temp001 = staticTable.get(c).tmp;
              temp002 = staticTable.get(c).tmp2;
              neoVar = staticTable.get(c);
            }
          }
        }
      }
      
      if (!found) {
        if (firstAlpha) {
          temp001 = neoVar.tmp;
          temp002 = neoVar.tmp2;
        }
      }
      
      opCodes[pos] = temp001;
      pos++;
      opCodes[pos] = temp002;
      pos++;
    }
  }
}

class staticVar {
  String tmp = "";
  String tmp2 = "";
  String id = "";
  int scope = 0;
  int offset = 0;
  boolean isStri = false;
  
  public staticVar() {
    // default constructor
  }
  
  public staticVar(String name, String name2, String ind, int sco, int off, boolean str) {
    tmp = name;
    tmp2 = name2;
    id = ind;
    scope = sco;
    offset = off;
    isStri = str;
  }
}

class stringLoc {
  String word = "";
  String loc = "";
  
  public stringLoc() {
    // default constructor
  }
  
  public stringLoc(String name) {
    word = name;
  }
}

class bList {
  int child = 0;
  int current = 0;
  int scope = 0;
  int prevBlock = -2;
  
  public bList() {
    // default constructor
  }
  
  public bList(int ch, int blockNum, int scoop) {
    child = ch;
    prevBlock = blockNum;
    scope = scoop;
  }
}
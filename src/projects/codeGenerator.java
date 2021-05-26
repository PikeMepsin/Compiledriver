import java.util.*;

public class codeGenerator {
  int currentScope = -1;
  int prevScope = -2;
  
  // data structures
  String[] opCodes = new String[255]; // for storing the machine code
  ArrayList<staticVar> staticTable = new ArrayList<staticVar>();
  
  // positional counters
  int pos = 0;
  int staticTemp = 0;
  
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
    }
  }
}

class staticVar {
  String tmp = "";
  String tmp2 = "";
  String indent = "";
  int scope = 0;
  int offset = 0;
  boolean isStri = false;
  
  public staticVar() {
    // default constructor
  }
  
  public staticVar(String name, String name2, String ind, int sco, int off, boolean str) {
    tmp = name;
    tmp2 = name2;
    indent = ind;
    scope = sco;
    offset = off;
    isStri = str;
  }
}
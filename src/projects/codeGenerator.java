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
  ArrayList<jumpVar> jumpTable = new ArrayList<jumpVar>();
  
  // positional counters
  int pos = 0;
  int staticTemp = 0;
  int stringPos = 254; //keeps track for reference variables
  
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
      //System.out.println(pos + " vardecl");
      
      opCodes[pos] = "00";
      pos++;
      //System.out.println(pos);
      
      opCodes[pos] = "8D";
      pos++;
      
      if (staticTemp <10) {
        String tname = "T" + staticTemp;
        staticVar temp;
        System.out.println(node.tree.get(0).type);
        if (node.tree.get(0).type.equals("String")) {
          System.out.println("String added");
          temp = new staticVar(tname, "XX", node.tree.get(1).token, node.tree.get(1).sc0pe, staticTemp, true);
        }
        else {
          temp = new staticVar(tname, "XX", node.tree.get(1).token, node.tree.get(1).sc0pe, staticTemp, false);
        }
        staticTable.add(temp);
        staticTemp++;
        
        opCodes[pos] = tname;
        pos++;
        //System.out.println(pos + " made it in");
        
        opCodes[pos] = "XX";
        pos++;
      }
      else {
        String tname = Integer.toString(staticTemp);
        staticVar temp;
        if (node.tree.get(0).type.equals("String")) {
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
    else if (node.token.equals("Print")) {
      if (node.tree.get(0).type.equals("ID")) {
        System.out.println(node.tree.get(0).token + " " + node.tree.get(0).type);
        opCodes[pos] = "AC";
        pos++;
        
        String temp1 = "";
        String temp2 = "";
        boolean found = false;
        boolean first = false;
        staticVar neoVar = null;
        for (int k=0; k<staticTable.size(); k++) {
          if (staticTable.get(k).id != null) {
            if (staticTable.get(k).id.equals(node.tree.get(0).token)) {
              if (!first) {
                neoVar = staticTable.get(k);
                first = true;
              }
              if (staticTable.get(k).scope == currentScope) {
                found = true;
                temp1 = staticTable.get(k).tmp;
                temp2 = staticTable.get(k).tmp2;
                neoVar = staticTable.get(k);
              }
            }
          }
        }
        
        if (!found) {
          if (first) {
            temp1 = neoVar.tmp;
            temp2 = neoVar.tmp2;
          }
        }
        
        opCodes[pos] = temp1;
        pos++;
        opCodes[pos] = temp2;
        pos++;
        
        opCodes[pos] = "A2";
        pos++;
        
        if (neoVar.isStri == false) {
          opCodes[pos] = "01";
          pos++;
        }
        else {
          opCodes[pos] = "02";
          pos++;
        }
      }
      
      else if (node.tree.get(0).type.equals("Int")) {
        opCodes[pos] = "A0";
        pos++;
        
        String num = "0" + node.tree.get(0).token;
        opCodes[pos] = num;
        pos++;
        
        opCodes[pos] = "A2";
        pos++;
        opCodes[pos] = "01";
        pos++;
      }
      else if (node.tree.get(0).token.equals("true")) {
        opCodes[pos] = "A0";
        pos++;
        
        opCodes[pos] = "01";
        pos++;
        
        opCodes[pos] = "A2";
        pos++;
        
        opCodes[pos] = "01";
        pos++;
      }
      else if (node.tree.get(0).token.equals("false")) {
        opCodes[pos] = "A0";
        pos++;
        
        opCodes[pos] = "00";
        pos++;
        
        opCodes[pos] = "A2";
        pos++;
        
        opCodes[pos] = "01";
        pos++;
      }
      else if (node.tree.get(0).type.equals("String")) {
        opCodes[pos] = "A0";
        pos++;
        
        String word = node.tree.get(0).token;
        boolean skip = false;
        
        for (int m=0; m<words.size(); m++) {
          if (words.get(m).word.equals(word)) {
            opCodes[pos] = words.get(m).loc;
            pos++;
            skip = true;
          }
        }
        
        if (!skip) {
          stringPos = stringPos - word.length();
          int tempor = stringPos;
          
          for (int n=0; n<word.length(); n++) {
            char tempo = word.charAt(n);
            opCodes[pos] = this.toHex((int) tempo);
            stringPos++;
          }
          
          opCodes[stringPos] = "00";
          opCodes[pos] = this.toHex(tempor);
          
          stringLoc stor = new stringLoc(word);
          stor.loc = this.toHex(tempor);
          words.add(stor);
          
          pos++;
          stringPos = tempor;
        }
        
        opCodes[pos] = "A2";
        pos++;
        opCodes[pos] = "02";
        pos++;
      }
      
      opCodes[pos] = "FF";
      pos++;
    }
    else if (node.token.equals("Assign")) {
      opCodes[pos] = "A9";
      pos++;
      
      System.out.println(node.tree.get(1).type);
      if(!node.tree.get(0).token.equals("String")) {
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
      else if (node.tree.get(1).type.equals("Int")) {
        String num = "0" + node.tree.get(1).token;
        System.out.println("int is a hit");
        opCodes[pos] = num;
        pos++;
      }
      else if (node.tree.get(1).token.equals("Add")) {
        this.intSequence(node.tree.get(1), "", "", true);
        ignoreNext = true;
      }
      else if (node.tree.get(1).type.equals("String")) {
        String word = node.tree.get(1).token;
        boolean skip = false;
        
        for (int d=0; d<words.size(); d++) {
          if (words.get(d).word.equals(word)) {
            opCodes[pos] = words.get(d).loc;
            pos++;
            skip = true;
          }
        }
        
        if (!skip) {
          stringPos = stringPos - word.length()-1;
          int tempor = stringPos;
          
          for (int d=0; d<word.length(); d++) {
            char tempo = word.charAt(d);
            opCodes[stringPos] = this.toHex((int) tempo);
            stringPos++;
          }
          
          opCodes[stringPos] = "00";
          opCodes[pos] = this.toHex(tempor);
          
          stringLoc stor = new stringLoc(word);
          stor.loc = this.toHex(tempor);
          words.add(stor);
          
          pos++;
          stringPos = tempor;
        }
        skip = false;
      }
      
      opCodes[pos] = "8D";
      pos++;
      
      int tempSco = -2;
      boolean found = false;
      String temp1 = "";
      String temp2 = "";
      
      for (int f=0; f<staticTable.size(); f++) {
        if (staticTable.get(f).id != null) {
          if (staticTable.get(f).id.equals(node.tree.get(0).token)) {
            if (!ignoreNext) {
              tempSco = staticTable.get(f).scope;
              if (tempSco == currentScope) {
                found = true;
                temp1 = staticTable.get(f).tmp;
                temp2 = staticTable.get(f).tmp2;
              }
            }
            else {
              found = true;
              temp1 = staticTable.get(f).tmp;
              temp2 = staticTable.get(f).tmp2;
            }
          }
        }
      }
      
      if (!found && tempSco != -2) {
        if (staticTemp < 10) {
          String tname = "T" + staticTemp;
          temp1 = tname;
          temp2 = "XX";
          staticVar temp;
          if (node.tree.get(1).type.equals("String")) {
            temp = new staticVar(temp1, temp2, node.tree.get(0).token, currentScope, staticTemp, true);
          }
          else {
            temp = new staticVar(temp1, temp2, node.tree.get(0).token, currentScope, staticTemp, false);
          }
          staticTable.add(temp);
          staticTemp++;
        }
        else {
          String tname = Integer.toString(staticTemp);
          temp1 = "T0";
          temp2 = tname;
          staticVar temp;
          if (node.tree.get(1).type.equals("String")) {
            temp = new staticVar(temp1, temp2, node.tree.get(0).token, currentScope, staticTemp, true);
          }
          else {
            temp = new staticVar(temp1, temp2, node.tree.get(0).token, currentScope, staticTemp, false);
          }
          staticTable.add(temp);
          staticTemp++;
        }
        
        staticTemp++;
        
        opCodes[pos] = temp1;
        pos++;
        opCodes[pos] = temp2;
        pos++;
      }
      else {
        opCodes[pos] = temp1;
        pos++;
        opCodes[pos] = temp2;
        pos++;
      }
    }
    
    if ((!ignoreNext || override) && node.tree.size() != 0) {
      for (int q=0; q<node.tree.size(); q++) {
        generate(node.tree.get(q), prog);
      }
      ignoreNext = false;
      override = false;
    }
  }
  
  public void printOps() {
    int format = 0;
    String line = "";
    
    System.out.println();
    for (int y=0; y<opCodes.length; y++) {
      if (format == 8) {
        System.out.println(line);
        format = 0;
        line = "";
      }
      line = line + opCodes[y] + " ";
      format++;
      if (y == 254) {
        System.out.println(line); 
      }
    }
  }
  
  public void backpatch() {
    opCodes[pos] = "00";
    pos++;
    opCodes[pos] = "00";
    pos++;
    
    for (int r=0; r<staticTable.size(); r++) {
      staticTable.get(r).actualC = this.toHex(pos);
      pos++;
    }
    
    for (int t=0; t<staticTable.size(); t++) {
      for (int u=0; u<opCodes.length; u++) {
        if (staticTable.get(t).tmp.equals(opCodes[u])) {
          if (staticTable.get(t).tmp2.equals(opCodes[u+1])) {
            opCodes[u] = staticTable.get(t).actualC;
            opCodes[u+1] = "00";
          }
        }
      }
    }
    
    for (int v=0; v<jumpTable.size(); v++) {
      jumpTable.get(v).replace = this.toHex(jumpTable.get(v).len);
    }
    
    for (int w=0; w<jumpTable.size(); w++) {
      for (int x=0; x<opCodes.length; x++) {
        if (jumpTable.get(w).tmp.equals(opCodes[x])) {
          opCodes[x] = jumpTable.get(w).replace;
        }
      }
    }
  }
  
  public void intSequence(CSTNode node, String tmp1, String tmp2, boolean first) {
    String temp1 = "";
    String temp2 = "";
    
    if (!node.tree.get(1).type.equals("Id")) {
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
  
  public String toHex(int c) {
    char hex[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    int rem = 0;
    String retHex = "";
    while (c > 0) {
      rem = c%16;
      retHex = hex[rem] + retHex;
      c /= 16;
    }
    
    if (retHex.length() == 1) {
      retHex = "0" + retHex;
    }
    return retHex;
  }
}

class staticVar {
  String tmp = "";
  String tmp2 = "";
  String id = "";
  int scope = 0;
  int offset = 0;
  boolean isStri = false;
  
  String actualC = "";
  
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

class jumpVar {
  String tmp = "";
  int len = 0;
  String replace = "";
  
  public jumpVar() {
    // default constructor
  }
  
  public jumpVar(String name) {
    tmp = name;
  }
  
  public void setLen(int l) {
    len = l;
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
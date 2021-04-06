import java.util.ArrayList;


public class CST {
  CSTNode root = null;
  CSTNode pointer = null;
  
  public CST() {
    // the default constructor
  }
  
  
  
  public void newBranch(String name, int lline, int ppos) {
    CSTNode node = new CSTNode(name, lline, ppos);
    if(this.root == null) {
      this.root = node;
    }
    else {
      node.parent = this.pointer;
      this.pointer.tree.add(node);
    }
    this.pointer = node;
  }
}


class CSTNode {
  // This is the important thing but...
  public String token = "";
  // I care about the position for error reporting purposes.
  public int lineNum = 0;
  public int position = 0;
  
  ArrayList<CSTNode> tree = new ArrayList<CSTNode>();
  
  CSTNode parent = null;
  
  public CSTNode(String tok, int line, int pos) {
    this.token = tok;
    this.lineNum = line;
    this.position = pos;
  }
}
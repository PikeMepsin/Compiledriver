import java.util.ArrayList;

class CSTNode {
  public String token = "";
  
  ArrayList<CSTNode> tree = new ArrayList<CSTNode>();
  
  CSTNode parent = null;
  
  public CSTNode() {
    // the default constructor
  }
  
  public CSTNode(String tok) {
    this.token = tok;
  }
}

public class CST {
  CSTNode root = null;
  CSTNode pointer = null;
  
  public CST() {
    // the default constructor
  }
  
  // adds a leaf node, this will be our terminal symbol
  public void sproutLeaf(String name) {
    CSTNode node = new CSTNode(name);
    node.parent = this.pointer;
    this.pointer.tree.add(node);
  }
  
  // adds a branch node, these act as productions and nonterminals
  public void growBranch(String name) {
    CSTNode node = new CSTNode(name);
    if(this.root == null) {
      this.root = node;
    }
    else {
      node.parent = this.pointer;
      this.pointer.tree.add(node);
    }
    this.pointer = node;
  }
  
  public void printCST(CSTNode node, int depth) {
    String formatTree = "";
    
    for (int x=0; x<depth; x++) {
      formatTree = formatTree + "-";
    }
    
    // handles branch nodes (nodes with children)
    if (node.tree.size() > 0) {
      formatTree += "<" + node.token + ">";
      System.out.println(formatTree);
      depth++;
      
      for (int y=0; y<node.tree.size(); y++) {
        printCST(node.tree.get(y), depth);
      }
    }
    // handles leaf nodes
    else {
      if (node.token != "StatementList") {
        formatTree += "[ " + node.token + " ]";
        System.out.println(formatTree);
      }
    }
  }
  
  // climb the tree, hence the recursion
  public void climb() {
    if (this.pointer.parent != null) {
      this.pointer = this.pointer.parent;
    }
  }
}

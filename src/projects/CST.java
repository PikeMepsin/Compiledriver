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
  
  // climb the tree, hence the recursion
  public void climb() {
    if (this.pointer.parent != null) {
      this.pointer = this.pointer.parent;
    }
  }
}

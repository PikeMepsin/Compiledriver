import java.util.ArrayList;

class ASTNode {
  public String token = "";
  public int lineN = 0;
  public int position = 0;
  
  ArrayList<ASTNode> tree = new ArrayList<ASTNode>();
  
  ASTNode parent = null;
  
  public ASTNode() {
    // default constructor
  }
  
  public ASTNode(String name, int line, int posi) {
    token = name;
    lineN = line;
    position = posi;
  }
}

public class AST {
  ASTNode root = null;
  ASTNode pointer = null;
  
  public AST() {
    // default constructor
  }
  
  // adds a production node
  public void growBranch(String token) {
    ASTNode node = new ASTNode(token, 0, 0);
    if (this.root == null) {
      this.root = node;
    }
    else {
      node.parent = this.pointer;
      this.pointer.tree.add(node);
    }
    this.pointer = node;
  }
  
  // adds a terminal symbol leaf node
  public void sproutLeaf(String token, int l, int p) {
    ASTNode node = new ASTNode(token, l, p);
    if (this.root == null) {
      this.root = node;
    }
    else {
      node.parent = this.pointer;
      this.pointer.tree.add(node);
    }
  }
  
  public void printAST(ASTNode node, int depth) {
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
        printAST(node.tree.get(y), depth);
      }
    }
    // handles leaf nodes
    else {
      formatTree += "[ " + node.token + " ]";
      System.out.println(formatTree);
    }
  }
  
  // climb the tree, the strategy for the AST is also recursive
  public void climb() {
    if (this.pointer.parent != null) {
      this.pointer = this.pointer.parent;
    }
  }
}
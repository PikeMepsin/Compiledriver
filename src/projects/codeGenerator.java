import java.util.*;

public class codeGenerator {
  int currentScope = -1;
  int prevScope = -2;
  String[] opCodes = new String[255]; // for storing the machine code
  int currentPos = 0;
  
  public codeGenerator() {
    for(int i=0; i<255; i++) {
      opCodes[i] = "00";
    }
  };
  
  public void generate(CSTNode node, int prog) {
    currentScope = node.sc0pe;
    System.out.println(currentScope);
  }
}
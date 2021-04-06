import java.util.ArrayList;

public class parser {
  
  int errors = 0;
  ArrayList<Token> stream = new ArrayList<Token>();
  CST tree = new CST();
  
  public parser() {
    // default constructor
  }
  
  public parser(ArrayList<Token> lexOutput) {
    stream = lexOutput;
    
    parseProgram(stream)
  }
}
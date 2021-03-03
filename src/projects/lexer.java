/* This is the beginning of the lexer
 * Compilers CMPT 432
 * @author Mike Pepsin
 */

import java.util.Scanner;

public class lexer {
  public static void main(String[] args) {
    Scanner lex = new Scanner(System.in);
    String code = "";
    while (lex.hasNextLine()) {
      while (lex.hasNext()) {
        code = lex.next();
        System.out.println(code);
      }
    }
  }
}
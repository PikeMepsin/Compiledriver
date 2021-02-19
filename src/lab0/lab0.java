import java.util.Scanner;

/* Lab 0
 * @author Mike Pepsin
 * Professor Alan Labouseur
 * CMPT432 Compilers
 */ 
public class lab0 {

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    String line = "";

    while (input.hasNext()) {
      line = input.nextLine();
      System.out.println(line);
    }
  }
}
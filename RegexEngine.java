import java.util.Scanner;

public class RegexEngine {
  private boolean verbose;

  public RegexEngine(boolean verbose) {
    this.verbose = verbose;
  }

  public static void main(String[] args) {
    boolean verbose = false;

    // Check for verbose flag
    if (args.length > 0 && args[0].equals("-v")) {
      verbose = true;
    }

    RegexEngine engine = new RegexEngine(verbose);
    engine.run();
  }

  public void run() {
    Scanner scanner = new Scanner(System.in);

    try {
      // get first input as regex
      String regex = scanner.nextLine().trim();

      if (regex.isEmpty()) {
        System.err.println("Error: Regex is empty");
        System.exit(1);
      }

      // construct e-nfa 

      System.out.println("Ready");

      while (scanner.hasNextLine()) {
        String input = scanner.nextLine();

        // two modes to process
        // verbose
        // normal

        System.out.println("Read " + input);
      }
    } finally {
      scanner.close();
    }
  }
}

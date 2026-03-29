import java.util.Scanner;

public class RegexEngine {
  private boolean verbose;
  private E_NFAStructure nfa;
  private NFASimulator sim;

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
      try {
        RegexParser parser = new RegexParser(regex);
        String validatedRegex = parser.validate();

        RegexToNFA nfaBuilder = new RegexToNFA();
        nfa = nfaBuilder.build(validatedRegex);
      } catch (IllegalArgumentException e) {
        System.err.println(e.getMessage());
        System.exit(1);
      }

      sim = new NFASimulator(nfa);

      if (verbose) {
        TransitionTablePrinter printer = new TransitionTablePrinter(nfa);
        printer.print();
      }

      System.out.println("ready");

      while (scanner.hasNextLine()) {
        String input = scanner.nextLine();

        // two modes to process
        if (verbose) {
          // verbose
          sim.matchesVerbose(input);
          System.out.println();
        } else {
          // normal
          System.out.println(sim.matches(input));
        }
      }
    } finally {
      scanner.close();
    }
  }
}

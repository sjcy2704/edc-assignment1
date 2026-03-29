import java.util.*;

/**
 * Prints a transition table for an eNFA in verbose mode.
 */
public class TransitionTablePrinter {
  private final E_NFAStructure nfa;

  /**
   * Creates a printer for the given ε-NFA.
   *
   * @param nfa The eNFA structure to print
   */
  public TransitionTablePrinter(E_NFAStructure nfa) {
    this.nfa = nfa;
  }

  /**
   * Prints the transition table to standard output.
   */
  public void print() {
    // Get all states and alphabet
    Set<State> allStates = nfa.getAllStates();
    Set<Character> alphabet = nfa.getAlphabet();

    // Build a sorted list of states for consistent output
    List<State> states = new ArrayList<>(allStates);
    states.sort(Comparator.comparingInt(State::getId));

    // Build sorted list of alphabet symbols
    List<Character> symbols = new ArrayList<>(alphabet);
    Collections.sort(symbols);

    // Print header row
    System.out.print("    epsilon");
    for (Character symbol : symbols) {
      System.out.print(" " + symbol);
    }
    System.out.println("   other");

    // Print each state row
    for (State state : states) {
      printStateRow(state, symbols);
    }

    System.out.println();  // Blank line after table
  }

  /**
   * Prints a single row of the transition table for a state.
   *
   * @param state The state to print
   * @param symbols The sorted list of alphabet symbols
   */
  private void printStateRow(State state, List<Character> symbols) {
    // Print state marker and name
    String marker = "";
    if (state.equals(nfa.getStartState())) {
      marker = ">";
    }
    if (state.isAccepting()) {
      marker += "*";
    }
    System.out.print(String.format("%-2s%s", marker, state.getName()));

    // Print epsilon transitions
    System.out.print(" " + getTargetStates(state, null));

    // Print transitions for each symbol in alphabet
    for (Character symbol : symbols) {
      System.out.print(" " + getTargetStates(state, symbol));
    }

    // Print "other" column (empty for our implementation)
    System.out.println("    ");
  }

  /**
   * Gets the target states for a given state and symbol.
   *
   * @param state The source state
   * @param symbol The transition symbol (null for epsilon)
   * @return list of target state names
   */
  private String getTargetStates(State state, Character symbol) {
    List<String> targets = new ArrayList<>();

    for (Transition t : state.getTransitions()) {
      // Check if transition matches the symbol
      boolean matches = false;
      if (symbol == null && t.isEpsilon()) {
        matches = true;
      } else if (symbol != null && !t.isEpsilon() && t.getSymbol().equals(symbol)) {
        matches = true;
      }

      if (matches) {
        targets.add(t.getTo().getName());
      }
    }

    if (targets.isEmpty()) {
      return "       ";  // Spacing for empty cell
    }

    // Sort targets for consistent output
    Collections.sort(targets);

    // Join with commas
    String result = String.join(",", targets);

    // Pad to minimum width
    return String.format("%-7s", result);
  }
}


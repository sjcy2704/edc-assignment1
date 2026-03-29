import java.util.Set;

/**
 * Simulates an eNFA to determine if the input strings match the regular expression.
 *
 * The simulator tracks the set of current states and processes input
 * character-by-character, using epsilon closures before and after each transition.
 */
public class NFASimulator {
  private final E_NFAStructure nfa;
  private final E_ClosureCalc eClosureCalc;
  private final TransitionFunction transitionFunction;

  /**
   * Creates a simulator for the given eNFA.
   *
   * @param nfa The eNFA structure to simulate
   */
  public NFASimulator(E_NFAStructure nfa) {
    this.nfa = nfa;
    this.eClosureCalc = new E_ClosureCalc();
    this.transitionFunction = new TransitionFunction(eClosureCalc);
  }

  /**
   * Tests if the given string matches the regex (normal mode).
   *
   * @param input The input string to test
   * @return true if the string is accepted, false otherwise
   */
  public boolean matches(String input) {
    // Start with epsilon closure of start state
    Set<State> currentStates = eClosureCalc.compute(nfa.getStartState());

    // Process each character
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      currentStates = transitionFunction.compute(currentStates, c);

      // If no states remain, the string is rejected
      if (currentStates.isEmpty()) {
        return false;
      }
    }

    // Check if any current state is an accepting state
    return isAccepting(currentStates);
  }

  /**
   * Tests if the given string matches the regex (verbose mode).
   *
   * Prints "true" or "false" after processing each character based on
   * whether the current state set contains an accepting state.
   *
   * @param input The input string to test
   * @return true if the string is accepted, false otherwise
   */
  public boolean matchesVerbose(String input) {
    // Start with epsilon closure of start state
    Set<State> currentStates = eClosureCalc.compute(nfa.getStartState());

    // Print initial state (before processing any characters)
    System.out.println(isAccepting(currentStates));

    // Process each character
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);

      System.out.println(c);
      currentStates = transitionFunction.compute(currentStates, c);

      // Print acceptance status after each character
      System.out.println(isAccepting(currentStates));

      // If no states remain, the string is rejected
      if (currentStates.isEmpty()) {
        return false;
      }
    }

    // Return final acceptance status
    return isAccepting(currentStates);
  }

  /**
   * Checks if any state in the set is an accepting state.
   *
   * @param states The set of states to check
   * @return true if at least one state is accepting
   */
  private boolean isAccepting(Set<State> states) {
    for (State state : states) {
      if (state.isAccepting()) {
        return true;
      }
    }
    return false;
  }
}


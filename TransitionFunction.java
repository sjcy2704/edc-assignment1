import java.util.*;

/**
 * Extended transition function for eNFA based on Chapter 3. The transition
 * function returns all the states that are reachable from q by taking the
 * epsilon transitions from q, taking a smybol transition, and taking the
 * epsilon transitions from the result.
 */
public class TransitionFunction {
  private final E_ClosureCalc eClosureCalc;

  /**
   * @param eClosureCalc Calculator for epsilon closures
   */
  public TransitionFunction(E_ClosureCalc eClosureCalc) {
    this.eClosureCalc = eClosureCalc;
  }

  /**
   * @param state  The starting state
   * @param symbol The input symbol
   * @return Set of all states reachable via symbol transition including epsilon closures
   */
  public Set<State> compute(State state, char symbol) {
    Set<State> startStates = eClosureCalc.compute(state);
    return compute(startStates, symbol);
  }

  /**
   * @param states Set of starting states
   * @param symbol The input symbol
   * @return Set of all states reachable via symbol transition
   */
  public Set<State> compute(Set<State> states, char symbol) {
    Set<State> targets = new HashSet<>();

    // For each state in the starting set
    for (State state : states) {
      // Follow all transitions labeled with 'symbol'
      for (Transition t : state.getTransitions()) {
        if (!t.isEpsilon() && t.getSymbol() == symbol) {
          targets.add(t.getTo());
        }
      }
    }

    // Compute epsilon closure of all target states
    return eClosureCalc.compute(targets);
  }
}

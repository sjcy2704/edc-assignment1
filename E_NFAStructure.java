import java.util.*;

/**
 * E_NFAStructure class represents the complete structure of an εNFA
 * defined in the chapter 3, slightly different from an NFA. It is defined by a
 * 5-tuple (Q, Σ, δ, q0, F) where:
 * - Q = finite set of the states
 * - Σ = alphabet which is extracted from the transitions
 * - δ = transition function which is stored in the state objects (slightly
 * changed from an NFA, as stated in Chapter 3, it accepts a state in Q, and
 * either an imput symbol or ε, and then return the a set of states)
 * - q0 = start state
 * - F = the set of accepting states
 */
public class E_NFAStructure {
  private final State startState;
  private final State acceptState;
  private final Set<State> allStates;
  private final Set<Character> alphabet;

  /**
   * @param startState  The initial state
   * @param acceptState The single accepting state
   */
  public E_NFAStructure(State startState, State acceptState) {
    this.startState = startState;
    this.acceptState = acceptState;
    this.allStates = extractStates();
    this.alphabet = extractAlphabet();
  }

  /**
   * Collects all states connected using BFS traversal algorithm.
   * (Used Claude to help me with the implementation of BFS traversal)
   *
   * @return Set of all states that can be reached from start state (Q)
   */
  private Set<State> extractStates() {
    Set<State> states = new HashSet<>();
    Queue<State> queue = new LinkedList<>();
    queue.add(startState);

    while (!queue.isEmpty()) {
      State current = queue.poll();
      if (states.add(current)) {
        for (Transition t : current.getTransitions()) {
          queue.add(t.getTo());
        }
      }
    }

    return states;
  }

  /**
   * Get all the input symbols from non-epsilon transitions to get the alphabet
   * (Σ) by looping through every transitions in all states. Based on chapter 3, ε
   * is NOT part of the alphabet.
   *
   * @return Set of all symbols used in transitions (excluding epsilon)
   */
  private Set<Character> extractAlphabet() {
    Set<Character> symbols = new HashSet<>();
    for (State state : allStates) {
      for (Transition t : state.getTransitions()) {
        if (!t.isEpsilon()) {
          symbols.add(t.getSymbol());
        }
      }
    }
    return symbols;
  }

  public State getStartState() {
    return startState;
  }

  public State getAcceptState() {
    return acceptState;
  }

  public Set<State> getAllStates() {
    return Collections.unmodifiableSet(allStates);
  }

  public Set<Character> getAlphabet() {
    return Collections.unmodifiableSet(alphabet);
  }
}

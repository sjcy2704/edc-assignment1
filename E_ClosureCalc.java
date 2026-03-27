import java.util.*;

/**
 * Helper class that only serves as the epsilon closure calculator. It computes
 * the epsilon closure for the NFA states based on Chapter 3. It is defined as
 * the set of all states that are reachable from the starting state via epsilon
 * transitions.
 */
public class E_ClosureCalc {
  /**
   * Compute the epsilon closure on a single state.
   *
   * @param state The state to compute epsilon closure
   * @return Set of all states reachable via epsilon transitions
   */
  public Set<State> compute(State state) {
    Set<State> closure = new HashSet<>();
    Queue<State> queue = new LinkedList<>();

    queue.add(state);
    closure.add(state);

    // BFS through epsilon transitions
    while (!queue.isEmpty()) {
      State current = queue.poll();

      // Explore all epsilon transitions from current state
      for (Transition t : current.getTransitions()) {
        if (t.isEpsilon()) {
          State next = t.getTo();
          // Only add state to queue if not already visited to prevent infinite loops
          if (closure.add(next)) {
            queue.add(next);
          }
        }
      }
    }

    return closure;
  }

  /**
   * Computes epsilon closure of a set of states.
   *
   * @param states Set of states to compute epsilon closure
   * @return Set of epsilon closures of all input states
   */
  public Set<State> compute(Set<State> states) {
    Set<State> closure = new HashSet<>();
    for (State state : states) {
      closure.addAll(compute(state));
    }
    return closure;
  }
}

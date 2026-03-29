import java.util.ArrayList;
import java.util.List;

/**
 * NFAFragment is the building block of the epsilon-NFA. It is implemented based
 * on Chapter 4.3 for building NFAs from regular expressions. Each fragment
 * consists of a start state and a list of transitions that are not pointing to
 * any states, dangling transitions. Then fragments are combined together using
 * epsilon transitions to build the final epsilon-NFA.
 */
public class NFAFragment {
  private final State start;
  private final List<Transition> danglingTransitions;

  /**
   * Creates an NFA fragment with a start state and an empty list of dangling
   * transitions
   *
   * @param start The start state of this fragment
   */
  public NFAFragment(State start) {
    this.start = start;
    this.danglingTransitions = new ArrayList<>();
  }

  public State getStart() {
    return start;
  }

  public void addDanglingTransition(Transition transition) {
    danglingTransitions.add(transition);
  }

  /**
   * Connects all dangling transitions to the specified state.
   * This is used when completing a fragment by adding an accept state.
   *
   * @param target The state to connect all dangling transitions to
   */
  public void connectDanglingTo(State target) {
    for (Transition t : danglingTransitions) {
      State from = t.getFrom();
      from.addTransition(new Transition(from, target, t.getSymbol()));
    }
  }

  public List<Transition> getDanglingTransitions() {
    return new ArrayList<>(danglingTransitions);
  }
}

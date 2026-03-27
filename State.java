import java.util.ArrayList;
import java.util.List;

/**
 * Represents a state/node in the NFA graph. The implementation comes from the
 * concepts in the Chapters 1 to 3. Each state keeps track of their id, can have
 * multiple transitions per symbol (based on Chapter 2), and states can have
 * epsilon transitions meaning that it lets it transition from one state to
 * another without consuming any symbols (based on Chapter 3 which is
 * implemented in the Transition class).
 */
public class State {
  private static int nextId = 0;
  private int id;
  private boolean isAccepting;
  private List<Transition> transitions;

  /**
   * This creates a node/state for the NFA. Each node will have their own
   * transition list.
   *
   * @param isAccepting flag to tell whether the state is an accepting state
   */

  public State(boolean isAccepting) {
    this.id = nextId++;
    this.isAccepting = isAccepting;
    this.transitions = new ArrayList<Transition>();
  }

  public int getId() {
    return id;
  }

  public boolean isAccepting() {
    return isAccepting;
  }

  public String getName() {
    return "q" + id;
  }

  public static void resetIdCounter() {
    nextId = 0;
  }

  public void setAccepting(boolean accepting) {
    this.isAccepting = accepting;
  }

  public void addTransition(Transition t) {
    transitions.add(t);
  }

  public List<Transition> getTransitions() {
    return transitions;
  }
}

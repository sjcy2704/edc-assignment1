/**
 * The transition class represents the transition between states in the εNFA. It
 * implements the transition function δ from the Chapter 2 and Chapter 3.
 *
 * Symbol is null for epsilon transitions where ε represents the empty string.
 */
public class Transition {
  private State from;
  private State to;
  private Character symbol; // null = epsilon transition

  /**
   * Creates a transition from one state to another.
   *
   * @param from   Source state
   * @param to     Target state
   * @param symbol Input symbol (null for epsilon transitions)
   */
  public Transition(State from, State to, Character symbol) {
    this.from = from;
    this.to = to;
    this.symbol = symbol;
  }

  public State getFrom() {
    return from;
  }

  public State getTo() {
    return to;
  }

  public Character getSymbol() {
    return symbol;
  }

  /**
   * Checks if this is an epsilon transition.
   *
   * @return true if this transition is an epsilon transition meaning that the
   *         symbol is null (empty string)
   */
  public boolean isEpsilon() {
    return symbol == null;
  }
}

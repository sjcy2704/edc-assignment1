import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 * Tests produced by Claude
 */
public class TransitionFunction_Test {

  /**
   * Test delta function with simple transition: q0 --a--> q1
   * Expected: δ(q0, 'a') = {q1}
   */
  @Test
  public void testSimpleTransition() {
    State s0 = new State(false);
    State s1 = new State(true);
    s0.addTransition(new Transition(s0, s1, 'a'));

    E_ClosureCalc epsilonCalc = new E_ClosureCalc();
    TransitionFunction delta = new TransitionFunction(epsilonCalc);

    Set<State> result = delta.compute(s0, 'a');

    assertEquals(1, result.size());
    assertTrue(result.contains(s1));
  }

  /**
   * Test delta with epsilon closure:
   * q0 --ε--> q1 --a--> q2 --ε--> q3
   * Expected: δ(q0, 'a') = {q2, q3}
   * (includes epsilon closure before and after 'a' transition)
   */
  @Test
  public void testTransitionWithEpsilonClosure() {
    State s0 = new State(false);
    State s1 = new State(false);
    State s2 = new State(false);
    State s3 = new State(true);

    s0.addTransition(new Transition(s0, s1, null)); // ε to s1
    s1.addTransition(new Transition(s1, s2, 'a')); // 'a' to s2
    s2.addTransition(new Transition(s2, s3, null)); // ε to s3

    E_ClosureCalc epsilonCalc = new E_ClosureCalc();
    TransitionFunction delta = new TransitionFunction(epsilonCalc);

    Set<State> result = delta.compute(s0, 'a');

    assertEquals(2, result.size());
    assertTrue(result.contains(s2));
    assertTrue(result.contains(s3)); // via epsilon closure
  }

  /**
   * Test delta with no matching transition.
   * Expected: δ(q0, 'b') = ∅ (empty set)
   */
  @Test
  public void testNoMatchingTransition() {
    State s0 = new State(false);
    State s1 = new State(true);
    s0.addTransition(new Transition(s0, s1, 'a'));

    E_ClosureCalc epsilonCalc = new E_ClosureCalc();
    TransitionFunction delta = new TransitionFunction(epsilonCalc);

    Set<State> result = delta.compute(s0, 'b');

    assertTrue(result.isEmpty());
  }

  /**
   * Test delta with multiple transitions on same symbol:
   * q0 --a--> q1
   * q0 --a--> q2
   * Expected: δ(q0, 'a') = {q1, q2} (nondeterminism)
   */
  @Test
  public void testNondeterministicTransitions() {
    State s0 = new State(false);
    State s1 = new State(false);
    State s2 = new State(true);

    s0.addTransition(new Transition(s0, s1, 'a'));
    s0.addTransition(new Transition(s0, s2, 'a'));

    E_ClosureCalc epsilonCalc = new E_ClosureCalc();
    TransitionFunction delta = new TransitionFunction(epsilonCalc);

    Set<State> result = delta.compute(s0, 'a');

    assertEquals(2, result.size());
    assertTrue(result.contains(s1));
    assertTrue(result.contains(s2));
  }
}

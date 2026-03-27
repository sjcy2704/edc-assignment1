import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests produced by Claude
 */
public class State_Test {

  @Test
  public void testStateCreation() {
    State s = new State(false);
    assertNotNull(s);
    assertFalse(s.isAccepting());
  }

  @Test
  public void testAcceptingState() {
    State s = new State(true);
    assertTrue(s.isAccepting());
  }

  @Test
  public void testStateIdAutoIncrement() {
    State.resetIdCounter();
    State s1 = new State(false);
    State s2 = new State(false);
    assertEquals(0, s1.getId());
    assertEquals(1, s2.getId());
  }

  @Test
  public void testStateName() {
    State.resetIdCounter();
    State s = new State(false);
    assertEquals("q0", s.getName());
  }

  @Test
  public void testAddTransition() {
    State s1 = new State(false);
    State s2 = new State(true);
    Transition t = new Transition(s1, s2, 'a');

    s1.addTransition(t);

    assertEquals(1, s1.getTransitions().size());
    assertEquals(t, s1.getTransitions().get(0));
  }

  @Test
  public void testMultipleTransitions() {
    State s1 = new State(false);
    State s2 = new State(true);
    State s3 = new State(false);

    s1.addTransition(new Transition(s1, s2, 'a'));
    s1.addTransition(new Transition(s1, s3, 'b'));

    assertEquals(2, s1.getTransitions().size());
  }
}

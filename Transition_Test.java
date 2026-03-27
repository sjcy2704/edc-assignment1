import org.junit.*;
import static org.junit.Assert.*;

public class Transition_Test {

  @Before
  public void setUp() {
    State.resetIdCounter();
  }

  @Test
  public void testRegularTransition() {
    State s1 = new State(false);
    State s2 = new State(true);
    Transition t = new Transition(s1, s2, 'a');

    assertEquals(s1, t.getFrom());
    assertEquals(s2, t.getTo());
    assertEquals(Character.valueOf('a'), t.getSymbol());
    assertFalse(t.isEpsilon());
  }

  @Test
  public void testEpsilonTransition() {
    State s1 = new State(false);
    State s2 = new State(true);
    Transition t = new Transition(s1, s2, null);

    assertTrue(t.isEpsilon());
    assertNull(t.getSymbol());
  }
}

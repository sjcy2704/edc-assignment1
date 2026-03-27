import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests produced by Claude
 */
public class E_NFAStructure_Test {
  @Before
  public void setUp() {
    State.resetIdCounter();
  }

  @Test
  public void testSimpleNFA() {
    State start = new State(false);
    State accept = new State(true);
    start.addTransition(new Transition(start, accept, 'a'));

    E_NFAStructure nfa = new E_NFAStructure(start, accept);

    assertEquals(start, nfa.getStartState());
    assertEquals(accept, nfa.getAcceptState());
  }

  @Test
  public void testStateCollection() {
    State s0 = new State(false);
    State s1 = new State(false);
    State s2 = new State(true);

    s0.addTransition(new Transition(s0, s1, 'a'));
    s1.addTransition(new Transition(s1, s2, 'b'));

    E_NFAStructure nfa = new E_NFAStructure(s0, s2);

    assertEquals(3, nfa.getAllStates().size());
    assertTrue(nfa.getAllStates().contains(s0));
    assertTrue(nfa.getAllStates().contains(s1));
    assertTrue(nfa.getAllStates().contains(s2));
  }

  @Test
  public void testAlphabetCollection() {
    State s0 = new State(false);
    State s1 = new State(false);
    State s2 = new State(true);

    s0.addTransition(new Transition(s0, s1, 'a'));
    s1.addTransition(new Transition(s1, s2, 'b'));
    s0.addTransition(new Transition(s0, s2, null)); // epsilon

    E_NFAStructure nfa = new E_NFAStructure(s0, s2);

    assertEquals(2, nfa.getAlphabet().size());
    assertTrue(nfa.getAlphabet().contains('a'));
    assertTrue(nfa.getAlphabet().contains('b'));
    assertFalse(nfa.getAlphabet().contains(null));
  }

  @Test
  public void testUnmodifiableSets() {
    State s0 = new State(false);
    State s1 = new State(true);
    s0.addTransition(new Transition(s0, s1, 'a'));

    E_NFAStructure nfa = new E_NFAStructure(s0, s1);

    try {
      nfa.getAllStates().add(new State(false));
      fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected - sets should be unmodifiable
    }
  }
}

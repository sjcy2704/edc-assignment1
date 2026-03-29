import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for RegexToNFA class.
 */
public class RegexToNFA_Test {

  @Before
  public void setUp() {
    State.resetIdCounter();
  }

  @Test
  public void testSingleChar() {
    RegexToNFA converter = new RegexToNFA();
    E_NFAStructure nfa = converter.build("a");

    assertNotNull(nfa);
    assertNotNull(nfa.getStartState());
    assertNotNull(nfa.getAcceptState());
    assertTrue(nfa.getAcceptState().isAccepting());
  }

  @Test
  public void testAlternation() {
    RegexToNFA converter = new RegexToNFA();
    E_NFAStructure nfa = converter.build("a|b");

    assertNotNull(nfa);
    // Start state should have epsilon transitions to both 'a' and 'b' paths
    assertTrue(nfa.getStartState().getTransitions().size() >= 2);
  }

  @Test
  public void testStar() {
    RegexToNFA converter = new RegexToNFA();
    E_NFAStructure nfa = converter.build("a*");

    assertNotNull(nfa);
    // Should have states and transitions for looping
    assertTrue(nfa.getAllStates().size() > 2);
  }

  @Test
  public void testPlus() {
    RegexToNFA converter = new RegexToNFA();
    E_NFAStructure nfa = converter.build("a+");

    assertNotNull(nfa);
    assertTrue(nfa.getAllStates().size() > 2);
  }

  @Test
  public void testComplexRegex1() {
    RegexToNFA converter = new RegexToNFA();
    E_NFAStructure nfa = converter.build("(ab)*|c+");

    assertNotNull(nfa);
    assertNotNull(nfa.getStartState());
    assertNotNull(nfa.getAcceptState());
  }

  @Test
  public void testSequence() {
    RegexToNFA converter = new RegexToNFA();
    E_NFAStructure nfa = converter.build("abc");

    assertNotNull(nfa);
    assertTrue(nfa.getAllStates().size() >= 4); // At least start, accept, and intermediate states
  }
}

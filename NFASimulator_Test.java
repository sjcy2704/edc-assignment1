import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests produced by Claude
 */
public class NFASimulator_Test {

  @Before
  public void setUp() {
    State.resetIdCounter();
  }

  private E_NFAStructure buildNFA(String regex) {
    RegexToNFA converter = new RegexToNFA();
    return converter.build(regex);
  }

  @Test
  public void testSingleChar() {
    E_NFAStructure nfa = buildNFA("a");
    NFASimulator sim = new NFASimulator(nfa);

    assertTrue(sim.matches("a"));
    assertFalse(sim.matches("b"));
    assertFalse(sim.matches("aa"));
    assertFalse(sim.matches(""));
  }

  @Test
  public void testSequence() {
    E_NFAStructure nfa = buildNFA("abc");
    NFASimulator sim = new NFASimulator(nfa);

    assertTrue(sim.matches("abc"));
    assertFalse(sim.matches("ab"));
    assertFalse(sim.matches("abcd"));
    assertFalse(sim.matches("xyz"));
  }

  @Test
  public void testAlternation() {
    E_NFAStructure nfa = buildNFA("a|b");
    NFASimulator sim = new NFASimulator(nfa);

    assertTrue(sim.matches("a"));
    assertTrue(sim.matches("b"));
    assertFalse(sim.matches("c"));
    assertFalse(sim.matches("ab"));
  }

  @Test
  public void testStar() {
    E_NFAStructure nfa = buildNFA("a*");
    NFASimulator sim = new NFASimulator(nfa);

    assertTrue(sim.matches(""));      // Zero repetitions
    assertTrue(sim.matches("a"));     // One repetition
    assertTrue(sim.matches("aa"));    // Two repetitions
    assertTrue(sim.matches("aaaa"));  // Multiple repetitions
    assertFalse(sim.matches("b"));
    assertFalse(sim.matches("ab"));
  }

  @Test
  public void testPlus() {
    E_NFAStructure nfa = buildNFA("a+");
    NFASimulator sim = new NFASimulator(nfa);

    assertFalse(sim.matches(""));     // Zero repetitions - should fail
    assertTrue(sim.matches("a"));     // One repetition
    assertTrue(sim.matches("aa"));    // Two repetitions
    assertTrue(sim.matches("aaaa"));  // Multiple repetitions
    assertFalse(sim.matches("b"));
  }

  @Test
  public void testComplexRegex1() {
    // (ab)*|c+
    E_NFAStructure nfa = buildNFA("(ab)*|c+");
    NFASimulator sim = new NFASimulator(nfa);

    // (ab)* part
    assertTrue(sim.matches(""));      // Empty matches (ab)*
    assertTrue(sim.matches("ab"));
    assertTrue(sim.matches("abab"));
    assertFalse(sim.matches("abc"));  // Doesn't match exactly

    // c+ part
    assertTrue(sim.matches("c"));
    assertTrue(sim.matches("ccc"));

    // Other
    assertFalse(sim.matches("a"));
    assertFalse(sim.matches("b"));
  }

  @Test
  public void testComplexRegex2() {
    // 01*|10
    E_NFAStructure nfa = buildNFA("01*|10");
    NFASimulator sim = new NFASimulator(nfa);

    assertTrue(sim.matches("0"));     // 01* with zero 1s
    assertTrue(sim.matches("01"));    // 01* with one 1
    assertTrue(sim.matches("0111"));  // 01* with multiple 1s
    assertTrue(sim.matches("10"));    // 10 alternative

    assertFalse(sim.matches("1"));
    assertFalse(sim.matches("00"));
    assertFalse(sim.matches("011001"));
  }

  @Test
  public void testWithSpaces() {
    E_NFAStructure nfa = buildNFA("a b");
    NFASimulator sim = new NFASimulator(nfa);

    assertTrue(sim.matches("a b"));
    assertFalse(sim.matches("ab"));
    assertFalse(sim.matches("a  b"));
  }
}


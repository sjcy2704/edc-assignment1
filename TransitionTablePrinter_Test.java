import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;

/**
 * Tests produced by Claude
 */
public class TransitionTablePrinter_Test {

  @Before
  public void setUp() {
    State.resetIdCounter();
  }

  private E_NFAStructure buildNFA(String regex) {
    RegexToNFA converter = new RegexToNFA();
    return converter.build(regex);
  }

  @Test
  public void testPrintSimple() {
    E_NFAStructure nfa = buildNFA("a");
    TransitionTablePrinter printer = new TransitionTablePrinter(nfa);

    // Capture output
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    printer.print();

    // Restore original System.out
    System.setOut(originalOut);

    String output = outContent.toString();

    // Check that output contains expected elements
    assertTrue(output.contains("epsilon"));
    assertTrue(output.contains("a"));
    assertTrue(output.contains(">q0"));  // Start state
    assertTrue(output.contains("*"));     // Accept state marker
  }

  @Test
  public void testPrintComplex() {
    E_NFAStructure nfa = buildNFA("(ab)*|c+");
    TransitionTablePrinter printer = new TransitionTablePrinter(nfa);

    // Just verify it doesn't crash
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    printer.print();

    System.setOut(originalOut);

    String output = outContent.toString();
    assertFalse(output.isEmpty());
    assertTrue(output.contains("epsilon"));
  }
}


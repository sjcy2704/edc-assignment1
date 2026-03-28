import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests produced by Claude.
 */
public class RegexParser_Test {

  @Test
  public void testSimpleChar() {
    RegexParser parser = new RegexParser("a");
    String validated = parser.validate();
    assertEquals("a", validated);
  }

  @Test
  public void testSequence() {
    RegexParser parser = new RegexParser("abc");
    String validated = parser.validate();
    assertEquals("abc", validated);
  }

  @Test
  public void testAlternation() {
    RegexParser parser = new RegexParser("a|b");
    String validated = parser.validate();
    assertEquals("a|b", validated);
  }

  @Test
  public void testStar() {
    RegexParser parser = new RegexParser("a*");
    String validated = parser.validate();
    assertEquals("a*", validated);
  }

  @Test
  public void testPlus() {
    RegexParser parser = new RegexParser("a+");
    String validated = parser.validate();
    assertEquals("a+", validated);
  }

  @Test
  public void testParentheses() {
    RegexParser parser = new RegexParser("(ab)");
    String validated = parser.validate();
    assertEquals("(ab)", validated);
  }

  @Test
  public void testComplexRegex() {
    RegexParser parser = new RegexParser("(ab)*|c+");
    String validated = parser.validate();
    assertEquals("(ab)*|c+", validated);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyRegex() {
    RegexParser parser = new RegexParser("");
    parser.validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUnbalancedParens1() {
    RegexParser parser = new RegexParser("(ab");
    parser.validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUnbalancedParens2() {
    RegexParser parser = new RegexParser("ab)");
    parser.validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStarPlacement() {
    RegexParser parser = new RegexParser("*ab");
    parser.validate();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidPipePlacement() {
    RegexParser parser = new RegexParser("|ab");
    parser.validate();
  }

  @Test
  public void testSpaceCharacter() {
    RegexParser parser = new RegexParser("a b");
    String validated = parser.validate();
    assertEquals("a b", validated);
  }
}

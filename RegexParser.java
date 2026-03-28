/**
 * RegexParser class validates the regex inputted
 */
public class RegexParser {
  private final String regex;

  /**
   * Creates a parser for the given regular expression.
   *
   * @param regex The regular expression to parse
   */
  public RegexParser(String regex) {
    this.regex = regex;
  }

  /**
   * Validates the regular expression.
   *
   * @return The validated regex string
   * @throws IllegalArgumentException if the regex is invalid
   */
  public String validate() throws IllegalArgumentException {
    if (regex.isEmpty()) {
      throw new IllegalArgumentException("Error: regex is empty");
    }

    // Check for balanced parentheses
    int parenthesesCount = 0;
    for (int i = 0; i < regex.length(); i++) {
      char c = regex.charAt(i);

      if (c == '(') {
        parenthesesCount++;
      } else if (c == ')') {
        parenthesesCount--;
        if (parenthesesCount < 0) {
          throw new IllegalArgumentException("Error: parentheses are unbalanced");
        }
      }
    }

    if (parenthesesCount != 0) {
      throw new IllegalArgumentException("Error: parentheses are unbalanced");
    }

    // Check for invalid operator placement
    for (int i = 0; i < regex.length(); i++) {
      char c = regex.charAt(i);

      // Star and plus cannot be first
      if (i == 0 && (c == '*' || c == '+')) {
        throw new IllegalArgumentException("Error: invalid operator placement");
      }

      // Pipe cannot be first or last
      if (c == '|' && (i == 0 || i == regex.length() - 1)) {
        throw new IllegalArgumentException("Error: invalid pipe placement");
      }
    }

    return regex;
  }
}


import java.util.Stack;

/**
 * RegexToNFA class will be responsible of building the epsilon-NFA using the
 * approach from Chapter 4.3.
 *
 * 1. Construct an NFAFragment for single characters
 * 2. Combine the fragments using operators (concatenation, alternation (|),
 * repetition (*))
 * 3. Then combine everything with epsilon transitions
 */
public class RegexToNFA {

  /**
   * Builds the epsilon-NFA from the regex string.
   *
   * @param regex The regular expression string
   * @return An E_NFAStructure representing the regex
   * @throws IllegalArgumentException if the regex is invalid
   */
  public E_NFAStructure build(String regex) throws IllegalArgumentException {
    State.resetIdCounter();

    // Convert infix notation to postfix for easier processing when going for a
    // stack approach
    String postfix = infixToPostfix(regex);

    // Build NFA using a stack-based approach
    Stack<NFAFragment> stack = new Stack<>();

    for (int i = 0; i < postfix.length(); i++) {
      char c = postfix.charAt(i);

      if (c == '\0') {
        // Concatenation operator (marked with \0)
        NFAFragment b = stack.pop();
        NFAFragment a = stack.pop();
        stack.push(buildConcat(a, b));
      } else if (c == '|') {
        // Alternation: a|b
        NFAFragment b = stack.pop();
        NFAFragment a = stack.pop();
        stack.push(buildAlternation(a, b));
      } else if (c == '*') {
        // Kleene star: a*
        NFAFragment a = stack.pop();
        stack.push(buildStar(a));
      } else if (c == '+') {
        // Kleene plus: a+
        NFAFragment a = stack.pop();
        stack.push(buildPlus(a));
      } else {
        // Regular character
        stack.push(buildChar(c));
      }
    }

    if (stack.size() != 1) {
      throw new IllegalArgumentException("Error: Invalid regex");
    }

    // Get the final fragment and create accept state
    NFAFragment finalFragment = stack.pop();
    State acceptState = new State(true);

    // Connect all dangling transitions to accept state
    finalFragment.connectDanglingTo(acceptState);

    return new E_NFAStructure(finalFragment.getStart(), acceptState);
  }

  /**
   * Builds an NFA fragment for a single character.
   *
   * Based on Chapter 4, Section 4.3: "The simplest regular expression, a,
   * can be converted to a two-state NFA, with a start-state and a final state,
   * by writing the character a on the transition"
   *
   * @param c The character
   * @return NFA fragment for this character
   */
  private NFAFragment buildChar(char c) {
    State start = new State(false);
    State end = new State(false);

    Transition t = new Transition(start, end, c);
    start.addTransition(t);

    NFAFragment fragment = new NFAFragment(start);
    // The transition to 'end' is dangling and will be connected later
    fragment.addDanglingTransition(new Transition(end, null, null));

    return fragment;
  }

  /**
   * Builds an NFA fragment for alternation (a|b).
   *
   * Creates a new start state with epsilon transitions to both fragments.
   *
   * Based on Chapter 4, Section 4.3: "A regular expression of the form a|b
   * can be handled by building two simple NFAs, to handle the a and b, and
   * then running them in parallel. We use ε transitions to glue the parts
   * together"
   *
   * @param a First fragment
   * @param b Second fragment
   * @return NFA fragment for a|b
   */
  private NFAFragment buildAlternation(NFAFragment a, NFAFragment b) {
    State start = new State(false);

    // Epsilon transitions to both alternatives
    Transition toA = new Transition(start, a.getStart(), null);
    Transition toB = new Transition(start, b.getStart(), null);
    start.addTransition(toA);
    start.addTransition(toB);

    NFAFragment fragment = new NFAFragment(start);

    // Collect dangling transitions from both fragments
    for (Transition t : a.getDanglingTransitions()) {
      fragment.addDanglingTransition(t);
    }
    for (Transition t : b.getDanglingTransitions()) {
      fragment.addDanglingTransition(t);
    }

    return fragment;
  }

  /**
   * Builds an NFA fragment for Kleene star (a*).
   *
   * Creates loops allowing zero or more repetitions.
   *
   * Based on Chapter 4, Section 4.3: "To handle the regular expression a*,
   * we simply add a pair of ε transitions to the NFA for a. One of the
   * transitions allows us to completely bypass the NFA (allowing
   * 'zero-times'), and the other transition allows an infinite number of
   * repetitions
   *
   * @param a The fragment to repeat
   * @return NFA fragment for a*
   */
  private NFAFragment buildStar(NFAFragment a) {
    State start = new State(false);
    State end = new State(false);

    // Epsilon transition to skip (zero repetitions)
    Transition skip = new Transition(start, end, null);
    start.addTransition(skip);

    // Epsilon transition to enter the fragment
    Transition enter = new Transition(start, a.getStart(), null);
    start.addTransition(enter);

    // Connect fragment's dangling transitions to loop back or exit
    for (Transition t : a.getDanglingTransitions()) {
      State from = t.getFrom();
      // Loop back to start for more repetitions
      Transition loop = new Transition(from, start, null);
      from.addTransition(loop);
      // Also allow exit
      Transition exit = new Transition(from, end, null);
      from.addTransition(exit);
    }

    NFAFragment fragment = new NFAFragment(start);
    fragment.addDanglingTransition(new Transition(end, null, null));

    return fragment;
  }

  /**
   * Builds an NFA fragment for plus (a+).
   *
   * Similar to star but requires at least one repetition.
   * Based on Chapter 4: a+ has the same meaning as aa*
   *
   * @param a The fragment to repeat
   * @return NFA fragment for a+
   */
  private NFAFragment buildPlus(NFAFragment a) {
    State start = a.getStart();
    State end = new State(false);

    // Connect fragment's dangling transitions to loop back or exit
    for (Transition t : a.getDanglingTransitions()) {
      State from = t.getFrom();
      // Loop back to start for more repetitions
      Transition loop = new Transition(from, start, null);
      from.addTransition(loop);
      // Allow exit
      Transition exit = new Transition(from, end, null);
      from.addTransition(exit);
    }

    NFAFragment fragment = new NFAFragment(start);
    fragment.addDanglingTransition(new Transition(end, null, null));

    return fragment;
  }

  /**
   * Asked Claude to help me with the processing of regex into NFA fragments.
   * Converting from infix to postfix notation makes it easier to process since we
   * are going for a stack approach.
   *
   * What does it do and why it is better?
   *
   * First, we need to understand that each operator has a level of importance.
   * We can learn from it in Chapter 4.2.
   *
   * 1. Repetition (*,+) - highest precedence
   * 2. Concatenation - middle precedence
   * 3. Alternation (|) - lowest Precedence
   *
   * Now, what the algorithm does is to change the order of the string so that
   * is favorable for the stack. We can take a simple calculator example when
   * using a stack, lets say 1 + 1, we can't just add them to the stack in order,
   * we want to know first the numbers/values and then the operation the
   * calculator has to do, so in reality it would be [1 1 +]. It pops both values
   * and then perform the sum operation.
   *
   * Similarly with regex, we want to know the values before we do an operation.
   * For example, the regex "a|bc*", following the precedence rule it will be
   * transformed to "abc*\0|". Each operator will do something different, for
   * example, once we hit '*', we only pop one value 'c' and then construct the
   * NFA fragment accordingly.
   *
   * @param regex Infix regex string
   * @return Postfix regex string
   */
  private String infixToPostfix(String regex) {
    // First, insert explicit concatenation operators
    String withConcat = insertConcatOperators(regex);

    // Then convert to postfix using Shunting Yard
    Stack<Character> operators = new Stack<>();
    StringBuilder output = new StringBuilder();

    for (int i = 0; i < withConcat.length(); i++) {
      char c = withConcat.charAt(i);

      if (c == '(') {
        operators.push(c);
      } else if (c == ')') {
        while (!operators.isEmpty() && operators.peek() != '(') {
          output.append(operators.pop());
        }
        if (!operators.isEmpty()) {
          operators.pop(); // Remove the left paren
        }
      } else if (isOperator(c) || c == '\0') {
        // It's an operator (*, +, |, or concat marked with \0)
        while (!operators.isEmpty() &&
            operators.peek() != '(' &&
            precedence(operators.peek()) >= precedence(c)) {
          output.append(operators.pop());
        }
        operators.push(c);
      } else {
        // Regular character
        output.append(c);
      }
    }

    while (!operators.isEmpty()) {
      output.append(operators.pop());
    }

    return output.toString();
  }

  /**
   * Inserts explicit concatenation operators between characters.
   *
   * For example: "ab" becomes "a\0b" (where \0 is concat)
   *
   * @param regex Original regex string
   * @return Regex with concatenation operators inserted
   */
  private String insertConcatOperators(String regex) {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < regex.length(); i++) {
      char current = regex.charAt(i);
      result.append(current);

      if (i < regex.length() - 1) {
        char next = regex.charAt(i + 1);

        // Insert concat if:
        // - current is char/rparen/star/plus AND next is char/lparen
        boolean needsConcat = false;

        if ((isLiteral(current) || current == ')' || current == '*' || current == '+') &&
            (isLiteral(next) || next == '(')) {
          needsConcat = true;
        }

        if (needsConcat) {
          // Use \0 as concatenation marker
          result.append('\0');
        }
      }
    }

    return result.toString();
  }

  /**
   * Returns the precedence of an operator.
   * Higher number = higher precedence.
   *
   * Based on Chapter 4, Section 4.2 (Precedence of operators):
   * - Repetition (*,+): 3
   * - Concatenation: 2
   * - Alternation (|): 1
   *
   * @param c The operator character
   * @return Precedence value
   */
  private int precedence(char c) {
    if (c == '*' || c == '+') {
      return 3; // Highest precedence
    } else if (c == '\0') {
      return 2; // Concatenation (marked with \0)
    } else if (c == '|') {
      return 1; // Lowest precedence
    }
    return 0;
  }

  /**
   * Checks if a character is an operator.
   *
   * @param c The character to check
   * @return true if the character is an operator
   */
  private boolean isOperator(char c) {
    return c == '|' || c == '*' || c == '+';
  }

  /**
   * Checks if a character is a literal character
   *
   * @param c The character to check
   * @return true if the character is a literal
   */
  private boolean isLiteral(char c) {
    return !isOperator(c) && c != '(' && c != ')' && c != '\0';
  }

  /**
   * Builds an NFA fragment for concatenation (ab).
   *
   * Connects fragment 'a' to fragment 'b' by linking a's dangling
   * transitions to b's start state.
   *
   * Based on Chapter 4, Section 4.3: "A sequence of simple regular
   * expressions abc, can be handled by converting each character in
   * the expression to a two-state NFA, and then joining the NFAs
   * together with ε transitions"
   *
   * @param a First fragment
   * @param b Second fragment
   * @return NFA fragment for ab
   */
  private NFAFragment buildConcat(NFAFragment a, NFAFragment b) {
    // Connect a's dangling transitions to b's start
    for (Transition t : a.getDanglingTransitions()) {
      State from = t.getFrom();
      Transition toB = new Transition(from, b.getStart(), null);
      from.addTransition(toB);
    }

    // Result has a's start and b's dangling transitions
    NFAFragment fragment = new NFAFragment(a.getStart());
    for (Transition t : b.getDanglingTransitions()) {
      fragment.addDanglingTransition(t);
    }

    return fragment;
  }
}

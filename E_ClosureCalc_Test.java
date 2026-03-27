import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Set;

/**
 * Tests produced by Claude
 */
public class E_ClosureCalc_Test {

    /**
     * Test epsilon closure of single state with no epsilon transitions.
     * Expected: ε-closure(q0) = {q0}
     */
    @Test
    public void testSingleStateNoEpsilon() {
        State s0 = new State(false);
        E_ClosureCalc calc = new E_ClosureCalc();
        
        Set<State> closure = calc.compute(s0);
        
        assertEquals(1, closure.size());
        assertTrue(closure.contains(s0));
    }

    /**
     * Test epsilon closure with chain: q0 --ε--> q1 --ε--> q2
     * Expected: ε-closure(q0) = {q0, q1, q2}
     */
    @Test
    public void testEpsilonChain() {
        State s0 = new State(false);
        State s1 = new State(false);
        State s2 = new State(true);
        
        s0.addTransition(new Transition(s0, s1, null)); // epsilon to s1
        s1.addTransition(new Transition(s1, s2, null)); // epsilon to s2
        
        E_ClosureCalc calc = new E_ClosureCalc();
        Set<State> closure = calc.compute(s0);
        
        assertEquals(3, closure.size());
        assertTrue(closure.contains(s0));
        assertTrue(closure.contains(s1));
        assertTrue(closure.contains(s2));
    }

    /**
     * Test epsilon closure with cycle: q0 --ε--> q1 --ε--> q0
     * Expected: ε-closure(q0) = {q0, q1} (handles cycle)
     */
    @Test
    public void testEpsilonCycle() {
        State s0 = new State(false);
        State s1 = new State(false);
        
        s0.addTransition(new Transition(s0, s1, null)); // epsilon to s1
        s1.addTransition(new Transition(s1, s0, null)); // epsilon back to s0
        
        E_ClosureCalc calc = new E_ClosureCalc();
        Set<State> closure = calc.compute(s0);
        
        assertEquals(2, closure.size());
        assertTrue(closure.contains(s0));
        assertTrue(closure.contains(s1));
    }

    /**
     * Test epsilon closure with multiple paths:
     *     ε     ε
     * q0 --> q1 --> q3
     *  |            ^
     *  ε            ε
     *  v            |
     * q2 -----------+
     *
     * Expected: ε-closure(q0) = {q0, q1, q2, q3}
     */
    @Test
    public void testMultipleEpsilonPaths() {
        State s0 = new State(false);
        State s1 = new State(false);
        State s2 = new State(false);
        State s3 = new State(true);
        
        s0.addTransition(new Transition(s0, s1, null)); // epsilon to s1
        s0.addTransition(new Transition(s0, s2, null)); // epsilon to s2
        s1.addTransition(new Transition(s1, s3, null)); // epsilon to s3
        s2.addTransition(new Transition(s2, s3, null)); // epsilon to s3
        
        E_ClosureCalc calc = new E_ClosureCalc();
        Set<State> closure = calc.compute(s0);
        
        assertEquals(4, closure.size());
        assertTrue(closure.contains(s0));
        assertTrue(closure.contains(s1));
        assertTrue(closure.contains(s2));
        assertTrue(closure.contains(s3));
    }
}

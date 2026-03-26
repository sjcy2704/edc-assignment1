import org.junit.*;
import static org.junit.Assert.*;

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
}

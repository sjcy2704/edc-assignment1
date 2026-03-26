public class State {
    private static int nextId = 0;
    private int id;
    private boolean isAccepting;

    public State(boolean isAccepting) {
        this.id = nextId++;
        this.isAccepting = isAccepting;
    }

    public int getId() {
        return id;
    }

    public boolean isAccepting() {
        return isAccepting;
    }

    public String getName() {
        return "q" + id;
    }

    public static void resetIdCounter() {
        nextId = 0;
    }

    public void setAccepting(boolean accepting) {
        this.isAccepting = accepting;
    }
}

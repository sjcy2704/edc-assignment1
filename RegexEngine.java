import java.util.Scanner;

public class RegexEngine {
    private boolean verbose;

    public RegexEngine(boolean verbose) {
        this.verbose = verbose;
    }

    public static void main(String[] args) {
        boolean verbose = false;

        // Check for verbose flag
        if (args.length > 0 && args[0].equals("-v")) {
            verbose = true;
        }

        RegexEngine engine = new RegexEngine(verbose);
        engine.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        scanner.close();
    }
}

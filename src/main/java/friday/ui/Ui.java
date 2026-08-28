package friday.ui;

import java.util.Scanner;

/**
 * Handles all console input and output for the Friday application.
 */
public class Ui {
    private static final String SEPARATOR =
            "____________________________________________________________";
    private static final String BANNER = " ______    _     _             \n"
                                       + "|  ____|  (_)   | |            \n"
                                       + "| |__ _ __ _  __| | __ _ _   _\n"
                                       + "|  __| '__| |/ _` |/ _` | | | |\n"
                                       + "| |  | |  | | (_| | (_| | |_| |\n"
                                       + "|_|  |_|  |_|\\__,_|\\__,_|\\__, |\n"
                                       + "                          __/ |\n"
                                       + "                         |___/ \n";

    private final Scanner scanner;

    /**
     * Creates a UI connected to the standard console input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the greeting shown when Friday starts.
     */
    public void showWelcome() {
        showSeparator();
        System.out.print(BANNER);
        showLine("Hello! I'm Friday.");
        showLine("What you want ah?");
        showSeparator();
    }

    /**
     * Reads the next command, treating end-of-input as a request to exit.
     *
     * @return Trimmed command, or {@code "bye"} at end-of-input.
     */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine().trim() : "bye";
    }

    /**
     * Displays one line of text to the user.
     *
     * @param message Message to display.
     */
    public void showLine(String message) {
        System.out.println(message);
    }

    /**
     * Displays the standard separator between commands and responses.
     */
    public void showSeparator() {
        showLine(SEPARATOR);
    }

    /**
     * Displays the farewell shown when Friday exits.
     */
    public void showGoodbye() {
        showSeparator();
        showLine("Bye. See you next time lah!");
        showSeparator();
    }

    /**
     * Releases the console input resource.
     */
    public void close() {
        scanner.close();
    }
}

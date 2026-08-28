package friday.exception;

/**
 * Represents an invalid command entered by the user.
 */
public class FridayException extends Exception {
    private static final long serialVersionUID = 1L;

    /** Creates an exception with an explanation of the invalid command. */
    public FridayException(String message) {
        super(message);
    }
}

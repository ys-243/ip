/**
 * Represents an invalid command entered by the user.
 */
public class FridayException extends Exception {
    private static final long serialVersionUID = 1L;

    public FridayException(String message) {
        super(message);
    }
}

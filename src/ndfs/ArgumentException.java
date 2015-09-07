package ndfs;

/**
 * This exception gets thrown in case of a wrong program argument.
 */
public class ArgumentException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an <code>ArgumentException</code> with the specified message.
     *
     * @param message
     *            the message.
     */
    public ArgumentException(String message) {
        super(message);
    }
}
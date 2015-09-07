package ndfs;

/**
 * In this program, a result is passed as an exception.
 */
public abstract class ResultException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a result exception with the specified message.
     *
     * @param message
     *            the message
     */
    public ResultException(String message) {
        super(message);
    }
}

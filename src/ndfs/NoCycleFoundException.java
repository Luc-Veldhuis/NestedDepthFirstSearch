package ndfs;

/**
 * This exception, when thrown, indicates that no cycle was found.
 */
public class NoCycleFoundException extends ResultException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception indicating that no cycle was found.
     */
    public NoCycleFoundException() {
        super("Did not find a cycle");
    }

    /**
     * Creates an exception indicating that the specified thread did not find a
     * cycle.
     * 
     * @param id
     *            the thread number of the thread that did not find a cycle
     */
    public NoCycleFoundException(int id) {
        super("Thread " + id + ": did not find a cycle");
    }

    /**
     * Creates an exception indicatint that no cycle was found, with the
     * specified message.
     * 
     * @param message
     *            the message.
     */
    public NoCycleFoundException(String message) {
        super("Did not find a cycle: " + message);
    }

    /**
     * Creates an exception indicating that the specified thread did not find a
     * cycle, with the specified message.
     * 
     * @param id
     *            the thread number of the thread that dit not find a cycle
     * @param message
     *            the message.
     */
    public NoCycleFoundException(int id, String message) {
        super("Thread " + id + ": did not find a cycle. " + message);
    }
}

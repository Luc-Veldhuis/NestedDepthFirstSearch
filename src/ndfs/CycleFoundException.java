package ndfs;

/**
 * This exception, when thrown, indicates that a cycle was found.
 */
public class CycleFoundException extends ResultException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception indicating that a cycle was found.
     */
    public CycleFoundException() {
        super("Found a cycle");
    }

    /**
     * Creates an exception indicating that the specified thread
     * found a cycle.
     * @param id the thread number of the thread that found the cycle
     */
    public CycleFoundException(int id) {
        super("Thread " + id + ": found a cycle");
    }

    /**
     * Creates an exception indicatint that a cycle was found, with
     * the specified message.
     * @param message the message.
     */
    public CycleFoundException(String message) {
        super("Found a cycle: " + message);
    }

    /**
     * Creates an exception indicating that the specified thread
     * found a cycle, with the specified message.
     * @param id the thread number of the thread that found the cycle
     * @param message the message.
     */
    public CycleFoundException(int id, String message) {
        super("Thread " + id + ": found a cycle. " + message);
    }
}

package ndfs;

/**
 * This interface specifies the way this framework calls an NDFS implementation
 * and the way the result is passed.
 */
public interface NDFS {

    /**
     * This method has no parameters, and passes its result on by throwing an
     * exception. It determines whether or not the graph has a cycle, and throws
     * the corresponding exception, either a {@link CycleFoundException} or a
     * {@link NoCycleFoundException}.
     *
     * @throws ResultException
     *             the result exception.
     */
    public void ndfs() throws ResultException;
}

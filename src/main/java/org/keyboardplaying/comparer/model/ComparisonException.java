package org.keyboardplaying.comparer.model;

/**
 * An exception to be thrown when method comparison fails.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class ComparisonException extends Exception {

    /** Generated serial version UID. */
    private static final long serialVersionUID = -3223910951558666707L;

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p/>
     * Note that the detail message associated with {@code cause} is <em>not</em> automatically incorporated in this
     * exception's detail message.
     *
     * @param message
     *            the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause
     *            the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
     *            value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ComparisonException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of
     * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains the class and detail message of
     * <tt>cause</tt>). This constructor is useful for exceptions that are little more than wrappers for other
     * throwables (for example, {@link java.security.PrivilegedActionException}).
     *
     * @param cause
     *            the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
     *            value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ComparisonException(Throwable cause) {
        super(cause);
    }
}

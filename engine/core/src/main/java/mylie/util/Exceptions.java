package mylie.util;

/**
 * A utility class that provides methods for working with exceptions.
 * This class is not intended to be instantiated.
 */
public final class Exceptions {
    private Exceptions() {}

    /**
     * Retrieves the root cause of the provided throwable. The root cause is determined
     * by traversing the cause chain until a throwable with no cause is found.
     *
     * @param throwable the throwable for which the root cause is to be determined; must not be null
     * @return the root cause of the provided throwable
     */
    public static Throwable getRootCause(Throwable throwable) {
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }
}

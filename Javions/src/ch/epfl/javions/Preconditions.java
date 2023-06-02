package ch.epfl.javions;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The Preconditions class provides methods to check preconditions.
 * This class cannot be instantiated and contains only static methods.
 */
public final class Preconditions {
    private Preconditions() {
        // This constructor is private to prevent instantiation of the Preconditions class.
    }


    /**
     * Checks that a condition is true.
     * @param shouldBeTrue the condition to check that is true
     */
    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }

}

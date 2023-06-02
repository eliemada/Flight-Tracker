package ch.epfl.javions;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The Math2 class provides additional mathematical operations.
 * This class cannot be instantiated and contains only static methods.
 */
public final class Math2 {
    /**
     * This constructor is private to prevent instantiation of the Math2 class.
     */
    private Math2() {
    }

    /**
     * Returns a value clamped between a minimum and maximum value.
     *
     * @param min   the minimum value to clamp to
     * @param value the value to clamp
     * @param max   the maximum value to clamp to
     * @return the value value clamped between min and max
     * @throws IllegalArgumentException if min is greater than max
     */
    public static int clamp(int min, int value, int max) {
        Preconditions.checkArgument(min <= max);
        return Math.max(min, Math.min(value, max));
    }

    /**
     * Returns the inverse hyperbolic sine of a value.
     *
     * @param x the value to calculate the inverse hyperbolic sine of
     * @return the inverse hyperbolic sine of x
     */
    public static double asinh(double x) {
        return Math.log(x + Math.sqrt(1 + Math.pow(x, 2)));
    }
}

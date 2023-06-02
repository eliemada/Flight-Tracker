package ch.epfl.javions;


import java.util.Objects;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The {@code Bits} class contains methods for extracting bits from a long value.
 */
public final class Bits {

    private Bits() {
        // Prevents instantiation of the class
        throw new AssertionError();
    }

    /**
     * Extracts an unsigned integer value of the specified size from the given long value,
     * starting at the specified index.
     *
     * @param value the long value to extract the bits from
     * @param start the starting index of the bit range to extract
     * @param size  the number of bits to extract
     * @return the extracted unsigned integer value
     * @throws IndexOutOfBoundsException if the start or the range of bits is not within [0, 64)
     * @throws IllegalArgumentException  if the size is not greater than 0 and less than 32
     */
    public static int extractUInt(long value, int start, int size) {
        Preconditions.checkArgument(!(size <= 0 || size >= Integer.SIZE));
        Objects.checkFromIndexSize(start, size, Long.SIZE);

        int mask = (1 << size) - 1;
        return (int) ((value >>> start) & mask);
    }

    /**
     * Tests the value of the bit at the specified index of the given long value.
     *
     * @param value the long value to test the bit from
     * @param index the index of the bit to test
     * @return true if the bit is set to 1, false otherwise
     * @throws IndexOutOfBoundsException if the index is not within [0, 64)
     */
    public static boolean testBit(long value, int index) {
        Objects.checkIndex(index, Long.SIZE);
        return ((value >>> index) & 1) == 1;
    }
}
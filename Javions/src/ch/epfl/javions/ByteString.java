package ch.epfl.javions;

import java.util.Arrays;
import java.util.HexFormat;
import java.util.Objects;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The ByteString class represents an immutable sequence of bytes. It provides methods to obtain the size of
 * the sequence, retrieve individual bytes, and compare instances for equality. It also provides methods
 * to create instances from hexadecimal strings and to retrieve the bytes in a specified range as a long value.
 */
public final class ByteString {

    private final static HexFormat HEX_FORMAT = HexFormat.of().withUpperCase();

    private final byte[] bytes;

    /**
     * Constructs a new ByteString instance from the given byte array.
     *
     * @param bytes the byte array to use as the contents of this ByteString
     */
    public ByteString(byte[] bytes) {
        // Make a copy of the byte array to ensure immutability.
        this.bytes = bytes.clone();

    }

    /**
     * Returns a new ByteString instance created from the given hexadecimal string.
     *
     * @param hexString the hexadecimal string to use as the contents of the new ByteString instance
     * @return the new ByteString instance
     * @throws IllegalArgumentException if the length of the hexadecimal string is odd
     */
    public static ByteString ofHexadecimalString(String hexString) {
        // Ensure that the hexadecimal string has an even length.
        Preconditions.checkArgument((hexString.length() & 1) == 0); //The hexString should be even
        // Parse the hexadecimal string into a byte array.
        byte[] bytes = HEX_FORMAT.parseHex(hexString);

        // Create a new ByteString instance from the byte array.
        return new ByteString(bytes);
    }

    /**
     * Returns the size of this ByteString, i.e., the number of bytes it contains.
     *
     * @return the size of this ByteString
     */
    public int size() {
        return bytes.length;
    }

    /**
     * Returns the unsigned byte (as an integer between 0 and 255) at the given index in this ByteString.
     *
     * @param index the index of the byte to be returned
     * @return the unsigned byte (as an integer between 0 and 255) at the given index in this ByteString
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    public int byteAt(int index) {
        // Check that the index is within the valid range for this ByteString.
        Objects.checkIndex(index, size());
        // Convert the byte to an unsigned integer and return it.
        return Byte.toUnsignedInt(bytes[index]);
    }

    /**
     * Returns the bytes in the specified range as a long value.
     *
     * @param fromIndex the index of the first byte to include in the returned value (inclusive)
     * @param toIndex   the index of the first byte to exclude from the returned value (exclusive)
     * @return the bytes in the specified range as a long value
     * @throws IndexOutOfBoundsException if the range described by fromIndex and toIndex is not
     *                                   entirely contained between 0 and the size of this ByteString
     * @throws IllegalArgumentException  if the difference between toIndex and fromIndex is not
     *                                   strictly less than the number of bytes contained in a long
     *                                   value
     */
   public long bytesInRange(int fromIndex, int toIndex) {
       // Check that the range is within the valid range for this ByteString.
       Objects.checkFromToIndex(fromIndex, toIndex, bytes.length);
       // Check that the number of bytes in the range is less than or equal to the number of bytes in a long value.
       int numBytes = toIndex - fromIndex;
        Preconditions.checkArgument(numBytes < Long.BYTES);
       long result = 0;
       // Iterate through each byte in the range.
       for (int i = fromIndex; i < toIndex; i++) {
           // Shift the current result by 8 bits to make room for the next byte, and OR the next byte to the result.
           result <<= Byte.SIZE;
           result |= byteAt(i);
       }
       return result;
   }


    /**
     * Compares the contents of this ByteString with the contents of another ByteString.
     *
     * @param other the ByteString to compare with
     * @return true if the contents of the two ByteStrings are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof ByteString that &&
               Arrays.equals(this.bytes, that.bytes);
    }



    /**
     * Returns the hash code of this byte string.
     *
     * @return the hash code value for this object
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    /**
     * Each byte in the array is converted into a two-digit hexadecimal
     * string, and the resulting strings are concatenated together.
     *
     * @return the hexadecimal string representation of the byte array.
     */
    @Override
    public String toString() {
        HexFormat hf = HexFormat.of().withUpperCase();
        return hf.formatHex(bytes);
    }


}


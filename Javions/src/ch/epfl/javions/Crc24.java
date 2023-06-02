package ch.epfl.javions;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * A class for calculating 24-bit CRC checksums.
 */
public final class Crc24 {
    /**
     * The default generator value.
     */
    public static final int GENERATOR = 0xFFF409;

    /**
     * The number of bits in the CRC.
     */
    private static final int   CRC_SIZE   = 24;

    /**
     * The index of the first bit of the CRC.
     */
    private static final int   CRC_START_INDEX = 0;

    /**
     * The size of the lookup table.
     */
    private static final int   TABLE_SIZE = 256;
    /**
     * A lookup table for this CRC.
     */
    private final        int[] table;


    /**
     * Creates a new Crc24 instance with the given generator value.
     *
     * @param generator The generator value to use for this CRC.
     */
    public Crc24(int generator) {
        this.table = buildTable(generator);
    }

    /**
     * Calculates the CRC for the given byte array using bitwise operations.
     *
     * @param generator The generator value to use for this CRC.
     * @param bytes     The input bytes to calculate the CRC for.
     * @return The calculated CRC value.
     */
    private static int crc_bitwise(int generator, byte[] bytes) {
        int[] table = {0, generator};
        int   crc   = 0;
        for (byte aByte : bytes) {
            for (int j = Byte.SIZE - 1;
                 j >= 0; j--) { // Loop through each bit of the byte, starting from the most significant bit
                int bit = Bits.extractUInt(aByte, j, 1);
                crc = ((crc << 1) | bit) ^ table[Bits.extractUInt(crc, CRC_SIZE - 1, 1)];
                // Shifting by 1 is equivalent to multiplying by 2
            }
        }
        for (int i = 0;
             i < CRC_SIZE; i++) {
            crc = ((crc << 1)) ^ table[Bits.extractUInt(crc, CRC_SIZE - 1,
                                                        1)];
            // Shifting by 1 is equivalent to multiplying by 2
        }
        crc = Bits.extractUInt(crc, 0,
                               CRC_SIZE);
        return crc; // Return the final CRC value
    }


    /**
     * Builds a lookup table for the given generator value.
     *
     * @param generator The generator value to build the table for.
     * @return The lookup table for the given generator value.
     */
    private static int[] buildTable(int generator) {
        int[] table = new int[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++) {
            byte[] bytes = new byte[]{(byte) i};
            table[i] = crc_bitwise(generator, bytes);
        }
        return table;
    }

    /**
     * Calculates the CRC for the given byte array.
     *
     * @param bytes The input bytes to calculate the CRC for.
     * @return The calculated CRC value.
     */
    public int crc(byte[] bytes) {
        int crc = 0;
        for (byte o : bytes) {
            // XOR the current byte with the CRC and lookup the result in the table
            crc = ((crc << Byte.SIZE) | Byte.toUnsignedInt(o)) ^ table[Bits.extractUInt(crc,
                                                                                        CRC_SIZE - Byte.SIZE,
                                                                                        Byte.SIZE)];
        }
        for (int i = 0; i < CRC_SIZE / Byte.SIZE; i++) {
            // XOR the CRC with the table entry for the last Byte.SIZE bits of the CRC
            crc = ((crc << Byte.SIZE) ^ table[Bits.extractUInt(crc, CRC_SIZE - Byte.SIZE, Byte.SIZE)]);
        }
        // Extract the CRC_SIZE least significant bits of the CRC and return them
        return Bits.extractUInt(crc, CRC_START_INDEX, CRC_SIZE);


        // The following line can be used to test the crc_bitwise method
        // return crc_bitwise(generator,bytes);
    }
}

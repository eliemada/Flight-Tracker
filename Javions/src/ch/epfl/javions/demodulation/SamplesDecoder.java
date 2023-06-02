package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * <p>
 * The SamplesDecoder class decodes a batch of raw audio samples read from an InputStream
 * into an array of short values. It uses a bias value of 2048 to convert the two's complement
 * representation to the standard signed short format.
 */
public final class SamplesDecoder {
    private final static int    BIAS = 2048;
    /**
     * The number of samples to decode in each batch
     */
    private final        int    batchSize;
    /**
     * Buffer to store the bytes read from the input stream
     */
    private final        byte[] buffer;

    /**
     * The InputStream to read raw samples from
     */
    private final InputStream stream;

    /**
     * Constructs a SamplesDecoder with the specified InputStream and batch size.
     *
     * @param stream    the InputStream to read raw samples from
     * @param batchSize the number of samples to decode in each batch
     * @throws NullPointerException     if the stream argument is null
     * @throws IllegalArgumentException if the batchSize argument is less than or equal to zero
     */

    public SamplesDecoder(InputStream stream, int batchSize) {
        Preconditions.checkArgument(batchSize > 0);// Check if the batch size is valid
        Objects.requireNonNull(stream); // Check if the stream is not null
        this.stream    = stream;
        this.batchSize = batchSize;
        this.buffer    = new byte[batchSize * Short.BYTES];// Initialize the buffer to the required size
    }

    /**
     * Reads a batch of raw samples from the InputStream and decodes them into the
     * specified short array using the bias value. Returns the number of samples read.
     *
     * @param batch the short array to store the decoded samples in
     * @return the number of samples read from the InputStream
     * @throws IOException              if an I/O error occurs while reading from the InputStream
     * @throws IllegalArgumentException if the length of the batch argument does not match the batch size
     */

    public int readBatch(short[] batch) throws IOException {
        Preconditions.checkArgument((batch.length == batchSize));// Check if the batch size is valid
        // Read bytes from the stream into the buffer
        int numberOfByteRead = stream.readNBytes(buffer, 0, Short.BYTES * batchSize);
        for (int i = 0; i < numberOfByteRead; i += Short.BYTES) {
            int lsb = Byte.toUnsignedInt((buffer[i + 1]));// Read the least significant byte
            int msb = Byte.toUnsignedInt((buffer[i]));// Read the most significant byte
            // Decode the sample and store it in the array
            batch[i >> 1] = (short) ((lsb << Byte.SIZE | msb) - BIAS);
        }
        return numberOfByteRead >> 1;// Return the number of samples read
    }
}

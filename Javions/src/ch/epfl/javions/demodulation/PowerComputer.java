package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * <p>
 * The PowerComputer class calculates the power of a batch of samples using circular buffering.
 */

public final class PowerComputer {
    private final static int            NUMBER_OF_SAMPLES = 8;
    private final        int            batchSize;
    private final        SamplesDecoder decoder;
    private final        short[]        table;
    private final        short[]        circularTable;
    /**
     * This constant has been created after benchmarks where I observed that modulus operator was very slow,
     * Furthermore, we saw in the second lecture that doing : {@code x % Math.pow(2,n)} is slower than doing
     * {@code x & ((1 << n)-1}
     */
    private final        int            MODULUS           = ((1 << 3) - 1);
    private              int            tableHead         = 0;


    /**
     * Constructs a new PowerComputer object with the given input stream and batch size.
     *
     * @param stream    the input stream from which the samples are read
     * @param batchSize the number of samples in each batch
     * @throws IllegalArgumentException if the batch size is not positive or not divisible by 8
     */

    public PowerComputer(InputStream stream, int batchSize) {
        //Preconditions.checkArgument((batchSize > 0 )&& (batchSize % NUMBER_OF_SAMPLES == 0));
        Preconditions.checkArgument((batchSize > 0) && (batchSize & MODULUS) == 0); // This
        // is equivalent to the line above, as using modulus is slower than bitshifts.

        // Initializing variables
        this.batchSize     = batchSize;
        this.decoder       = new SamplesDecoder(stream, this.batchSize * 2);
        this.table         = new short[batchSize * 2];
        this.circularTable = new short[NUMBER_OF_SAMPLES];
    }

    /**
     * Reads a batch of samples from the input stream and calculates their power.
     *
     * @param batch the array where the computed power values will be stored
     * @return the number of samples read
     * @throws IOException if an I/O error occurs while reading the samples
     */
    public int readBatch(int[] batch) throws IOException {
        Preconditions.checkArgument(batch.length == batchSize);

        int numberRead = decoder.readBatch(table);
        for (int i = 0; i < numberRead; i += Short.BYTES) {
            circularTable[tableHead] = table[i];
            //The following line is equivalent to
            // circularTable[(tableHead + 1) % 8]
            circularTable[(tableHead + 1) & MODULUS] = table[i + 1];

            tableHead += 2;
            tableHead = tableHead & MODULUS;
            // Compute the I and Q components of the sample
            int I =
                    circularTable[6] -
                    circularTable[4] +
                    circularTable[2] -
                    circularTable[0];
            int Q = circularTable[7] -
                    circularTable[5] +
                    circularTable[3] -
                    circularTable[1];
            batch[i >> 1] = I * I + Q * Q;
        }

        return numberRead >> 1; // Divides by 2 the numberRead;
    }
}

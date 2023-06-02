package ch.epfl.javions.demodulation;

import ch.epfl.javions.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The PowerWindow class represents a sliding window of power samples over a signal stream.
 * It computes the power of each window by taking the mean square of the samples in the window.
 */
public final class PowerWindow {

    /**
     * The default size of a batch of samples to read from the input stream.
     */
    private final int DEFAULT_BATCH_SIZE = 1 << 16;

    /**
     * The size of the sliding window.
     */
    private final int windowSize;

    /**
     * The PowerComputer instance used to compute the power of each window.
     */
    private final PowerComputer powerComputer;

    /**
     * The array of samples in the first batch.
     */
    private int[] currentBatchSamples;

    /**
     * The array of samples in the second batch.
     */
    private int[] nextBatchSamples;


    /**
     * The size of the current batch of samples.
     */
    private int totalSamplesRead;

    /**
     * The current position in the stream.
     */
    private long position;


    /**
     * Constructs a PowerWindow object with the specified input stream and window size.
     *
     * @param stream     the input stream to read the signal from
     * @param windowSize the size of the sliding window
     * @throws IOException if there is an error reading from the input stream
     */
    public PowerWindow(InputStream stream, int windowSize) throws IOException {
        // Check that window size is within bounds.
        Preconditions.checkArgument(!(windowSize <= 0 || windowSize > (DEFAULT_BATCH_SIZE)));
        // Initialize fields.
        this.windowSize          = windowSize;
        this.powerComputer       = new PowerComputer(stream, DEFAULT_BATCH_SIZE);
        this.position            = 0;
        this.nextBatchSamples    = new int[DEFAULT_BATCH_SIZE];
        this.currentBatchSamples = new int[DEFAULT_BATCH_SIZE];
        totalSamplesRead         = powerComputer.readBatch(currentBatchSamples);
    }

    /**
     * Returns the size of the sliding window.
     *
     * @return the size of the sliding window
     */
    public int size() {
        return windowSize;
    }

    /**
     * Returns the current position in the stream.
     *
     * @return the current position in the stream
     */
    public long position() {
        return position;
    }

    /**
     * Returns whether the sliding window is full.
     *
     * @return true if the sliding window is full, false otherwise
     */
    public boolean isFull() {
        return windowSize + position <= totalSamplesRead;
    }

    /**
     * Returns the sample at the specified index in the sliding window.
     *
     * @param index the index of the sample to retrieve
     * @return the sample at the specified index in the sliding window
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public int get(int index) {
        // Check that index is within bounds.
        Objects.checkIndex(index, windowSize);
        // Get the sample from either the current or next batch.
        int batchIndex = (int) ((position + index) & (DEFAULT_BATCH_SIZE - 1));
        return (realPos() + index < DEFAULT_BATCH_SIZE) ?
                currentBatchSamples[batchIndex] :
                nextBatchSamples[batchIndex];
    }

    /**
     * Returns the position of the head of the sliding window.
     *
     * @return the position of the head of the sliding window
     */
    private long realPos() {
        return (position & (DEFAULT_BATCH_SIZE - 1));
    }
    // This code is equivalent to thd code using modulo operator, but it is faster.

    /**
     * Advances the sliding window by one sample.
     *
     * @throws IOException if there is an error reading from the input stream
     */
    public void advance() throws IOException {
        position++;// increment the position by 1
        // If the current position in the stream plus the window size equals the default batch size,
        // read in the next batch
        if ((realPos()) + windowSize - 1 == DEFAULT_BATCH_SIZE) {
            totalSamplesRead += powerComputer.readBatch(nextBatchSamples);
        }
        // If the current position in the stream equals 0, swap the current batch with the next batch
        if (realPos() == 0) {
            int[] temporaryDumpingTable; // temporary array for swapping
            temporaryDumpingTable = currentBatchSamples;
            currentBatchSamples   = nextBatchSamples;
            nextBatchSamples      = temporaryDumpingTable;
        }
    }


    /**
     * @param offset number of times we want to advanc  e the PowerWindow
     * @throws IOException if there is an error reading from the input stream
     */
    public void advanceBy(int offset) throws IOException {
        Preconditions.checkArgument(offset >= 0);
        for (int i = 0; i < offset; i++) {
            advance();
        }
    }
}

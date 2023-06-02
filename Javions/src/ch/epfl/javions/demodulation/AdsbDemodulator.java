package ch.epfl.javions.demodulation;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.adsb.RawMessage;

import java.io.IOException;
import java.io.InputStream;


/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The AdsbDemodulator class is responsible for decoding raw samples of ADS-B (Automatic Dependent Surveillance-Broadcast)
 * messages from an InputStream. This class uses a PowerWindow to compute the power of the received signal and then
 * demodulates the samples to extract a RawMessage object from the binary message.
 * <ul>
 * This class implements the following steps to extract the message:
 * <li>Check if the power of the received signal is above a certain threshold.</li>
 * <li>Check if the difference between the sum of the peaks and the sum of the valleys of the power is above a certain threshold.</li>
 * <li>Decode the first byte of the message to check the message length.</li>
 * <li>Decode the remaining 13 bytes of the message.</li>
 * <li>Return a RawMessage object containing the decoded message and the time at which it was received.</li>
 * </ul>
 * @author Elie BRUNO
 * @see RawMessage
 * @see PowerWindow
 */
public final class AdsbDemodulator {
    /**
     * The size of the window to use for peak and valley calculation.
     */
    private final static int SAMPLE_WINDOW_SIZE = 1200;

    /**
     * The conversion factor to convert nanoseconds to seconds.
     */
    private final static int NANOSEC_TO_SEC = 100;


    /**
     * The PowerWindow instance used for managing the incoming samples.
     */
    private final PowerWindow powerWindow;

    /**
     * The decoded message as an array of bytes.
     */
    private final byte[] messageDecoded;

    /**
     * Constructs a new AdsbDemodulator instance.
     *
     * @param samplesStream the input stream to read samples from
     * @throws IOException if an I/O error occurs while reading the input stream
     */
    public AdsbDemodulator(InputStream samplesStream) throws IOException {
        // Initialize a new PowerWindow instance to manage the incoming samples with a fixed window size.
        this.powerWindow = new PowerWindow(samplesStream, SAMPLE_WINDOW_SIZE);
        // Allocate an array of bytes to store the decoded message.
        this.messageDecoded = new byte[RawMessage.LENGTH];
    }

    /**
     * Calculates the sum of the peaks for a given offset.
     *
     * @param offset the offset to use for the calculation
     * @return the sum of the peaks for the given offset
     */
    private int sumOfPeaks(int offset) {
        // Compute the sum of the peaks for a given offset.
        return powerWindow.get(offset) + powerWindow.get(10 + offset) +
               powerWindow.get(35 + offset) + powerWindow.get(45 + offset);
    }

    /**
     * Checks if a peak has been found.
     *
     * @param sumIndexMinusOne the sum of peaks from the previous iteration
     * @param actualSumOfPeak  the current sum of peaks
     * @return true if a peak has been found, false otherwise
     */
    private boolean isPeakFound(int sumIndexMinusOne, int actualSumOfPeak) {
        return (sumIndexMinusOne < actualSumOfPeak) &&
               (actualSumOfPeak > sumOfPeaks(1));
    }

    /**
     * Calculates the sum of the valleys.
     *
     * @return the sum of the valleys
     */
    private int sumOfValleys() {
        return powerWindow.get(5) + powerWindow.get(15) + powerWindow.get(20) +
               powerWindow.get(25) + powerWindow.get(30) + powerWindow.get(40);
    }

    /**
     * Decodes a bit for a given index.
     *
     * @param index the index to use for the decoding
     * @return 0 if the power of the sample at index 80 + 10 * index is less than the power of the sample at index 85 + 10 * index, 1 otherwise
     */
    private int decodeBitsForIndex(int index) {
        return (powerWindow.get(80 + 10 * index) < powerWindow.get(85 + 10 * index)) ? 0 : 1;
    }

    /**
     * Decodes a byte at the specified index in the encoded data.
     *
     * @param index the index of the first bit of the byte to decode, starting from 0.
     * @return the decoded byte.
     * @throws IndexOutOfBoundsException if the specified index is out of range.
     */

    private byte decodingByte(int index) {
        long byteToReturn = 0;
        for (int i = 0; i < Byte.SIZE; i++) {
            byteToReturn = ((decodeBitsForIndex(index * Byte.SIZE + i)) | (byteToReturn << 1));
        }
        return (byte) byteToReturn;
    }

    /**
     * Decodes the last 13 bytes of the encoded data.
     *
     * @throws IndexOutOfBoundsException if the encoded data does not contain at least 13 bytes.
     */
    private void decodeLastThirteenBytes() {
        for (int i = 1; i < RawMessage.LENGTH; i++) {
            messageDecoded[i] = decodingByte(i);
        }
    }


    /**
     * Checks if the sum of peak values is greater than twice the sum of valley values.
     *
     * @param actualSumOfPeak the sum of peak values.
     * @return true if the sum of peak values is greater than twice the sum of valley values, false otherwise.
     */

    private boolean sumOfPeaksGreater2SumOfValleys(int actualSumOfPeak) {
        return actualSumOfPeak >= 2 * sumOfValleys();
    }


    /**
     * @return the next {@link RawMessage} that is decoded from the input stream, or {@code null} if the end of the input
     * stream is reached or the message could not be decoded
     * @throws IOException if an I/O error occurs while reading the input stream
     */


    // the structure if not has been adopted here, because it helps us remove indentations.
    public RawMessage nextMessage() throws IOException {
        int sumIndexMinusOne = 0;
        int actualSumOfPeak  = sumOfPeaks(0);
        // Iterate through the power window while it is full
        for (; powerWindow.isFull();
            // Advance the power window and update the actual sum of peaks and the sum index
             sumIndexMinusOne = actualSumOfPeak, actualSumOfPeak = sumOfPeaks(1),
                     powerWindow.advance()) {
            // Check if a peak is found, if not continue to next iteration
            if (!isPeakFound(sumIndexMinusOne, actualSumOfPeak)) {
                continue;
            }
            // Check if the sum of peaks is greater than twice the sum of valleys, if not continue to next iteration
            if (!sumOfPeaksGreater2SumOfValleys(
                    actualSumOfPeak)) {
                continue;
            }

            // Decode the first byte of the message
            messageDecoded[0] = decodingByte(0);
            // Check if the message length is valid, if not continue to next iteration
            if (RawMessage.size(messageDecoded[0]) != RawMessage.LENGTH) {
                continue;
            }

            // Decode the last 13 bytes of the message
            decodeLastThirteenBytes(); // the last 13 bytes are decoded here.
            // Check if the message can be created from the power window data, if not continue to next iteration
            if (RawMessage.of(powerWindow.position() * NANOSEC_TO_SEC, messageDecoded) == null) {
                continue;
            }

            // Create a new RawMessage object and return it
            long position = powerWindow.position() * NANOSEC_TO_SEC;
            powerWindow.advanceBy(SAMPLE_WINDOW_SIZE);
            return new RawMessage(position, new ByteString(messageDecoded));
        }

        // If no message is found, return null
        return null;
    }


}

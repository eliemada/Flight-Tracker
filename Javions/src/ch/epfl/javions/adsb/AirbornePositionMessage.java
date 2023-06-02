package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.Units;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.Objects;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * @param timeStampNs the timestamp of the message in nanoseconds
 * @param icaoAddress the ICAO address of the aircraft
 * @param altitude the altitude of the aircraft in meters
 * @param parity the parity of the message
 * @param x the x coordinate of the aircraft in meters
 * @param y the y coordinate of the aircraft in meters
 * @author Elie BRUNO
 * Represents an ADS-B message.
 */

public record AirbornePositionMessage(long timeStampNs, IcaoAddress icaoAddress, double altitude, int parity,
                                      double x, double y) implements Message {


    // Constants for decoding the altitude information

    /**
     * The starting altitude value for the non-trivial case.
     */
    private final static int NON_TRIVIAL_ALTITUDE_START    = -1300;
    /**
     * The starting altitude value for the trivial case.
     */
    private final static int TRIVIAL_ALTITUDE_START        = -1000;
    /**
     * The size of the least significant bits for the non-trivial altitude case.
     */
    private final static int NON_TRIVIAL_ALTITUDE_LSB_SIZE = 3;

    /**
     * The start index of the least significant bits of the altitude, it is always 0 even if we are in the
     * case where Q = 1 or Q = 0
     */
    private final static int ALTITUDE_LSB_START_INDEX      = 0;
    /**
     * The size of the most significant bits for the non-trivial altitude case.
     */
    private final static int NON_TRIVIAL_ALTITUDE_MSB_SIZE = 9;
    /**
     * The size (in bits) of the latitude and longitude information in the ADS-B message payload.
     */
    private final static int LATITUDE_LONGITUDE_SIZE       = 17;
    /**
     * The index of the first bit of the altitude information in the ADS-B message payload.
     */
    private final static int ALTITUDE_START_INDEX          = 36;
    /**
     * The size of the altitude information (in bits) in the ADS-B message payload.
     */
    private final static int ALTITUDE_SIZE                 = 12;

    private final static int FORMAT_INDEX_START = 34;

    private final static int REFLECTION_VALUE       = 6;
    private final static int HUNDRED_MULTIPLES      = 100;
    private final static int FIVE_HUNDRED_MULTIPLES = 500;
    private final static int TWENTY_FIVE_MULTIPLES  = 25;

    private final static int PARITY_SIZE = 1;

    private final static int LONGITUDE_START_INDEX = 0;

    // This array helps us through the gray code decoding, here are all the shifts I need to do, for
    // example : the bit0 of the altitude before realigning, is D4, and it needs to go at the index 9, so
    // we shift by 9, and so on...
    private final static int[] SHIFTS = {4, 2, 0, 10, 8, 6, 5, 3, 1, 11, 9, 7};

    /**
     * The size of the least significant bits for the trivial altitude case.
     */
    private final static int TRIVIAL_ALTITUDE_LSB_SIZE = 4;
    /**
     * The size of the most significant bits for the trivial altitude case.
     */
    private final static int TRIVIAL_ALTITUDE_MSB_SIZE = 7;

    private final static int FOURTH_BIT_INDEX = 4;


    /**
     * Constructs an AirbornePositionMessage object
     *
     * @param timeStampNs the timestamp in nanoseconds
     * @param icaoAddress the address of the aircraft
     * @param altitude    the altitude of the aircraft in meters
     * @param parity      the parity bit of the message
     * @param x           the longitude of the aircraft
     * @param y           the latitude of the position of the aircraft
     */
    public AirbornePositionMessage {
        Objects.requireNonNull(icaoAddress, "icaoAddress must not be null");
        Preconditions.checkArgument(!(timeStampNs < 0));
        Preconditions.checkArgument(!(parity != 0 && parity != 1));
        Preconditions.checkArgument(!(x < 0 || x >= 1 || y < 0 || y >= 1));
    }


    /**
     * Extracts the 11 bits on the left of the altitude, excluding the 5th bit that is always 1 when
     * calling this method
     *
     * @param altitude a 12-bit integer representing the altitude
     * @return an 11-bit integer representing the altitude without the 5th bit
     */
    private static int extractingElevenBitsLeft(int altitude) {
        int msb =
                Bits.extractUInt(altitude, TRIVIAL_ALTITUDE_LSB_SIZE + 1, TRIVIAL_ALTITUDE_MSB_SIZE)
                << TRIVIAL_ALTITUDE_LSB_SIZE; // Note that we are doing TRIVIAL_ALTITUDE_LSB_SIZE + 1 as we
        // want to remove the unwanted bit.
        int lsb = Bits.extractUInt(altitude, ALTITUDE_LSB_START_INDEX, TRIVIAL_ALTITUDE_LSB_SIZE);
        // We are extracting the 4th first bits and the last 7th that we are merging back after so we get
        // the 11 bits integer we wanted!
        return (lsb | msb);
    }


    /**
     * Extracts the altitude information from the given ADS-B message payload.
     *
     * @param payload the ADS-B message payload
     * @return the altitude information
     */
    private static int getAltitude(long payload) {
        return Bits.extractUInt(payload, ALTITUDE_START_INDEX, ALTITUDE_SIZE);
    }

    /**
     * Calculates the altitude for the trivial case (4th bit is 1).
     *
     * @param elevenBitsLeft the eleven most significant bits of the altitude information
     * @return the calculated altitude
     */
    private static int processAltitudeTrivialCase(int elevenBitsLeft) {
        return TRIVIAL_ALTITUDE_START + elevenBitsLeft * TWENTY_FIVE_MULTIPLES;
    }

    /**
     * Realigns the altitude information bits to the proper indexes for decoding.
     *
     * @param input the 12-bit altitude information
     * @return the realigned altitude information
     */

    // Please note that the following method has been used for a modularity reason,
    // Finding a pattern for this specific case and looping through the indexes is also doable but in the
    // event that we change the shifting indexes the following code is editable with more ease.
    private static int realigningAltitude(int input) {
        int result = 0;
        for (int i : SHIFTS) {
            result = result << 1;
            result |= Bits.extractUInt(input, i, 1);
        }

        return result;
    }

    /**
     * Decodes the altitude information using the Gray code decoding algorithm.
     *
     * @param input the altitude information to decode
     * @param size  the size of the altitude information in bits
     * @return the decoded altitude information
     */
    private static int codeGrayDecoding(int input, int size) {
        int initialValue = input; // We need to store the input value, otherwise it causes error
        for (int i = 1; i < size; i++) {
            input ^= (initialValue >> i);
        }
        return input;
    }

    /**
     * Calculates the altitude for the non-trivial case (4th bit is 0).
     *
     * @param altitude the altitude information
     * @return the calculated altitude
     */

    private static double processNonTrivialAltitude(int altitude) {
        // Extract the groups of bits
        int leastSignificantBits = Bits.extractUInt(altitude, ALTITUDE_LSB_START_INDEX,
                                                    NON_TRIVIAL_ALTITUDE_LSB_SIZE);
        int mostSignificantBits = Bits.extractUInt(altitude, NON_TRIVIAL_ALTITUDE_LSB_SIZE,
                                                   NON_TRIVIAL_ALTITUDE_MSB_SIZE);

        //decoding the bits
        leastSignificantBits = codeGrayDecoding(leastSignificantBits, NON_TRIVIAL_ALTITUDE_LSB_SIZE);
        mostSignificantBits  = codeGrayDecoding(mostSignificantBits, NON_TRIVIAL_ALTITUDE_MSB_SIZE);

        // Apply the transformations
        switch (leastSignificantBits) {
            case 0, 5, 6 -> {
                return Double.NaN; // Sets a Flag that tells us the altitude is invalid
            }
            case 7 -> {
                leastSignificantBits = 5;
            }
        }
        if ((mostSignificantBits & 1) == 1) { // Checks if the MSB is odd, using bit manipulation
            leastSignificantBits = (REFLECTION_VALUE - leastSignificantBits);
        }
        return NON_TRIVIAL_ALTITUDE_START +
               leastSignificantBits * HUNDRED_MULTIPLES +
               FIVE_HUNDRED_MULTIPLES * mostSignificantBits;
    }


    /**
     * Constructs an AirbornePositionMessage from a RawMessage.
     *
     * @param rawMessage the raw message to process
     * @return an AirbornePositionMessage or null if altitude is invalid
     */
    public static AirbornePositionMessage of(RawMessage rawMessage) {
        double altitude;
        long   payload = rawMessage.payload();
        // Using the testBit method to check that the 4th bit is a 1
        altitude = Bits.testBit(getAltitude(payload), FOURTH_BIT_INDEX) ?
                processAltitudeTrivialCase(extractingElevenBitsLeft(getAltitude(payload))) :
                processNonTrivialAltitude(realigningAltitude(getAltitude(payload)));

        if (Double.isNaN(altitude)) { // check if altitude is invalid by using NaN a s a sentinel value
            return null;
        }

        //the altitude calculations have been done in the metrics of the ADS-B protocol, we need to convert
        // to meters so we can use it in the rest of the program.
        altitude = Units.convert(altitude, Units.Length.FOOT, Units.Length.METER);

        // latitude are 17 bits long, and we want to normalize them so have them between 0 and 1,
        double longitude = Bits.extractUInt(payload, LONGITUDE_START_INDEX, LATITUDE_LONGITUDE_SIZE);
        // Normalizing the longitude and latitude to be between 0 and 1.
        double latitude = Bits.extractUInt(payload, LATITUDE_LONGITUDE_SIZE, LATITUDE_LONGITUDE_SIZE);

        longitude = normalizingLongitudeLatitude(longitude);
        latitude  = normalizingLongitudeLatitude(latitude);


        // Please note that we are multiplying by 2^-LATITUDE_LONGITUDE_SIZE, because the longitude and
        return new AirbornePositionMessage(rawMessage.timeStampNs(), rawMessage.icaoAddress(), altitude,
                                           Bits.extractUInt(payload, FORMAT_INDEX_START, PARITY_SIZE),
                                           longitude, latitude);
    }

    private static double normalizingLongitudeLatitude(double value) {
        return Math.scalb(value, -LATITUDE_LONGITUDE_SIZE);
    }

}


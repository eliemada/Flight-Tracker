package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.aircraft.IcaoAddress;
import java.util.Objects;

/**
 * @author Elie BRUNO
 * The AircraftIdentificationMessage class represents an aircraft identification message.
 *
 * @param timeStampNs the timestamp in nanoseconds
 * @param icaoAddress  the ICAO address of the aircraft
 * @param category    the category of the message
 * @param callSign   the call sign of the aircraft
 */
public record AircraftIdentificationMessage(long timeStampNs, IcaoAddress icaoAddress, int category,
                                            CallSign callSign) implements Message {
    private final static int CALLSIGN_END_INDEX         = 47;
    private final static int CA_START_INDEX             = 48;
    private final static int CA_SIZE                    = 3;
    private final static int CALLSIGN_CHARACTER_SIZE    = 6;
    private final static int ALPHABET_LAST_INDEX        = 26;
    private final static int ALPHABET_START_INDEX       = 1;
    private final static int DECIMAL_NUMBER_START_INDEX = 48;
    private final static int DECIMAL_NUMBER_END_INDEX   = 57;
    private final static int SPACE_INTEGER_VALUE        = 32;
    private static final int TYPECODE_CORRECTION_VALUE  = 14;

    private static final int CA_SIZE_IN_CATEGORY = 4;


    /**
     * Constructs a new aircraft identification message with the given timestamp, ICAO address, category, and call sign.
     *
     * @param timeStampNs the timestamp in nanoseconds
     * @param icaoAddress the ICAO address of the aircraft
     * @param category    the category of the message
     * @param callSign    the call sign of the aircraft
     */
    public AircraftIdentificationMessage {
        Objects.requireNonNull(icaoAddress, "icaoAddress cannot be null");
        Objects.requireNonNull(callSign, "callSign cannot be null");
        Preconditions.checkArgument(timeStampNs >= 0);
    }

    /**
     * Decodes a six-bit integer into its corresponding character according to ADS-B message encoding.
     *
     * @param sixBitInt the six-bit integer to decode
     * @return the corresponding character, or null if the integer is not valid
     */
    private static Character decodeSixBitInt(int sixBitInt) {

        // Map the input integer to the corresponding character
        if (sixBitInt <= ALPHABET_LAST_INDEX && sixBitInt >= ALPHABET_START_INDEX) {
            //remapping of the integer values by the constraints we gave us.
            return (char) ('A' + sixBitInt - ALPHABET_START_INDEX);

        }
        if (sixBitInt <= DECIMAL_NUMBER_END_INDEX
                 && sixBitInt >= DECIMAL_NUMBER_START_INDEX) { //If the bits are between those value, we just
            // convert the integer value to ascii as they are already properly mapped.
            return (char) sixBitInt;
        }
        if (sixBitInt == SPACE_INTEGER_VALUE) {
            //If the bits are between those value, we just
            // convert the integer value to ascii as they are already properly mapped.
            return  (char) sixBitInt;
        }

        return null;
        // Note that Singleton ifs have been used instead of ELIF as they are a bit more efficient if the
        // value is encountered, benchmarks have been done, the improvements are in nanoseconds but still
        // worth it :)
    }

    /**
     * Decodes the call sign from the ADS-B message payload.
     *
     * @param payload the message payload to decode
     * @return the decoded call sign, or null if decoding fails
     */
    private static CallSign decodingCallsign(long payload) {
        String callSign = "";
        for (int i = 0; i <= CALLSIGN_END_INDEX; i += CALLSIGN_CHARACTER_SIZE) {
            Character character = decodeSixBitInt(Bits.extractUInt(payload, i, CALLSIGN_CHARACTER_SIZE));

            if (character != null) {
                callSign = character + callSign;
            }
            else {
                return null;
            }
        }
        return new CallSign(callSign.stripTrailing()); // Method to remove the trailing whitespace
    }

    public static AircraftIdentificationMessage of(RawMessage rawMessage) {
        int      typecode = rawMessage.typeCode();
        long     payload  = rawMessage.payload();
        CallSign callSign = decodingCallsign(payload);
        int category = (
                (TYPECODE_CORRECTION_VALUE - typecode) << CA_SIZE_IN_CATEGORY | (
                        Bits.extractUInt(payload,
                                         CA_START_INDEX, CA_SIZE)
                )
        ); // Need to shift by 4, so we merge the bits together.

        if (callSign == null) { // checking if the callSign is null, if so, we return null
            return null;
        }
        else {
            return (
                    new AircraftIdentificationMessage(rawMessage.timeStampNs(),
                                                      rawMessage.icaoAddress(),
                                                      category, callSign)
            );
        }
        // The Type Code checks are done later on, we are not supposed to check them here
    }
}


package ch.epfl.javions.adsb;


/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The MessageParser class is used to parse a raw message into an instance of a message.
 */
public final class MessageParser {
    private final static int IDENTIFICATION_TYPE_CODE = 19;

    /**
     * The length of a raw message, in bytes.
     */
    private MessageParser() {
        // private constructor to prevent instantiation
    }

    /**
     * Depending on the values of the typecode, we are returning different instances of a message.
     *
     * @param rawMessage the raw message
     * @return the instance of the message with the appropriate class.
     */
    public static Message parse(RawMessage rawMessage) {
        switch (rawMessage.typeCode()) {
            case 1, 2, 3, 4 -> { // Listing all the possible values of an IdentificationMessage typecode
                return AircraftIdentificationMessage.of(rawMessage);
            }

            case IDENTIFICATION_TYPE_CODE -> {
                return AirborneVelocityMessage.of(rawMessage);
            }
            case 9,10,11,12,13,14,15,16,17,18,20,21,22->{ // Listing all the possible values of the
                // typecode as there is not that many numbers, and it allows us to make a switch / case
                // instead of an if
                return AirbornePositionMessage.of(rawMessage);

            }
            default -> {
                // If the typecode is not matching the cases above then we return null.
                return null;
            }
        }
    }
}
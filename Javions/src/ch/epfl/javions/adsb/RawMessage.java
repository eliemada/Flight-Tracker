package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.ByteString;
import ch.epfl.javions.Crc24;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.HexFormat;


/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The RawMessage class represents a raw ADS-B message.
 * @param timeStampNs the timestamp, in nanoseconds
 * @param bytes the raw message bytes
 */
public record RawMessage(long timeStampNs, ByteString bytes) {

    /**
     * The length of a raw message, in bytes.
     */
    public static final int LENGTH = 14;

    private static final int DF_CA_INDEX = 0;

    /**
     * The starting index of the downlink format in a message.
     */
    private static final int DF_START_INDEX = 3;

    /**
     * The length of the downlink format in a message.
     */
    private static final int DF_LENGTH = 5;

    /**
     * The length of the ICAO address in a message.
     */
    private static final int ICAO_ADDRESS_LENGTH = 6;

    /**
     * The size of the ICAO address in bits.
     */
    private static final int ICAO_SIZE = 3;

    /**
     * The starting index of the ICAO address in a message.
     */
    private static final int ICAO_START_INDEX = 1;

    /**
     * The size of the downlink format in bits.
     */
    private static final int DOWNLINK_FORMAT_SIZE = 5;

    /**
     * The size of the typecode in bits.
     */
    private static final int TYPECODE_SIZE = 5;

    /**
     * The size of the payload in octets.
     */
    private static final int PAYLOAD_SIZE = 56;

    /**
     * The value of the downlink format.
     */
    private static final int DOWNLINK_FORMAT_VALUE = 17;

    /**
     * The starting index of the payload in a message.
     */
    private static final int PAYLOAD_START = 4;

    /**
     * The ending index of the payload in a message.
     */
    private static final int PAYLOAD_END = 10;

    /**
     * The Crc24 object used for error checking.
     */
    private static final Crc24 CRC = new Crc24(Crc24.GENERATOR);

    private final static HexFormat HEX_FORMAT = HexFormat.of().withUpperCase();


    /**
     * Creates a new RawMessage with the given timestamp and byte array.
     *
     * @param timeStampNs the timestamp, in nanoseconds
     * @param bytes       the raw message bytes
     * @throws IllegalArgumentException if the timestamp is negative or the byte array is not of the expected length
     */
    public RawMessage {
        Preconditions.checkArgument(timeStampNs >= 0);
        Preconditions.checkArgument(bytes.size() == LENGTH);
    }


    /**
     * Creates a new RawMessage with the given timestamp and byte array, if the message's CRC-24 checksum is valid.
     *
     * @param timeStampNs the timestamp, in nanoseconds
     * @param bytes       the raw message bytes
     * @return the new RawMessage, or null if the checksum is invalid
     */
    public static RawMessage of(long timeStampNs, byte[] bytes) {

        return (CRC.crc(bytes) != 0) ? null : new RawMessage(timeStampNs, new ByteString(bytes));
    }

    /**
     * We check if downlinkFormat is = 17
     * We are doing {@code Byte.SIZE - downlinkFormat_SIZE } because it allows us to extract the downlinkFormat which
     * is located on the 5 MSB, so the starting index will be 3, and size 5
     */
    public static int size(byte byte0) {
        return Bits.extractUInt(byte0, Byte.SIZE - DOWNLINK_FORMAT_SIZE,
                                DOWNLINK_FORMAT_SIZE) == DOWNLINK_FORMAT_VALUE ? LENGTH : 0;
    }

    /**
     * Extracts the type code from the given ADS-B payload.
     *
     * @param payload the message payload
     * @return the type code
     */
    public static int typeCode(long payload) {
        return Bits.extractUInt(payload, PAYLOAD_SIZE - TYPECODE_SIZE, DOWNLINK_FORMAT_SIZE);
    }

    /**
     * Gets the downlink format of the message.
     *
     * @return the downlink format
     */
    public int downLinkFormat() {
        return Bits.extractUInt(bytes.byteAt(DF_CA_INDEX), DF_START_INDEX, DF_LENGTH);
    }

    /**
     * Returns the ICAO address extracted from the bytes using hexadecimal format.
     * The {@code ICAO_SIZE + 1} is added to the {@code toIndex} parameter of the {@code bytesInRange} method because
     * the method excludes the {@code toIndex} and the ICAO is 3 bytes long, so we add 1 to include all bytes.
     *
     * @return IcaoAddress object containing the ICAO address in hexadecimal format
     */
    public IcaoAddress icaoAddress() {
        return new IcaoAddress(
                HEX_FORMAT.toHexDigits((int) (
                                                                   bytes.bytesInRange(ICAO_START_INDEX,
                                                                                      ICAO_SIZE + 1)
                                                           ),
                                       ICAO_ADDRESS_LENGTH));

    }


    /**
     * @return the payload of the message.
     * The {@code PAYLOAD_END + 1} is added to the {@code toIndex} parameter of the {@code bytesInRange} method because
     * the method excludes the {@code toIndex} and the payload is 7 bytes long, so we add 1 to include all bytes.
     */
    public long payload() {
        return bytes.bytesInRange(PAYLOAD_START, PAYLOAD_END + 1);
    }


    /**
     * Returns the type code of the message.
     * @return the type code
     */
    public int typeCode() {
        return typeCode(payload());
    }


}
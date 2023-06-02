package ch.epfl.javions.adsb;

import ch.epfl.javions.Bits;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.Units;
import ch.epfl.javions.aircraft.IcaoAddress;

import java.util.Objects;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * Represents an airborne velocity message, which contains information about an aircraft's speed and track or heading.
 */
public record AirborneVelocityMessage(long timeStampNs, IcaoAddress icaoAddress,
                                      double speed, double trackOrHeading) implements Message {

    private static final int USEFUL_BITS_START_INDEX = 21;
    private static final int USEFUL_BITS_SIZE        = 22;

    private static final int    SUBTYPE_START_INDEX               = 48;
    private static final int    SUBTYPE_SIZE                      = 3;
    private static final int    DIRECTION_EAST_WEST_INDEX_START   = 21;
    private static final int    VELOCITY_EAST_WEST_INDEX_START    = 11;
    private static final int    DIRECTION_NORTH_SOUTH_INDEX_START = 10;
    private static final int    VELOCIES_SIZE                     = 10;
    private static final double UNITS_FOUR_KNOTS                  = 4 * Units.Speed.KNOT;
    private static final int    SEARCH_HEAD_START_INDEX           = 21;
    private static final int    HEAD_DIRECTION_GAP_SIZE           = 10;
    private static final int    HEAD_DIRECTION_GAP_START_INDEX    = 11;
    private static final int    AERIAL_SPEED_SIZE                 = 10;
    private static final int    AERIAL_SPEED_START_INDEX          = 0;


    /**
     * Creates a new instance of an airborne velocity message.
     *
     * @param timeStampNs    the timestamp of the message in nanoseconds
     * @param icaoAddress    the ICAO address of the aircraft
     * @param speed          the speed of the aircraft in knots
     * @param trackOrHeading the track or heading of the aircraft in radians
     */
    public AirborneVelocityMessage {
        Objects.requireNonNull(icaoAddress);
        Preconditions.checkArgument(timeStampNs >= 0);
        Preconditions.checkArgument(speed >= 0);
        Preconditions.checkArgument(trackOrHeading >= 0);
    }

    /**
     * Creates a new instance of an airborne velocity message from a raw message.
     *
     * @param usefulBits the useful bits of the raw message
     * @param subType    the subtype of the raw message
     * @param rawMessage the raw message
     * @return a new instance of an airborne velocity message when subtype is 1 or 2.
     */
    private static AirborneVelocityMessage groundSpeedCalculator(int usefulBits, int subType,
                                                                 RawMessage rawMessage) {
        double velocity;
        double trackOrHeading;
        int velocityNorthSouth =
                Bits.extractUInt(usefulBits, AERIAL_SPEED_START_INDEX, VELOCIES_SIZE) - 1;
        int velocityEastWest =
                Bits.extractUInt(usefulBits, VELOCITY_EAST_WEST_INDEX_START, VELOCIES_SIZE) - 1;
        velocity = Math.hypot(velocityEastWest, velocityNorthSouth);
        if (velocityEastWest == -1 || velocityNorthSouth == -1) {
            return null;
        }
        //Determining the velocity using Math.hypot
        velocity = Units.convertFrom(velocity, (subType == 1) ? Units.Speed.KNOT : UNITS_FOUR_KNOTS);
        // Using ternary operator to distinguish the cases where the typeCode is 1 or 2, as when it
        // is 2, the unit is FourKnots

        //Determining the sign of the velocityNorthSouth & velocityNorthSouth
        // depending on the sign of directionNorthSouth & directionEastWest
        velocityNorthSouth *= Bits.testBit(usefulBits, DIRECTION_NORTH_SOUTH_INDEX_START) ? -1 : 1;
        //If it is one than it means the plane is going from north
        // to south which is the opposite way.

        velocityEastWest *= Bits.testBit(usefulBits, DIRECTION_EAST_WEST_INDEX_START) ? -1 : 1;
        //If it is one than it means the plane is going from east
        // to west which is the opposite way.


        trackOrHeading = Math.atan2(velocityEastWest, velocityNorthSouth);
        if (trackOrHeading < 0) {
            trackOrHeading += Units.Angle.TURN;// If the angle is less than zero we add 2Pi to make it
            // greater than 0.
        }
        return new AirborneVelocityMessage(rawMessage.timeStampNs(), rawMessage.icaoAddress(), velocity,
                                           trackOrHeading);
    }

    /**
     * Creates a new instance of an airborne velocity message from a raw message.
     *
     * @param usefulBits the useful bits of the raw message
     * @param subType    the subtype of the raw message
     * @param rawMessage the raw message
     * @return a new instance of an airborne velocity message when subtype is 3 or 4.
     */
    private static AirborneVelocityMessage aerialSpeedCalculator(int usefulBits, int subType,
                                                                 RawMessage rawMessage) {
        if (Bits.testBit(usefulBits, SEARCH_HEAD_START_INDEX)) {
            int headDirectionGap = Bits.extractUInt(usefulBits,
                                                HEAD_DIRECTION_GAP_START_INDEX,
                                                HEAD_DIRECTION_GAP_SIZE);
            double trackOrHeading   = Units.convertFrom(Math.scalb(headDirectionGap,
                                                            -HEAD_DIRECTION_GAP_SIZE),
                                                 Units.Angle.TURN);
            // We are using Math.scalb(headDirectionGap, -HEAD_DIRECTION_GAP_SIZE) because the head
            // direction is ten bits long, and we want a value in turn so <1, this is why we divide
            // by 2^10
            double velocity = Bits.extractUInt(usefulBits, 0, AERIAL_SPEED_SIZE) - 1;
            if (velocity == -1) {
                return null;
            }
            velocity = Units.convertFrom(velocity, (subType == 3) ? Units.Speed.KNOT :
                    UNITS_FOUR_KNOTS);
            // Using ternary operator to distinguish the case where the typecode is 3 or 4, when it
            // is 4 then, the velocity is given in the unit FourKnots.
            return new AirborneVelocityMessage(rawMessage.timeStampNs(), rawMessage.icaoAddress(), velocity,
                                               trackOrHeading);
        }
        else {
            return null;
        }

    }


    /**
     * Creates a new instance of an airborne velocity message from a raw message.
     *
     * @param rawMessage the raw message to parse
     * @return the airborne velocity message created from the raw message, or null if the message could not be parsed
     */
    public static AirborneVelocityMessage of(RawMessage rawMessage) {
        long payload    = rawMessage.payload();
        int  subType    = Bits.extractUInt(payload, SUBTYPE_START_INDEX, SUBTYPE_SIZE);
        int  usefulBits = Bits.extractUInt(payload, USEFUL_BITS_START_INDEX, USEFUL_BITS_SIZE);
        switch (subType) {
            case 1, 2 -> {
                return groundSpeedCalculator(usefulBits, subType, rawMessage);
            }
            case 3, 4 -> {
                return aerialSpeedCalculator(usefulBits, subType, rawMessage);
            }
            default -> {
                return null;
            }
        }
    }
}



package ch.epfl.javions.adsb;

import ch.epfl.javions.GeoPos;

import java.util.Objects;


/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * A class that accumulates aircraft state information from ADS-B messages.
 *
 * @param <T> the type of the aircraft state setter that this accumulator is associated with
 */
public final class AircraftStateAccumulator<T extends AircraftStateSetter> {

    /**
     * The maximum difference between two timestamp values for two airborne position messages
     * to be considered valid for calculating a position.
     */
    private final static long MAX_TIMESTAMP_DIFFERENCE = 10_000_000_000L;

    private final T stateSetter;

    /**
     * An array that holds the two most recent airborne position messages.
     */
    private final AirbornePositionMessage[] previousAirbornePositionMessages = new AirbornePositionMessage[2];

    /**
     * Constructs an AircraftStateAccumulator object with the specified stateSetter object.
     *
     * @param stateSetter the state setter object to be associated with this accumulator
     * @throws NullPointerException if the stateSetter object is null
     */
    public AircraftStateAccumulator(T stateSetter) {
        this.stateSetter = Objects.requireNonNull(stateSetter);
        ;
    }

    /**
     * Returns the aircraft state setter object associated with this accumulator.
     *
     * @return the aircraft state setter object
     */
    public T stateSetter() {
        return stateSetter;
    }

    /**
     * Validates a position calculation using the two most recent airborne position messages
     * with the specified parity value.
     *
     * @param parity the parity value of the most recent airborne position message
     */
    private void validatingPosition(int parity) {
        // If the previous airborne position message with the opposite parity value is not null
        // and the difference between the two timestamps is less than or equal to the maximum
        // timestamp difference, then calculate the position using the two messages.
        // Note that, here, Math.abs(parity - 1) is used so we don't do multiple checks, the parity is
        // either 0 or 1 so using the absolute value ensures that.
        if (previousAirbornePositionMessages[Math.abs(parity - 1)] != null
            && Math.abs(previousAirbornePositionMessages[0].timeStampNs() -
                        previousAirbornePositionMessages[1].timeStampNs())
               <= MAX_TIMESTAMP_DIFFERENCE) {
            GeoPos geopos = CprDecoder.decodePosition(previousAirbornePositionMessages[0].x(),
                                                      previousAirbornePositionMessages[0].y(),
                                                      previousAirbornePositionMessages[1].x(),
                                                      previousAirbornePositionMessages[1].y(),
                                                      parity);
            if (geopos != null) {
                stateSetter.setPosition(geopos);
            }
        }
    }

    /**
     * Updates the aircraft state information based on the specified message.
     *
     * @param message the message to update the state information with
     */
    public void update(Message message) {
        stateSetter.setLastMessageTimeStampNs(message.timeStampNs());

        switch (message) {
            case AircraftIdentificationMessage aircraftIdentificationMessage -> {
                stateSetter.setCategory(aircraftIdentificationMessage.category());
                stateSetter.setCallSign(aircraftIdentificationMessage.callSign());
            }
            case AirbornePositionMessage airbornePositionMessage -> {
                stateSetter.setAltitude(airbornePositionMessage.altitude());
                // Storing at the index of the parity value, the current airborne position message
                previousAirbornePositionMessages[airbornePositionMessage.parity()] = airbornePositionMessage;
                validatingPosition(airbornePositionMessage.parity());
            }
            case AirborneVelocityMessage airborneVelocityMessage -> {
                stateSetter.setVelocity(airborneVelocityMessage.speed());
                stateSetter.setTrackOrHeading(airborneVelocityMessage.trackOrHeading());
            }
            default -> { //We should never be in this case, so we can throw a new exception
                throw new IllegalStateException("Unexpected value: " + message);
            }
        }
    }
}
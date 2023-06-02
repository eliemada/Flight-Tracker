package ch.epfl.javions.gui;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.adsb.AircraftStateSetter;
import ch.epfl.javions.adsb.CallSign;
import ch.epfl.javions.aircraft.AircraftData;
import ch.epfl.javions.aircraft.AircraftDatabase;
import ch.epfl.javions.aircraft.IcaoAddress;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;


/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The {@code ObservableAircraftState} class represents an observable state of an aircraft.
 * It implements the {@link AircraftStateSetter} interface to allow setting and getting the state of the aircraft.
 * It also provides JavaFX properties for each state variable to enable binding with JavaFX UI components.
 */
public final class ObservableAircraftState implements AircraftStateSetter {
    // Instance variables representing the state of an aircraft

    /**
     * The aircraft data.
     */
    private final AircraftData data;
    /**
     * The ICAO address of the aircraft.
     */
    private final IcaoAddress icaoAddress;
    /**
     * The call sign of the aircraft.
     */
    private final SimpleObjectProperty<CallSign> callSignProperty;
    /**
     * The category of the aircraft.
     */
    private final IntegerProperty categoryProperty;
    /**
     * The last message time stamp of the aircraft.
     */
    private final LongProperty lastMessageTimeStampNsProperty;
    /**
     * The position of the aircraft.
     */
    private final SimpleObjectProperty<GeoPos> positionProperty;
    /**
     * The altitude of the aircraft.
     */
    private final DoubleProperty altitudeProperty;
    /**
     * The velocity of the aircraft.
     */
    private final DoubleProperty velocityProperty;
    /**
     * The track or heading of the aircraft.
     */
    private final DoubleProperty trackOrHeadingProperty;
    /**
     * The trajectory of the aircraft.
     */
    private final ObservableList<AirbornePos> trajectory;
    /**
     * The unmodifiable trajectory of the aircraft.
     */
    private final ObservableList<AirbornePos> trajectoryUnmodifiable;
    /**
     * The last message time stamp of the aircraft.
     */
    private long lastMessageTimeStampNs;

    /**
     * Constructs an instance of the {@code ObservableAircraftState} class with the specified ICAO address and aircraft data.
     *
     * @param icaoAddress the ICAO address of the aircraft
     * @param data        the aircraft data
     */
    public ObservableAircraftState(IcaoAddress icaoAddress, AircraftData data) {
        this.data = data;
        this.icaoAddress = Objects.requireNonNull(icaoAddress);
        trajectory = FXCollections.observableArrayList();
        trajectoryUnmodifiable = FXCollections.unmodifiableObservableList(trajectory);
        callSignProperty = new SimpleObjectProperty<>();
        categoryProperty = new SimpleIntegerProperty();
        lastMessageTimeStampNsProperty = new SimpleLongProperty();
        positionProperty = new SimpleObjectProperty<>();
        altitudeProperty = new SimpleDoubleProperty(Double.NEGATIVE_INFINITY);
        velocityProperty = new SimpleDoubleProperty(Double.NEGATIVE_INFINITY);
        trackOrHeadingProperty = new SimpleDoubleProperty();
    }

    /**
     * Returns the aircraft data.
     *
     * @return the aircraft data
     */
    public AircraftData getData() {
        return data;
    }

    /**
     * Returns the ICAO address of the aircraft.
     *
     * @return the ICAO address of the aircraft
     */

    public IcaoAddress getIcaoAddress() {
        return icaoAddress;
    }

    /**
     * Returns the read-only integer property representing the category of the aircraft.
     *
     * @return the read-only integer property representing the category of the aircraft
     */

    public ReadOnlyIntegerProperty categoryProperty() {
        return categoryProperty;
    }

    /**
     * Returns the read-only long property representing the last message time stamp of the aircraft.
     *
     * @return the read-only long property representing the last message time stamp of the aircraft
     */
    public ReadOnlyLongProperty lastMessageTimeStampNsProperty() {
        return lastMessageTimeStampNsProperty;
    }

    /**
     * Returns the last message time stamp of the aircraft.
     *
     * @return the last message time stamp of the aircraft
     */
    public long getLastMessageTimeStampNs() {
        return lastMessageTimeStampNsProperty.get();
    }

    /**
     * Sets the last message time stamp of the aircraft.
     *
     * @param timeStampNs the timestamp in nanoseconds
     */
    @Override
    public void setLastMessageTimeStampNs(long timeStampNs) {
        lastMessageTimeStampNsProperty.set(timeStampNs);

    }

    /**
     * Returns the category of the aircraft.
     *
     * @return the category of the aircraft
     */
    public int getCategory() {
        return categoryProperty.get();
    }

    /**
     * Sets the category of the aircraft.
     *
     * @param category the category of the aircraft
     */
    @Override
    public void setCategory(int category) {
        categoryProperty.set(category);
    }

    /**
     * Returns the read-only object property representing the call sign of the aircraft.
     *
     * @return the read-only object property representing the call sign of the aircraft
     */

    public ReadOnlyObjectProperty<CallSign> callSignProperty() {
        return callSignProperty;
    }

    /**
     * Returns the call sign of the aircraft.
     *
     * @return the call sign of the aircraft
     */

    public CallSign getCallSign() {
        return callSignProperty.get();
    }

    /**
     * Sets the call sign of the aircraft.
     *
     * @param callSign the call sign of the aircraft
     */
    @Override
    public void setCallSign(CallSign callSign) {
        callSignProperty.set(callSign);
    }

    /**
     * Returns the read-only object property representing the position of the aircraft.
     *
     * @return the read-only object property representing the position of the aircraft
     */
    public ReadOnlyObjectProperty<GeoPos> positionProperty() {
        return positionProperty;
    }

    /**
     * Returns the position of the aircraft.
     *
     * @return the position of the aircraft
     */
    public GeoPos getPosition() {
        return positionProperty.get();
    }

    /**
     * Sets the position of the aircraft.
     *
     * @param position the geographic position of the aircraft
     */
    @Override
    public void setPosition(GeoPos position) {
        positionProperty.set(position);
        if (altitudeProperty.get() != Double.NEGATIVE_INFINITY) {
            trajectory.add(new AirbornePos(position, getAltitude()));
            lastMessageTimeStampNs = getLastMessageTimeStampNs();
        }
    }

    /**
     * Returns the read-only double property representing the altitude of the aircraft.
     *
     * @return the read-only double property representing the altitude of the aircraft
     */
    public ReadOnlyDoubleProperty altitudeProperty() {
        return altitudeProperty;
    }

    /**
     * Returns the altitude of the aircraft.
     *
     * @return the altitude of the aircraft
     */
    public double getAltitude() {
        return altitudeProperty.get();
    }

    /**
     * Sets the altitude of the aircraft.
     *
     * @param altitude the altitude of the aircraft in meters
     */
    @Override
    public void setAltitude(double altitude) {
        altitudeProperty.set(altitude);
        GeoPos currentPos = positionProperty.get();
        if (currentPos != null && altitude != Double.NEGATIVE_INFINITY) {
            if (trajectory.isEmpty()) {
                trajectory.add(new AirbornePos(currentPos, altitudeProperty.get()));
            } else if (getLastMessageTimeStampNs() == lastMessageTimeStampNs) {
                trajectory.set(trajectory.size() - 1,
                        new AirbornePos(currentPos, altitudeProperty.get()));
            }
        }
    }

    /**
     * Returns the read-only double property representing the velocity of the aircraft.
     *
     * @return the read-only double property representing the velocity of the aircraft
     */
    public double getVelocity() {
        return velocityProperty.get();
    }

    /**
     * Sets the velocity of the aircraft.
     *
     * @param velocity the velocity of the aircraft in meters per second
     */
    @Override
    public void setVelocity(double velocity) {
        velocityProperty.set(velocity);
    }

    /**
     * Returns the read-only double property representing the velocity of the aircraft.
     *
     * @return the read-only double property representing the velocity of the aircraft
     */
    public ReadOnlyDoubleProperty velocityProperty() {
        return velocityProperty;
    }

    /**
     * Returns the track or heading of the aircraft.
     *
     * @return the track or heading of the aircraft
     */
    public double getTrackOrHeading() {
        return trackOrHeadingProperty.get();
    }

    /**
     * Sets the track or heading of the aircraft.
     *
     * @param trackOrHeading the track or heading of the aircraft in degrees
     */
    @Override
    public void setTrackOrHeading(double trackOrHeading) {
        trackOrHeadingProperty.set(trackOrHeading);
    }

    /**
     * Returns the read-only double property representing the track or heading of the aircraft.
     *
     * @return the read-only double property representing the track or heading of the aircraft
     */
    public ReadOnlyDoubleProperty trackOrHeadingProperty() {
        return trackOrHeadingProperty;
    }

    /**
     * Returns the trajectory of the aircraft.
     *
     * @return the trajectory of the aircraft
     */
    public ObservableList<AirbornePos> getTrajectory() {
        return trajectoryUnmodifiable;
    }

    /**
     * Returns the read-only object property representing the trajectory of the aircraft.
     *
     * @param position the geographic position of the aircraft
     * @param altitude the altitude of the aircraft in meters
     */
    public record AirbornePos(GeoPos position, double altitude) {

    }

}

package ch.epfl.javions.adsb;


import ch.epfl.javions.GeoPos;


/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 The AircraftStateSetter interface defines the methods to set the state of an aircraft.
 */
public interface  AircraftStateSetter {

    /**

     Sets the timestamp of the last message received from the aircraft.
     @param timeStampNs the timestamp in nanoseconds
     */
    void setLastMessageTimeStampNs(long timeStampNs);
    /**

     Sets the category of the aircraft.
     @param category the category of the aircraft
     */
    void setCategory(int category);
    /**

     Sets the call sign of the aircraft.
     @param callSign the call sign of the aircraft
     */
    void setCallSign(CallSign callSign);
    /**

     Sets the position of the aircraft.
     @param position the geographic position of the aircraft
     */
    void setPosition(GeoPos position);
    /**

     Sets the altitude of the aircraft.
     @param altitude the altitude of the aircraft in meters
     */
    public abstract void setAltitude(double altitude);
    /**

     Sets the velocity of the aircraft.
     @param velocity the velocity of the aircraft in meters per second
     */
    void setVelocity(double velocity);
    /**

     Sets the track or heading of the aircraft.
     @param trackOrHeading the track or heading of the aircraft in degrees
     */
    void setTrackOrHeading(double trackOrHeading);
}
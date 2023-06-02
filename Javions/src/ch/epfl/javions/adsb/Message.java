package ch.epfl.javions.adsb;


import ch.epfl.javions.aircraft.IcaoAddress;


/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 Represents an abstract message with a timestamp and an ICAO address.
 */
public interface Message {
    /**
     *

     *  @return the timestamp of this message in nanoseconds.
     *
     */
    public abstract long timeStampNs();

    /**
     *
     *@return the ICAO address associated with this message.
     *
     */
    public abstract IcaoAddress icaoAddress();
}


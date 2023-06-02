package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;
/** @author Elie BRUNO (elie.bruno@epfl.ch)**/


/**

 The AircraftRegistration class represents an aircraft's registration.
 This class is immutable and is represented by a single string value.
 */
public record AircraftRegistration(String string) {
    /**
     * The pattern used to validate aircraft registration strings.
     */
    private static final Pattern OACI_PATTERN = Pattern.compile("[A-Z0-9 .?/_+-]+");

    /**
     * Constructs a new AircraftRegistration object with the given string value.
     *
     * @param string the string value of the aircraft registration
     * @throws IllegalArgumentException if the given string is not a valid aircraft registration
     */
    public AircraftRegistration {
        Preconditions.checkArgument(OACI_PATTERN.matcher(string).matches());
    }

}

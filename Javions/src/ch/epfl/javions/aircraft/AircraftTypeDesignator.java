package ch.epfl.javions.aircraft;


import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The AircraftTypeDesignator class represents an aircraft's type designator.
 * This class is immutable and is represented by a single string value.
 */
public record AircraftTypeDesignator(String string) {
    /**
     * The pattern used to validate aircraft type designator strings.
     */
    private static final Pattern OACI_PATTERN = Pattern.compile("[A-Z0-9]{2,4}");

    /**
     * Constructs a new AircraftTypeDesignator object with the given string value.
     *
     * @param string the string value of the aircraft type designator
     * @throws IllegalArgumentException if the given string is not a valid aircraft type designator
     */
    public AircraftTypeDesignator {
        Preconditions.checkArgument(string.isEmpty() || OACI_PATTERN.matcher(string).matches());

    }

}

package ch.epfl.javions.aircraft;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The AircraftDescription class represents an aircraft's description.
 * This class is immutable and is represented by a single string value.
 */
public record AircraftDescription(String string) {
    /**
     * The pattern used to validate aircraft description strings.
     */
    private static final Pattern OACI_PATTERN = Pattern.compile("[ABDGHLPRSTV-][0123468][EJPT-]");

    /**
     * Constructs a new AircraftDescription object with the given string value.
     *
     * @param string the string value of the aircraft description
     * @throws IllegalArgumentException if the given string is not a valid aircraft description
     */
    public AircraftDescription {
        Preconditions.checkArgument(string.isEmpty() || OACI_PATTERN.matcher(string).matches());
    }
}

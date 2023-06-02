package ch.epfl.javions.adsb;

import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;


/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 The CallSign class represents an aircraft's call sign.
 This class is immutable and is represented by a single string value.
 */

public record CallSign(String string) {
    /**
     * The pattern used to validate call sign strings.
     */
    private static final Pattern CALLSIGN_PATTERN = Pattern.compile("[A-Z0-9 ]{0,8}");

    /**
     * Constructs a new CallSign object with the given string value.
     *
     * @param string the string value of the call sign
     * @throws IllegalArgumentException if the given string is not a valid call sign
     */
    public CallSign {
        Preconditions.checkArgument(CALLSIGN_PATTERN.matcher(string).matches());
    }
}

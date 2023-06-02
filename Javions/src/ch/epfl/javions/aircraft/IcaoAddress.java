package ch.epfl.javions.aircraft;


import ch.epfl.javions.Preconditions;

import java.util.regex.Pattern;

/**
 @author Elie BRUNO (elie.bruno@epfl.ch)
 The IcaoAddress class represents an aircraft's International Civil Aviation Organization (ICAO) address.
 This class is immutable and is represented by a single string value.
 */
public record IcaoAddress(String string) {
    /**
     * The pattern used to validate ICAO addresses.
     */

    public static int ICAOADDRESS_LENGTH = 6;
    public static int ICAOADDRESS_INDEX_OF_LAST_2_CHARACTERS = 4;
    private static final Pattern OACI_PATTERN = Pattern.compile("[0-9A-F]{6}");

    /**
     * Constructs a new IcaoAddress object with the given string value.
     *
     * @param string the string value of the ICAO address
     * @throws IllegalArgumentException if the given string is not a valid ICAO address
     */
    public IcaoAddress {
        Preconditions.checkArgument(OACI_PATTERN.matcher(string).matches());
    }
}

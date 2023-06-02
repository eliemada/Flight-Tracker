package ch.epfl.javions.aircraft;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The WakeTurbulenceCategory enum represents the wake turbulence category of an aircraft.
 * This enum defines four possible categories: LIGHT, MEDIUM, HEAVY, and UNKNOWN.
 */

public enum WakeTurbulenceCategory {
    /**
     * Represents the wake turbulence category for a light turbulence.
     */
    LIGHT,
    /**
     * Represents the wake turbulence category for a medium turbulence.
     */
    MEDIUM,
    /**
     * Represents the wake turbulence category for a heavy turbulence.
     */
    HEAVY,
    /**
     * Represents the wake turbulence category for an unknown turbulence or too small.
     */
    UNKNOWN;


    /**
     * Returns the WakeTurbulenceCategory enum corresponding to the given string.
     *
     * @param s the string representation of the WakeTurbulenceCategory
     * @return the WakeTurbulenceCategory enum corresponding to the given string
     */

    public static WakeTurbulenceCategory of(String s) {
        return switch (s) {
            case "L" -> LIGHT;
            case "M" -> MEDIUM;
            case "H" -> HEAVY;
            default -> UNKNOWN;
        };

    }
}

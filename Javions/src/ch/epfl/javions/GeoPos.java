package ch.epfl.javions;

// XXX : In the following code, you will se that the convert method is used, and maybe convertTo or convertFrom
// should have been used, but this has been done on purpose because the small imprecisons were causing
// issues in the CPRDecoder, and the teacher informed us that the correcrion was using the convert method
// in the correction.

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * A geographic position represented by a pair of 32-bit integers representing longitude and latitude in t32 format.
 */
public record GeoPos(int longitudeT32, int latitudeT32) {

    /**
     * The minimum valid latitude in t32 format.
     */
    private static final double LATITUDE_RANGE_LOW = Math.scalb(-1d, 30);

    /**
     * The maximum valid latitude in t32 format.
     */
    private static final double LATITUDE_RANGE_HIGH = Math.scalb(1d, 30);

    /**
     * Constructs a new GeoPos object with the given longitude and latitude in t32 format.
     *
     * @param longitudeT32 the longitude in t32 format
     * @param latitudeT32 the latitude in t32 format
     * @throws IllegalArgumentException if the latitude is not within the valid range of [-2^30, 2^30]
     */
    public GeoPos {
        Preconditions.checkArgument(isValidLatitudeT32(latitudeT32));
    }

    /**
     * Returns true if the given latitude in t32 format is valid, i.e. within the range of [-2^30, 2^30].
     *
     * @param latitudeT32 the latitude in t32 format
     * @return true if the latitude is valid, false otherwise
     */
    public static boolean isValidLatitudeT32(int latitudeT32) {
        return (latitudeT32 >= LATITUDE_RANGE_LOW) && (latitudeT32 <= LATITUDE_RANGE_HIGH);
    }

    /**
     * Returns the longitude in radian for this GeoPos object.
     *
     * @return the longitude in radian
     */
    public double longitude() {
        return Units.convert(longitudeT32,Units.Angle.T32, Units.Angle.RADIAN);
    }

    /**
     * Returns the latitude in radian for this GeoPos object.
     *
     * @return the latitude in radian
     */
    public double latitude() {
        return Units.convert(latitudeT32,Units.Angle.T32, Units.Angle.RADIAN);
    }

    /**
     * Returns a string representation of this GeoPos object in the format "(longitude, latitude)" in degrees.
     *
     * @return a string representation of this GeoPos object
     */
    @Override
    public String toString() {
        return "(" + Units.convert(longitudeT32,Units.Angle.T32,Units.Angle.DEGREE) + "°, " + Units.convert(latitudeT32,
                Units.Angle.T32,Units.Angle.DEGREE) + "°)";
    }
}

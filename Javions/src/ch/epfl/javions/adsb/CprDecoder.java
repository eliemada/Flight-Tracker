package ch.epfl.javions.adsb;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.Units;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch) * This class contains a static method to decode geographical coordinates based on input parameters.
 * <p>
 * It returns a GeoPos object with the corresponding longitude and latitude values.
 */
public class CprDecoder {
    // Constants used in calculation
    private final static double NUMBER_OF_LATITUDE_ZONES_EVEN = 60;
    private final static double NUMBER_OF_LATITUDE_ZONES_ODD  = 59;
    private final static double WIDTH_OF_EVEN_ZONE            = 1 / NUMBER_OF_LATITUDE_ZONES_EVEN;
    private final static double WIDTH_OF_ODD_ZONE = 1 / NUMBER_OF_LATITUDE_ZONES_ODD;
    public static final double  RECENTERING_VALUE = 0.5;

    // Private constructor to prevent instantiation
    private CprDecoder() {
    }


    /**
     * @param input that is either a latitude or a longitude.
     * @return the input that has been :<ul>
     * <li>Recentered</li>
     * <li>Converted to {@code T32} so that the {@code Geopos Accepts it}</li>
     * </ul>
     */
    private static double fixingLatitudeLongitude(double input) {
        input = recenterAngle(input);
        input = Units.convert(input, Units.Angle.TURN, Units.Angle.T32);
        return Math.rint(input);
    }


    /**
     * Decodes geographical coordinates based on input parameters.
     *
     * @param x0         the x-coordinate of the even longitude of the zone.
     * @param y0         the y-coordinate of the even latitude of the zone.
     * @param x1         the x-coordinate of the odd longitude of the zone.
     * @param y1         the y-coordinate of the odd latitude of the zone.
     * @param mostRecent an integer indicating the most recent zone can take two values : 0 or 1.
     * @return a GeoPos object containing the decoded longitude and latitude values.
     */
    public static GeoPos decodePosition(double x0, double y0, double x1, double y1, int mostRecent) {
        Preconditions.checkArgument(mostRecent == 1 || mostRecent == 0);

        double zoneNumber = calculateZoneNumbers(y0, y1);
        int zoneIndexEven = calculateZoneIndex(zoneNumber, 0, NUMBER_OF_LATITUDE_ZONES_EVEN,
                                               NUMBER_OF_LATITUDE_ZONES_ODD);
        int zoneIndexOdd = calculateZoneIndex(zoneNumber, 1, NUMBER_OF_LATITUDE_ZONES_EVEN,
                                              NUMBER_OF_LATITUDE_ZONES_ODD);
        double latitudeEven = calculateLatitude(zoneIndexEven, y0,
                                                y1, 0);
        double latitudeOdd            = calculateLatitude(zoneIndexOdd, y0, y1, 1);
        double latitudeZoneEven       = calculateLatitudeZone(latitudeEven);
        double latitudeZoneEvenSecond = calculateLatitudeZone(latitudeOdd);

        // the following if is focused on computing the longitudes
        if (latitudeZoneEvenSecond == latitudeZoneEven) {
            // even if it is a rare case,
            // The previous formula can be calculated with two different latitudes.
            // If this results in two different values, it means that between
            // the two messages the aircraft
            // has changed its "latitude band" and therefore
            // its position cannot be determined and therefore returns null.

            boolean isMostRecent = mostRecent == 0; // boolean flag to know if the most recent is even or odd


            double latitudeZoneOdd = calculateLatitudeZoneOdd(latitudeZoneEven);
            double longitude; //we initialize the longitude here as it would have been a waste if the
            // latitude zones were not equal.
            if (latitudeZoneEven == 1) {
                // If we are in polar zones, then there is only one zone of longitude so the longitude is
                // either x0 or x1
                longitude = isMostRecent ? x0 : x1;
            }
            else {
                // Outside of Polar zones so when latitudeZoneOdd >1, we compute the longitude index zones
                double indexZoneLongitude =
                        calculateIndexZoneLongitude(x0, x1, latitudeZoneOdd, latitudeZoneEven);
                double indexZoneEvenOrOdd = calculateZoneIndex(indexZoneLongitude, mostRecent,
                                                               latitudeZoneEven, latitudeZoneOdd);
                //Once computer we can easily conclude with the longitude value.
                longitude = calculateLongitude(latitudeZoneEven, latitudeZoneOdd, indexZoneEvenOrOdd, x0, x1,
                                               mostRecent);

            }
            //assigning to the latitude attribute either the even or odd one depending on the mostrecent.
            double latitude = isMostRecent ? latitudeEven : latitudeOdd;
            latitude  = fixingLatitudeLongitude(latitude);
            longitude = fixingLatitudeLongitude(longitude);

            // If the latitude and longitude are valid, we return a GeoPos object with the corresponding values.
            // Otherwise, we return null.
            return GeoPos.isValidLatitudeT32((int) latitude)?
                    new GeoPos((int)longitude, (int) latitude):
                    null;
        }
        return null;
    }

    /**
     * /**
     * For those given parameters, we calculate the latitude number of zones
     *
     * @param x0               the x-coordinate of the even longitude of the zone.
     * @param x1               the x-coordinate of the odd longitude of the zone.
     * @param latitudeZoneOdd  latitude zone associated with {@code x1}
     * @param latitudeZoneEven latitude zone associated with {@code x0}
     * @return the indew zone of the longitude
     */
    private static double calculateIndexZoneLongitude(double x0, double x1, double latitudeZoneOdd,
                                                      double latitudeZoneEven) {
        return Math.rint(x0 * latitudeZoneOdd - x1 * latitudeZoneEven);
    }


    /**
     * Calculates the z value for the zone.
     *
     * @param latitude the latitude of the zone.
     * @return the z value for the zone.
     */
    private static double calculateLatitudeZone(double latitude) {
        double A = Math.acos
                (1 - (
                        (1 - Math.cos(2 * Math.PI * (WIDTH_OF_EVEN_ZONE)))
                        / (Math.pow(Math.cos(Units.convertFrom(latitude, Units.Angle.TURN)), 2))
                ));
        return Double.isNaN(A) ? 1 : Math.floor((Units.Angle.TURN / A));
    }

    /**
     * Calculates the z value for odd latitude zones based on the given even z value.
     *
     * @param zEven the z value for even latitude zones
     * @return the calculated z value for odd latitude zones
     */
    private static double calculateLatitudeZoneOdd(double zEven) {
        return zEven - 1;
    }

    /**
     * Calculates the z value based on the given x0, x1, zOdd, and zEven values.
     * It is the first thing to do while decoding positions.
     *
     * @param y0 the y coordinate for the even latitude
     * @param y1 the y coordinate for the odd latitude
     * @return the calculated zoneIndex value
     */
    private static double calculateZoneNumbers(double y0, double y1) {
        return Math.rint(y0 * NUMBER_OF_LATITUDE_ZONES_ODD - y1 * NUMBER_OF_LATITUDE_ZONES_EVEN);
    }

    /**
     * @param zoneIndex        the zone number value
     * @param mostRecent       either 1 or 0 depending on if the previous message was even or odd
     * @param latitudeZoneEven latitude zone associated with {@code x0}
     * @param latitudeZoneOdd  latitude zone associated with {@code x1}
     * @return the zoneIndex depending on the mostrecent value
     */
    private static int calculateZoneIndex(double zoneIndex, int mostRecent, double latitudeZoneEven,
                                          double latitudeZoneOdd) {
        if (zoneIndex < 0) {
            return (int) ((mostRecent == 0) ? zoneIndex + latitudeZoneEven : zoneIndex + latitudeZoneOdd);
        }
        else {
            return (int) zoneIndex;
        }
    }

    /**
     * Calculates the latitude based on the given latitudeZoneNumbers, y0, y1, zoneIndex,
     * mostRecent, zOdd, and zEven values.
     *
     * @param y0         the y coordinate value for the even latitude
     * @param y1         the y coordinate for the odd latitude
     * @param zoneIndex  the zone index
     * @param mostRecent the most recent zone index
     * @return the calculated latitude
     **/
    private static double calculateLatitude(double zoneIndex, double y0, double y1,
                                            int mostRecent) {
        return (mostRecent == 0) ?
                WIDTH_OF_EVEN_ZONE * (zoneIndex + y0) :
                WIDTH_OF_ODD_ZONE * (zoneIndex + y1);
    }

    /**
     * Calculates the longitude value using the provided parameters.
     *
     * @param x0         the x-coordinate value for the even longitude
     * @param x1         the x-coordinate value for the odd longitude
     * @param zEven      the calculated value for zEven
     * @param zoneIndex  the index of the current zone
     * @param zOdd       the calculated value for zOdd
     * @param mostRecent the flag to determine if the current zone is the most recent
     * @return the calculated longitude value
     **/
    private static double calculateLongitude(double zEven, double zOdd, double zoneIndex, double x0,
                                             double x1,
                                             int mostRecent) {
        return (mostRecent == 0) ? (1 / zEven) * (zoneIndex + x0) : (1 / zOdd) * (zoneIndex + x1);

    }

    /**
     * /**
     * <p>
     * Recenters the given angle value to be between -PI and PI.
     *
     * @param angle the angle value to be recentred
     * @return the recentred angle value
     **/
    private static double recenterAngle(double angle) {
        //if the angle is greater than 180 we subsctract 360 to get it's
        // negative equivalent value.
        return angle >= RECENTERING_VALUE? angle - 1 : angle;
    }
}

package ch.epfl.javions;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The WebMercator class provides static methods to convert geographic coordinates to Web Mercator projection coordinates,
 * which is commonly used in web mapping applications.
 * The class provides two methods for calculating the x and y coordinates in the projection.
 */
public final class WebMercator {
    private WebMercator() {
    }


    /**
     * Calculates the x coordinate of a point in the Web Mercator projection for a given zoom level and longitude.
     *
     * @param zoomLevel the zoom level of the map
     * @param longitude the longitude of the point
     * @return the x coordinate of the point in the Web Mercator projection
     */
    public static double x(int zoomLevel, double longitude) {
        return Math.scalb(1, 8 + zoomLevel) * (Units.convertTo(longitude, Units.Angle.TURN) + 0.5);
    }

    /**
     * Calculates the y coordinate of a point in the Web Mercator projection for a given zoom level and latitude.
     *
     * @param zoomLevel the zoom level of the map
     * @param latitude  the latitude of the point
     * @return the y coordinate of the point in the Web Mercator projection
     */
    public static double y(int zoomLevel, double latitude) {
        return Math.scalb(1, 8 + zoomLevel) * (
                -Units.convertTo(Math2.asinh(Math.tan(latitude)),
                                 Units.Angle.TURN) + 0.5
        );
    }
}

package ch.epfl.javions;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The Units class defines common units of measurement and provides methods to convert values between them.
 */
public final class Units {

    /**
     * Constant representing one-hundredth of a unit.
     */
    public static final double CENTI = 1E-2;

    /**
     * Constant representing one-thousand units.
     */
    public static final double KILO = 1E3;

    /**
     * Converts a value from one unit to another unit.
     *
     * @param value    The value to convert.
     * @param fromUnit The unit to convert from.
     * @param toUnit   The unit to convert to.
     * @return The converted value.
     */
    public static double convert(double value, double fromUnit, double toUnit) {

        return value * (fromUnit / toUnit);
    }

    /**
     * Converts a value from one unit to another unit.
     *
     * @param value    The value to convert.
     * @param fromUnit The unit to convert from.
     * @return The converted value.
     */
    public static double convertFrom(double value, double fromUnit) {
        return convert(value, fromUnit, 1);
    }

    public static double convertTo(double value, double toUnit) {
        return convert(value, 1, toUnit);
    }

    /**
     * The Angle class contains units of measurement for angles.
     */
    public static class Angle {
        /**
         * Constant representing the unit of measurement for radians.
         */
        public static final double RADIAN = 1;
        /**
         * Constant representing the unit of measurement for turns.
         */
        public static final double TURN = 2 * Math.PI * RADIAN;
        /**
         * Constant representing the unit of measurement for degrees.
         */
        public static final double DEGREE = TURN / 360;
        /**
         * Constant representing a 32-bit floating point representation of a full turn.
         */
        public static final double T32 = Math.scalb(TURN, -32);

        private Angle() {
            // Prevents instantiation.
        }
    }

    /**
     * The Length class contains units of measurement for lengths.
     */
    public static class Length {
        /**
         * Constant representing the unit of measurement for meters.
         */
        public static final double METER = 1;
        /**
         * Constant representing the unit of measurement for centimeters.
         */
        public static final double CENTIMETER = CENTI * METER;
        /**
         * Constant representing the unit of measurement for inches.
         */
        public static final double INCH = 2.54 * CENTIMETER;
        /**
         * Constant representing the unit of measurement for feet.
         */
        public static final double FOOT = 12 * INCH;
        /**
         * Constant representing the unit of measurement for kilometers.
         */
        public static final double KILOMETER = KILO * METER;
        /**
         * Constant representing the unit of measurement for nautical miles.
         */
        public static final double NAUTICAL_MILE = 1852 * METER;

        private Length() {
        }
    }

    /**
     * The Time class contains units of measurement for time.
     */
    public static class Time {
        /**
         * Constant representing the unit of measurement for seconds.
         */
        public static final double SECOND = 1;
        /**
         * Constant representing the unit of measurement for minutes.
         */
        public static final double MINUTE = 60 * SECOND;
        /**
         * Constant representing the unit of measurement for hours.
         */
        public static final double HOUR = 60 * MINUTE;

        private Time() {
        }
    }

    /**
     * The Speed class contains units of measurement for speed.
     */
    public static class Speed {
        /**
         * Constant representing the unit of measurement for kilometers per hour.
         */
        public static final double KILOMETER_PER_HOUR = Length.KILOMETER / Time.HOUR;
        /**
         * Constant representing the unit of measurement for knots.
         */
        public static final double KNOT = Length.NAUTICAL_MILE / Time.HOUR;


        private Speed() {
        }
    }
}


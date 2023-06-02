package ch.epfl.javions.gui;

import javafx.scene.paint.Color;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * Represents a color ramp.
 */
public final class ColorRamp {

    /**
     * The color ramp used by the plasma theme.
     */
    public static final ColorRamp PLASMA = new ColorRamp(
            Color.valueOf("0x0d0887ff"), Color.valueOf("0x220690ff"),
            Color.valueOf("0x320597ff"), Color.valueOf("0x40049dff"),
            Color.valueOf("0x4e02a2ff"), Color.valueOf("0x5b01a5ff"),
            Color.valueOf("0x6800a8ff"), Color.valueOf("0x7501a8ff"),
            Color.valueOf("0x8104a7ff"), Color.valueOf("0x8d0ba5ff"),
            Color.valueOf("0x9814a0ff"), Color.valueOf("0xa31d9aff"),
            Color.valueOf("0xad2693ff"), Color.valueOf("0xb6308bff"),
            Color.valueOf("0xbf3984ff"), Color.valueOf("0xc7427cff"),
            Color.valueOf("0xcf4c74ff"), Color.valueOf("0xd6556dff"),
            Color.valueOf("0xdd5e66ff"), Color.valueOf("0xe3685fff"),
            Color.valueOf("0xe97258ff"), Color.valueOf("0xee7c51ff"),
            Color.valueOf("0xf3874aff"), Color.valueOf("0xf79243ff"),
            Color.valueOf("0xfa9d3bff"), Color.valueOf("0xfca935ff"),
            Color.valueOf("0xfdb52eff"), Color.valueOf("0xfdc229ff"),
            Color.valueOf("0xfccf25ff"), Color.valueOf("0xf9dd24ff"),
            Color.valueOf("0xf5eb27ff"), Color.valueOf("0xf0f921ff"));
    private final       Color[]   colors;

    /**
     * Constructs a ColorRamp object with the specified colors.
     *
     * @param colors the colors
     */
    public ColorRamp(Color... colors) {
        if (colors.length < 2) {
            throw new IllegalArgumentException("At least two colors are required.");
        }
        else {
            this.colors = colors;
        }
    }

    /**
     * @param value the value
     * @return the color at the specified value
     */
    public Color at(double value) {
        if (value < 0) {
            return colors[0];
        }
        else if (value >= 1) {
            return colors[colors.length - 1];
        }
        else {
            int index = (int) (value * (colors.length - 1));
            return interpolateColors(value, colors[index], colors[index + 1], index);
        }
    }

    /**
     * Interpolates the specified colors.
     *
     * @param value       the value
     * @param firstColor  the first color
     * @param secondColor the second color
     * @param index       the index
     * @return the interpolated color
     */
    private Color interpolateColors(double value, Color firstColor, Color secondColor, int index) {
        double length = colors.length - 1,
                firstValue = index / length,
                secondValue = (index + 1) / length,
                distance = secondValue - firstValue,
                diff = value - firstValue,
                percentage = diff / distance;
        return firstColor.interpolate(secondColor, percentage);
    }
}

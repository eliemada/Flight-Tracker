package ch.epfl.javions.gui;

import ch.epfl.javions.Math2;
import ch.epfl.javions.Preconditions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * Manages the map parameters.
 */
public final class MapParameters {
    /**
     * The maximum zoom level for the map.
     */
    private static final int             MAX_ZOOM_LEVEL = 19;
    /**
     * The maximum zoom level for the map.
     */
    private static final int             MIN_ZOOM_LEVEL = 6;
    /**
     * The zoom level property.
     */
    private final        IntegerProperty zoom;
    /**
     * The minimum x coordinate property.
     */
    private final        DoubleProperty  minX;
    /**
     * The minimum y coordinate property.
     */
    private final        DoubleProperty  minY;

    /**
     * Constructs a new MapParameters instance.
     *
     * @param zoom The initial zoom level.
     * @param minX The initial minimum x coordinate.
     * @param minY The initial minimum y coordinate.
     * @throws IllegalArgumentException if zoom is not within the valid range.
     */
    public MapParameters(int zoom, double minX, double minY) throws IllegalArgumentException {
        Preconditions.checkArgument(zoom >= MIN_ZOOM_LEVEL);
        Preconditions.checkArgument(zoom <= MAX_ZOOM_LEVEL);
        this.zoom = new SimpleIntegerProperty(zoom);
        this.minX = new SimpleDoubleProperty(minX);
        this.minY = new SimpleDoubleProperty(minY);

    }

    /**
     * Returns the zoom level property.
     *
     * @return The zoom level property.
     */
    public IntegerProperty zoomProperty() {
        return this.zoom;
    }

    /**
     * Returns the minimum x coordinate property.
     *
     * @return The minimum x coordinate property.
     */
    public DoubleProperty minXProperty() {
        return this.minX;
    }

    /**
     * Returns the minimum y coordinate property.
     *
     * @return The minimum y coordinate property.
     */
    public DoubleProperty minYProperty() {
        return this.minY;
    }

    /**
     * Returns the current zoom level.
     *
     * @return The current zoom level.
     */
    public int getZoom() {
        return this.zoom.get();
    }

    /**
     * Returns the current minimum x coordinate.
     *
     * @return The current minimum x coordinate.
     */
    public double getMinX() {
        return this.minX.get();
    }

    /**
     * Returns the current minimum y coordinate.
     *
     * @return The current minimum y coordinate.
     */
    public double getMinY() {
        return this.minY.get();
    }

    /**
     * Scrolls the map by the specified amount in the x and y directions.
     *
     * @param deltaX The amount to scroll in the x direction.
     * @param deltaY The amount to scroll in the y direction.
     */
    public void scroll(double deltaX, double deltaY) {
        if (deltaX != 0) {
            this.minX.set(this.minX.get() + deltaX);
        }
        if (deltaY != 0) {
            this.minY.set(this.minY.get() + deltaY);
        }
    }

    /**
     * Changes the zoom level by the specified amount.
     *
     * @param deltaZoom The amount to change the zoom level by.
     */
    public void changeZoomLevel(int deltaZoom) {
        int newZoom = this.zoom.get() + deltaZoom;
        newZoom = Math2.clamp(MIN_ZOOM_LEVEL, newZoom, MAX_ZOOM_LEVEL);
        double factor = Math.scalb(1.0F, newZoom - this.zoom.get());
        this.minX.set(this.minX.get() * factor);
        this.minY.set(this.minY.get() * factor);
        this.zoom.set(newZoom);
    }
}

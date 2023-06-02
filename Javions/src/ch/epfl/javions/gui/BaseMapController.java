package ch.epfl.javions.gui;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.WebMercator;
import ch.epfl.javions.gui.TileManager.TileId;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * This class provides the functionality to manage the base map.
 * It is responsible for actions such as creating, manipulating, and drawing the map, as well as handling user input events.
 */

public final class BaseMapController {
    /**
     * The minimum time between two scrolls.
     */
    private static final int MIN_SCROLL_TIME = 200;

    /**
     * The pane.
     */
    private final Pane pane;
    /**
     * The tile manager.
     */
    private final TileManager tileManager;
    /**
     * The map parameters.
     */
    private final MapParameters parameters;
    /**
     * The canvas.
     */
    private final Canvas canvas;
    /**
     * The mouse position.
     */
    private final ObjectProperty<Point2D> mousePosition;
    /**
     * The minimum scroll time.
     */
    private final LongProperty minScrollTime;
    /**
     * The redraw if needed boolean flag.
     */
    private boolean redrawNeeded;

    /**
     * Constructs a BaseMapController object with the specified tile manager and map parameters.
     * Initializes the pane, the canvas, and binds the canvas size to the pane size.
     *
     * @param tileManager Tile manager responsible for providing tile images for drawing
     * @param parameters  Map parameters specifying the view parameters of the map
     */
    public BaseMapController(TileManager tileManager, MapParameters parameters) {
        this.tileManager = Objects.requireNonNull(tileManager);
        this.parameters = Objects.requireNonNull(parameters);
        this.canvas = new Canvas();
        this.pane = new Pane(this.canvas);
        this.canvas.widthProperty().bind(this.pane.widthProperty());
        this.canvas.heightProperty().bind(this.pane.heightProperty());
        this.redrawNeeded = true;
        this.minScrollTime = new SimpleLongProperty();
        this.mousePosition = new SimpleObjectProperty<>();
        this.initialize();
    }

    /**
     * @return The Pane that contains the map Canvas.
     */
    public Pane getPane() {
        return this.pane;
    }

    /**
     * Centers the map on a specified geographical point.
     *
     * @param point The geographical point to center the map on.
     */
    public void centerOn(GeoPos point) {
        this.parameters.scroll(WebMercator.x(this.parameters.getZoom(),
                        point.longitude()) - this.parameters.getMinX()
                        - (this.canvas.getWidth() / 2),
                WebMercator.y(this.parameters.getZoom(), point.latitude()) - this.parameters.getMinY() - (this.canvas.getHeight() / 2));
    }

    /**
     * Redraws the map if the redrawNeeded flag is set to true.
     */
    private void redrawIfNeeded() {
        if (this.redrawNeeded) {
            drawingOnCanvas();
        }
    }

    /**
     * Performs the actual drawing of the map on the Canvas.
     * Clears the Canvas and calculates the necessary tiles to draw based on the current map parameters.
     */
    private void drawingOnCanvas() {
        GraphicsContext graphicsContext = this.canvas.getGraphicsContext2D();
        this.redrawNeeded = false;
        graphicsContext.clearRect(0.0, 0.0, this.canvas.getWidth(), this.canvas.getHeight());
        int minX = (int) this.parameters.getMinX();
        int minY = (int) this.parameters.getMinY();
        int maxTileIndexX = (int) (this.canvas.getWidth() + (double) minX) / TileManager.TILE_SIZE;
        int maxTileIndexY = (int) (this.canvas.getHeight() + (double) minY) / TileManager.TILE_SIZE;
        int minTileIndexX = minX / TileManager.TILE_SIZE;
        int minTileIndexY = minY / TileManager.TILE_SIZE;

        for (int indexX = minTileIndexX; indexX <= maxTileIndexX; ++indexX) {
            for (int indexY = minTileIndexY; indexY <= maxTileIndexY; ++indexY) {
                TileManager.TileId id = new TileManager.TileId(this.parameters.getZoom(), indexX, indexY);
                if (TileId.isValid(id.zoomLevel(), id.x(), id.y())) {
                    try {
                        graphicsContext.drawImage(this.tileManager.imageForTileAt(id),
                                indexX * TileManager.TILE_SIZE - minX,
                                indexY * TileManager.TILE_SIZE - minY);
                    } catch (IOException e) {
                        throw new RuntimeException("Error drawing on canvas :" + id, e);
                    }
                }
            }
        }
    }

    /**
     * Sets the redrawNeeded flag to true and requests a new pulse (a frame in JavaFX).
     */
    private void redrawOnNextPulse() {
        this.redrawNeeded = true;
        Platform.requestNextPulse();
    }

    /**
     * Initializes event handlers and listeners for mouse actions and changes in map parameters or canvas size.
     */
    private void initialize() {
        this.canvas.setOnScroll(this::handleScroll);
        this.canvas.setOnMousePressed(this::handleMousePressed);
        this.canvas.setOnMouseDragged(this::handleMouseDragged);
        this.canvas.setOnMouseReleased(this::handleMouseReleased);

        this.canvas.sceneProperty().addListener((p, oldS, newS) -> {
            assert oldS == null;

            newS.addPreLayoutPulseListener(this::redrawIfNeeded);
        });
        this.parameters.minXProperty().addListener((observable, oldValue, newValue) -> {
            this.redrawOnNextPulse();
        });
        this.parameters.minYProperty().addListener((observable, oldValue, newValue) -> {
            this.redrawOnNextPulse();
        });
        this.parameters.zoomProperty().addListener((observable, oldValue, newValue) -> {
            this.redrawOnNextPulse();
        });
        this.canvas.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.redrawOnNextPulse();
        });
        this.canvas.heightProperty().addListener((observable, oldValue, newValue) -> {
            this.redrawOnNextPulse();
        });
    }


    /**
     * Handles a scroll event by calculating the zoom delta and executing a zoom if possible.
     *
     * @param event The scroll event triggered by the user.
     */
    private void handleScroll(ScrollEvent event) {
        int zoomDelta = (int) Math.signum(event.getDeltaY());
        if (zoomDelta != 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= this.minScrollTime.get()) {
                this.minScrollTime.set(currentTime + MIN_SCROLL_TIME);
                double dx = event.getX();
                double dy = event.getY();
                this.parameters.scroll(dx, dy);
                this.parameters.changeZoomLevel(zoomDelta);
                this.parameters.scroll(-dx, -dy);
            }
        }
    }

    /**
     * Handles a mouse press event by recording the current mouse position.
     *
     * @param event The mouse press event triggered by the user.
     */
    private void handleMousePressed(MouseEvent event) {
        this.mousePosition.set(new Point2D(event.getX(), event.getY()));
    }

    /**
     * Handles the mouse drag event.
     *
     * @param event the mouse drag event
     */
    private void handleMouseDragged(MouseEvent event) {
        Point2D eventPosition = new Point2D(event.getX(), event.getY());
        Point2D delta = this.mousePosition.get().subtract(eventPosition);
        this.parameters.scroll(delta.getX(), delta.getY());
        mousePosition.set(eventPosition);
    }

    /**
     * Handles the mouse release event.
     *
     * @param event the mouse release event
     */
    private void handleMouseReleased(MouseEvent event) {
        // Handling the mouse release event is not mandatory, but it is a good practice to do so.
        this.mousePosition.set(null);
    }
}

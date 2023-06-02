package ch.epfl.javions.gui;

import ch.epfl.javions.GeoPos;
import ch.epfl.javions.Preconditions;
import ch.epfl.javions.Units;
import ch.epfl.javions.WebMercator;
import ch.epfl.javions.aircraft.AircraftData;
import ch.epfl.javions.aircraft.AircraftDescription;
import ch.epfl.javions.aircraft.AircraftTypeDesignator;
import ch.epfl.javions.aircraft.WakeTurbulenceCategory;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

import static ch.epfl.javions.gui.ColorRamp.PLASMA;
import static javafx.scene.paint.CycleMethod.NO_CYCLE;


/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The AircraftController class manages the display of aircraft on the map.
 * It creates a Pane that holds all the aircraft's Group nodes, each group
 * consisting of a trajectory and a label and icon.
 */
public final class AircraftController {

    /**
     * Altitude above which aircraft labels become visible.
     */
    private static final int LABEL_VISIBLE_THRESHOLD = 11;

    /**
     * The maximum altitude that can be displayed on the map.
     */
    private static final int MAX_ALTITUDE = 12000;

    private static final double CUBE_ROOT_EXPONENT = 1.0 / 3.0;

    /**
     * The padding that needs to be added to the label's width and height.
     */
    private static final int LABEL_PADDING = 4;
    private final static AircraftTypeDesignator EMPTY_TYPE = new AircraftTypeDesignator("");
    private final static AircraftDescription EMPTY_DESCRIPTION = new AircraftDescription("");
    private final static WakeTurbulenceCategory EMPTY_WAKE_TURBULENCE = WakeTurbulenceCategory.of("");
    /**
     * The map parameters to use.
     */
    private final MapParameters mapParameters;
    /**
     * The currently selected aircraft state.
     */
    private final ObjectProperty<ObservableAircraftState> selectedAircraftProperty;
    /**
     * The pane that holds all the aircraft's Group nodes.
     */
    private final Pane mapPane;

    /**
     * Constructs an AircraftController with the specified map parameters and aircraft states.
     *
     * @param mapParameters            parameters for the map
     * @param aircraftStates           set of aircraft states to be displayed
     * @param selectedAircraftProperty currently selected aircraft state
     */
    public AircraftController(MapParameters mapParameters,
                              ObservableSet<ObservableAircraftState> aircraftStates,
                              ObjectProperty<ObservableAircraftState> selectedAircraftProperty) {
        Preconditions.checkArgument(aircraftStates.isEmpty());
        this.mapParameters = mapParameters;
        this.selectedAircraftProperty = selectedAircraftProperty;
        mapPane = new Pane();
        mapPane.setPickOnBounds(false);
        mapPane.getStylesheets().add("aircraft.css");
        initializeListeners(aircraftStates);
    }

    /**
     * Initializes listeners for the specified set of aircraft states.
     *
     * @param aircraftStates set of aircraft states to initialize listeners for
     */
    private void initializeListeners(ObservableSet<ObservableAircraftState> aircraftStates) {
        aircraftStates.addListener((SetChangeListener<ObservableAircraftState>) change -> {
            if (change.wasAdded()) {
                Group aircraft = aircraft(change.getElementAdded());
                mapPane.getChildren().add(aircraft);
            }
            if (change.wasRemoved()) {
                mapPane.getChildren().removeIf(
                        node -> node.getId().equals(
                                change.getElementRemoved().getIcaoAddress().string()));
            }
        });
    }

    /**
     * Creates a Group node for the specified aircraft state, which includes the aircraft's trajectory, label, and icon.
     *
     * @param aircraftState the aircraft state to display
     * @return a Group node representing the aircraft state
     */
    private Group aircraft(ObservableAircraftState aircraftState) {
        Group group = new Group(trajectory(aircraftState), labelAndIcon(aircraftState));
        group.viewOrderProperty().bind(aircraftState.altitudeProperty().negate());
        group.setId(aircraftState.getIcaoAddress().string());
        return group;
    }

    /**
     * Creates a Group node representing the trajectory of the specified aircraft state.
     *
     * @param aircraftState the aircraft state whose trajectory to display
     * @return a Group node representing the aircraft's trajectory
     */
    private Group trajectory(ObservableAircraftState aircraftState) {
        Group group = new Group();
        ObservableList<ObservableAircraftState.AirbornePos> trajectory =
                aircraftState.getTrajectory();
        bindingTrajectory(group, aircraftState);
        IntegerProperty zoom = mapParameters.zoomProperty();

        group.visibleProperty().addListener(
                (observable, oldValue, newValue) -> computeTrajectory(trajectory, group, zoom.get())
        );
        zoom.addListener(
                (observable, oldValue, newValue) -> computeTrajectory(trajectory, group, zoom.get())
        );
        trajectory.addListener((ListChangeListener<ObservableAircraftState.AirbornePos>) change ->
                computeTrajectory(trajectory, group, zoom.get()));

        return group;
    }

    /**
     * Binds the position of the trajectory group to the minimum values of the map parameters and sets the visibility
     * of the group depending on whether the associated aircraft is currently selected.
     *
     * @param group         The group to bind to the map parameters.
     * @param aircraftState The associated aircraft state.
     */
    private void bindingTrajectory(Group group, ObservableAircraftState aircraftState) {
        group.getStyleClass().add("trajectory");
        group.layoutXProperty().bind(mapParameters.minXProperty().negate());
        group.layoutYProperty().bind(mapParameters.minYProperty().negate());
        group.visibleProperty().bind(selectedAircraftProperty.isEqualTo(aircraftState));
    }

    /**
     * Computes the trajectory from the given airborne positions and adds the corresponding lines to the group.
     *
     * @param trajectory The list of airborne positions defining the trajectory.
     * @param group      The group to add the lines to.
     * @param zoom       The current zoom level.
     */
    private void computeTrajectory(ObservableList<ObservableAircraftState.AirbornePos> trajectory,
                                   Group group, int zoom) {
        group.getChildren().clear();
        for (int i = 0; i < trajectory.size() - 1; i++) {
            // Getting the two positions of the line
            GeoPos pos1 = trajectory.get(i).position();
            GeoPos pos2 = trajectory.get(i + 1).position();

            // Computation of the x and y coordinates of the line
            double x1 = WebMercator.x(zoom, pos1.longitude());
            double y1 = WebMercator.y(zoom, pos1.latitude());
            double x2 = WebMercator.x(zoom, pos2.longitude());
            double y2 = WebMercator.y(zoom, pos2.latitude());

            // Creation of the line
            Line line = new Line(x1, y1, x2, y2);

            double altitude1 = trajectory.get(i).altitude();
            double altitude2 = trajectory.get(i + 1).altitude();

            // Coloration of the line
            colorLine(line, altitude1, altitude2);

            // Adding the line to the group
            group.getChildren().add(line);
        }
    }

    /**
     * Colors the given line depending on the altitude of the aircraft at the two ends of the line.
     *
     * @return The color of the line.
     */
    public Pane pane() {
        return mapPane;
    }

    /**
     * Returns the string identifier for the given aircraft state.
     *
     * @param aircraftState The aircraft state to get the identifier for.
     * @return The identifier string.
     */
    private String returnAircraftString(ObservableAircraftState aircraftState) {
        AircraftData aircraftData = aircraftState.getData();
        if (!isAircraftDataNull(aircraftData)) {
            return (aircraftData.registration().string());
        } else if (aircraftState.getCallSign() != null) {
            return (aircraftState.getCallSign().string());
        } else {
            return (aircraftState.getIcaoAddress().string());
        }
    }

    /**
     * Creates a label for the given observable aircraft state object
     *
     * @param aircraftState the observable aircraft state object to create label for
     * @return a Group object containing the label for the aircraft
     */
    private Group label(ObservableAircraftState aircraftState) {
        Text text = new Text();
        Rectangle rectangle = new Rectangle();
        // Bind width and height of the rectangle to the size of the text
        rectangle.widthProperty().bind(
                text.layoutBoundsProperty().map(b -> b.getWidth() + LABEL_PADDING));
        rectangle.heightProperty().bind(
                text.layoutBoundsProperty().map(b -> b.getHeight() + LABEL_PADDING));
        // Bind text of the label to aircraft ID, velocity and altitude of the aircraft
        text.textProperty().bind(Bindings.createStringBinding(
                () -> String.format("%s\n%s km/h\u2002%s m", returnAircraftString(aircraftState),
                        getVelocityString(aircraftState),
                        getAltitudeString(aircraftState)),
                aircraftState.velocityProperty(), aircraftState.altitudeProperty(), aircraftState.callSignProperty()));

        Group group = new Group(rectangle, text);
        // Bind visibility of the label group to the zoom level and selected aircraft property
        group.visibleProperty().bind(mapParameters.zoomProperty().greaterThanOrEqualTo(
                LABEL_VISIBLE_THRESHOLD).or(
                selectedAircraftProperty.isEqualTo((aircraftState))));

        group.getStyleClass().add("label");
        return group;
    }


    private String getVelocityString(ObservableAircraftState aircraftState) {
        boolean isVelocityKnown = aircraftState.velocityProperty().get() > Double.NEGATIVE_INFINITY;
        return isVelocityKnown ? Integer.toString((int) Math.round(Units.convertTo(
                aircraftState.getVelocity(), Units.Speed.KILOMETER_PER_HOUR))) : "?";
    }

    /**
     * Note that <a href="https://edstem.org/eu/courses/237/discussion/34517">in this EDPost</a> the teacher affirms
     * that the altitude can not be unknown.
     * The following method has still been implemented to be consistent with the guidelines.
     *
     * @param aircraftState the observable aircraft state object to get the altitude from
     * @return the altitude of the aircraft if known, "?" otherwise
     */
    private String getAltitudeString(ObservableAircraftState aircraftState) {
        boolean isAltitudeKnown = aircraftState.altitudeProperty().get() > Double.NEGATIVE_INFINITY;
        return isAltitudeKnown ? Integer.toString((int) Math.rint(aircraftState.getAltitude())) : "?";
    }

    /**
     * Checks if the given aircraft data object is null or not
     *
     * @param aircraftData the aircraft data object to check for null
     * @return true if the given aircraft data object is null, false otherwise
     */
    private boolean isAircraftDataNull(AircraftData aircraftData) {
        return aircraftData == null;
    }

    /**
     * Creates an icon for the given observable aircraft state object
     *
     * @param aircraftState the observable aircraft state object to create icon for
     * @return an SVGPath object containing the icon for the aircraft
     */
    private SVGPath icon(ObservableAircraftState aircraftState) {
        AircraftData aircraftData = aircraftState.getData();
        ObjectProperty<AircraftIcon> icon = new SimpleObjectProperty<>();
        // Get the icon for the aircraft and bind it to the icon object property
        icon.bind(Bindings.createObjectBinding(() -> AircraftIcon.iconFor(isAircraftDataNull(aircraftData) ?
                        EMPTY_TYPE :
                        aircraftData.typeDesignator(),
                isAircraftDataNull(aircraftData) ?
                        EMPTY_DESCRIPTION :
                        aircraftData.description(),
                aircraftState.getCategory(),
                isAircraftDataNull(aircraftData) ?
                        EMPTY_WAKE_TURBULENCE :
                        aircraftData.wakeTurbulenceCategory()), aircraftState.categoryProperty()));

        return computeSvgPath(aircraftState, icon);
    }

    /**
     * Computes the SVG path for the given aircraft state and icon
     *
     * @param aircraftState the observable aircraft state object to compute the SVG path for
     * @param icon          the icon object property to compute the SVG path for
     * @return an SVGPath object containing the SVG path for the aircraft
     */
    private SVGPath computeSvgPath(ObservableAircraftState aircraftState, ObjectProperty<AircraftIcon> icon) {
        SVGPath svgPath = new SVGPath();
        svgPath.contentProperty().bind
                (Bindings.createStringBinding(() -> icon.get().svgPath(), icon));
        svgPath.rotateProperty().bind(Bindings.createDoubleBinding(
                () -> icon.get().canRotate() ? Units.convert(aircraftState.getTrackOrHeading(), Units.Angle.RADIAN,
                        Units.Angle.DEGREE) : 0.0,
                aircraftState.trackOrHeadingProperty(), icon, aircraftState.callSignProperty()));
        svgPath.fillProperty().bind(Bindings.createObjectBinding(() ->
                PLASMA.at(computeColor(aircraftState.getAltitude())), aircraftState.altitudeProperty()));
        svgPath.getStyleClass().add("aircraft");
        svgPath.setOnMouseClicked(event -> selectedAircraftProperty.set(aircraftState));

        return svgPath;
    }

    /**
     * Creates a group with a label displaying the aircraft's information and an icon indicating its type.
     *
     * @param aircraftState the aircraft state to display
     * @return a Group node containing the aircraft's label and icon
     */
    private Group labelAndIcon(ObservableAircraftState aircraftState) {
        Group labelAndIcon = new Group(label(aircraftState), icon(aircraftState));

        labelAndIcon.layoutXProperty().bind(Bindings.createDoubleBinding(
                () -> WebMercator.x(mapParameters.zoomProperty().get(),
                        aircraftState.positionProperty().get().longitude())
                        - mapParameters.getMinX(), mapParameters.minXProperty(),
                mapParameters.zoomProperty(), aircraftState.positionProperty()));

        labelAndIcon.layoutYProperty().bind(Bindings.createDoubleBinding(
                () -> WebMercator.y(mapParameters.zoomProperty().get(),
                        aircraftState.positionProperty().get().latitude())
                        - mapParameters.getMinY(), mapParameters.minYProperty(),
                mapParameters.zoomProperty(), aircraftState.positionProperty()));

        return labelAndIcon;
    }


    /**
     * Computes the color of the line depending on the altitude of the aircraft at the two ends of the line.
     *
     * @param altitude The altitude of the aircraft.
     * @return The color of the line.
     */
    private double computeColor(double altitude) {
        return Math.pow((altitude / MAX_ALTITUDE), CUBE_ROOT_EXPONENT);
    }

    /**
     * Colors the given line depending on the altitude of the aircraft at the two ends of the line.
     *
     * @param line      The line to color.
     * @param altitude1 The altitude of the aircraft at the first end of the line.
     * @param altitude2 The altitude of the aircraft at the second end of the line.
     */
    private void colorLine(Line line, double altitude1, double altitude2) {
        // Check if the altitude is a constant
        if (altitude1 - altitude2 != 0) {
            Stop[] stops = {new Stop(0, PLASMA.at(computeColor(altitude1))),
                    new Stop(1, PLASMA.at(computeColor(altitude2)))};
            line.setStroke(new LinearGradient(0, 0, 1, 0, true, NO_CYCLE,
                    stops));
        } else {
            line.setStroke(PLASMA.at(computeColor(altitude1)));
        }
    }

}


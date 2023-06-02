package ch.epfl.javions.gui;

import ch.epfl.javions.Units;
import ch.epfl.javions.adsb.CallSign;
import ch.epfl.javions.aircraft.AircraftData;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Class to control and manage a TableView for aircraft data.
 * This class helps to create and manage the aircraft table with different features like column resizing,
 * data formating, and selection listeners.
 *
 * @author Elie BRUNO (elie.bruno@epfl.ch) SCIPER : 355932
 */

public final class AircraftTableController {

    // Static String constants for column attributes.
    /**
     * Header for the ICAO column
     */
    private static final String ICAO_COLUMN_HEADER = "OACI";
    /**
     * Header for the call sign column
     */
    private static final String CALLSIGN_COLUMN_HEADER = "Indicatif";
    /**
     * Header for the registration column
     */
    private static final String REGISTRATION_COLUMN_HEADER = "Immatriculation";
    /**
     * Header for the model column
     */
    private static final String MODEL_COLUMN_HEADER = "Modèle";
    /**
     * Header for the type column
     */
    private static final String TYPE_COLUMN_HEADER = "Type";
    /**
     * Header for the description column
     */
    private static final String DESCRIPTION_COLUMN_HEADER = "Description";
    /**
     * Header for the latitude column
     */
    private static final String LATITUDE_COLUMN_HEADER = "Latitude (°)";
    /**
     * Header for the longitude column
     */
    private static final String LONGITUDE_COLUMN_HEADER = "Longitude (°)";
    /**
     * Header for the altitude column
     */
    private static final String ALTITUDE_COLUMN_HEADER = "Altitude (m)";
    /**
     * Header for the velocity column
     */
    private static final String VELOCITY_COLUMN_HEADER = "Vitesse (km/h)";
    /**
     * Number formatter to format the numeric values in table. This formatter is used to ensure
     * consistent number formatting across different numeric columns in the table.
     */
    private final static NumberFormat NUMBER_FORMATTER =
            NumberFormat.getInstance();

    /**
     * The numeric column prefered width
     */
    private static final int NUMERIC_COLUMN_WIDTH = 85;

    /**
     * Width of the ICAO column
     */
    private static final int OACI_COLUMN_WIDTH = 60;
    /**
     * Width of the call sign column
     */
    private static final int CALLSIGN_COLUMN_WIDTH = 70;
    /**
     * Width of the registration column
     */
    private static final int REGISTRATION_COLUMN_WIDTH = 90;
    /**
     * Width of the model column
     */
    private static final int MODEL_COLUMN_WIDTH = 230;
    /**
     * Width of the type column
     */
    private static final int TYPE_COLUMN_WIDTH = 50;
    /**
     * Width of the description column
     */
    private static final int DESCRIPTION_COLUMN_WIDTH = 70;
    private static final int POSITION_FORMAT_SIZE = 4;
    private static final int ALTITUDE_VELOCITY_FORMAT_SIZE = 0;
    private static final int DOUBLE_CLICK_COUNT = 2;
    /**
     * The table view that holds and displays the aircraft data.
     */
    private final TableView<ObservableAircraftState> aircraftTableView;

    /**
     * The set of ObservableAircraftState objects representing aircraft states.
     * This set is unmodifiable to prevent accidental modification.
     */
    private final ObservableSet<ObservableAircraftState> unmodifiableObservableAircraftStates;

    /**
     * The property holding the currently selected aircraft state in the table view.
     */
    private final ObjectProperty<ObservableAircraftState> selectedObservableAircraftState;

    /**
     * /**
     * Creates an instance of the AircraftTableController class.
     *
     * @param observableAircraftStates        The set of ObservableAircraftState objects to display in the table
     * @param selectedObservableAircraftState The currently selected ObservableAircraftState object
     */
    public AircraftTableController(ObservableSet<ObservableAircraftState> observableAircraftStates,
                                   ObjectProperty<ObservableAircraftState> selectedObservableAircraftState) {
        unmodifiableObservableAircraftStates =
                FXCollections.unmodifiableObservableSet(observableAircraftStates);
        this.selectedObservableAircraftState = selectedObservableAircraftState;
        aircraftTableView = new TableView<>();
        aircraftTableView.getStylesheets().add("table.css");
        aircraftTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_SUBSEQUENT_COLUMNS);
        aircraftTableView.setTableMenuButtonVisible(true);
        initializeListeners();
        buildTable();
    }

    /**
     * Method to initialize all listeners for the table view and the selection model.
     * This method ensures that changes in the data model or the selection model are correctly reflected in the table view.
     */
    private void initializeListeners() {
        unmodifiableObservableAircraftStates.addListener((SetChangeListener<ObservableAircraftState>) setChange -> {
            if (setChange.wasAdded()) {
                // Add the value to each column cell of the tableView
                aircraftTableView.getItems().add(setChange.getElementAdded());
            } else if (setChange.wasRemoved()) {
                aircraftTableView.getItems().remove(setChange.getElementRemoved());
            }
        });

        selectedObservableAircraftState.addListener((observable, oldValue, newSelectedObservableAircraftState) -> {
            assert (newSelectedObservableAircraftState != null && aircraftTableView.getItems().contains(newSelectedObservableAircraftState));
            {
                // this is a logic option that has been added such that if the aircraft that we click on is
                // already selected then, we do not need to scroll to it again
                if (!aircraftTableView.getSelectionModel().isSelected(aircraftTableView.getItems().indexOf(newSelectedObservableAircraftState))) {
                    aircraftTableView.getSelectionModel().select(newSelectedObservableAircraftState);
                    aircraftTableView.scrollTo(newSelectedObservableAircraftState);
                }
            }
        });

        aircraftTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedObservableAircraftState.set(newValue);
            }
        });
    }

    /**
     * @return The pane containing the table view of aircraft data.
     */
    public Node pane() {
        return aircraftTableView;
    }

    /**
     * Sets a double click event listener on the aircraftTableView that triggers a Consumer function with the selected
     * ObservableAircraftState as a parameter.
     *
     * @param onDoubleClick the Consumer function to be executed when a double click event occurs on the aircraftTableView
     */
    public void setOnDoubleClick(Consumer<ObservableAircraftState> onDoubleClick) {
        aircraftTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == DOUBLE_CLICK_COUNT &&
                    aircraftTableView.getSelectionModel().getSelectedItem() != null) {
                onDoubleClick.accept(aircraftTableView.getSelectionModel().getSelectedItem());
            }
        });
    }

    /**
     * Builds the aircraft table by creating columns with specific headers, widths, and cell values.
     * The columns include the ICAO address, callsign, registration, model, type designator, description,
     * latitude, longitude, altitude, and velocity of each aircraft.
     * Two different helper methods are used, one for the numeric columns and one for the string columns.
     * The numeric columns are formatted with a number formatter.
     */
    private void buildTable() {
        aircraftTableView.getColumns().setAll(List.of(
                createStringColumn(ICAO_COLUMN_HEADER, OACI_COLUMN_WIDTH,
                        f -> new ReadOnlyObjectWrapper<>(f.getIcaoAddress().string())),
                createStringColumn(CALLSIGN_COLUMN_HEADER, CALLSIGN_COLUMN_WIDTH,
                        f -> f.callSignProperty().map(CallSign::string)),
                createStringColumn(REGISTRATION_COLUMN_HEADER, REGISTRATION_COLUMN_WIDTH,
                        f -> new ReadOnlyObjectWrapper<>(f.getData()).map(p -> p.registration().string())),
                createStringColumn(MODEL_COLUMN_HEADER, MODEL_COLUMN_WIDTH,
                        f -> new ReadOnlyObjectWrapper<>(f.getData()).map(AircraftData::model)),
                createStringColumn(TYPE_COLUMN_HEADER, TYPE_COLUMN_WIDTH,
                        f -> new ReadOnlyObjectWrapper<>(f.getData()).map(p -> p.typeDesignator().string())),
                createStringColumn(DESCRIPTION_COLUMN_HEADER, DESCRIPTION_COLUMN_WIDTH,
                        f -> new ReadOnlyObjectWrapper<>(f.getData()).map(p -> p.description().string())),
                createNumericColumn(LATITUDE_COLUMN_HEADER,
                        f -> Bindings.createDoubleBinding(() ->
                                        Units.convertTo(f.positionProperty().get().latitude(), Units.Angle.DEGREE),
                                f.positionProperty()), POSITION_FORMAT_SIZE),
                createNumericColumn(LONGITUDE_COLUMN_HEADER,
                        f -> Bindings.createDoubleBinding(() ->
                                        Units.convertTo(f.positionProperty().get().longitude(),
                                                Units.Angle.DEGREE),
                                f.positionProperty()), POSITION_FORMAT_SIZE),
                createNumericColumn(ALTITUDE_COLUMN_HEADER,
                        f -> Bindings.createDoubleBinding(f::getAltitude, f.altitudeProperty()),
                        ALTITUDE_VELOCITY_FORMAT_SIZE
                ), createNumericColumn(VELOCITY_COLUMN_HEADER,
                        f -> Bindings.createDoubleBinding(() ->
                                        Units.convertTo(f.velocityProperty().get(), Units.Speed.KILOMETER_PER_HOUR),
                                f.velocityProperty()), ALTITUDE_VELOCITY_FORMAT_SIZE)
        ));
    }

    /**
     * Create a column with a string value
     *
     * @param name          the name of the column
     * @param preferedWidth the prefered width of the column
     * @param function      the function to get the value of the column, it goes form an ObservableAircraftState to a String
     *                      ObservableValue because we want to be able to update the value of the column when the value changes
     * @return the column that has been created
     */
    private TableColumn<ObservableAircraftState, String> createStringColumn(String name, double preferedWidth,
                                                                            Function<ObservableAircraftState,
                                                                                    ObservableValue<String>> function) {
        TableColumn<ObservableAircraftState, String> column = new TableColumn<>(name);
        column.setCellValueFactory(f -> function.apply(f.getValue()));
        column.setPrefWidth(preferedWidth);
        return column;
    }

    /**
     * Set the comparator of the column to sort the values in the right order
     *
     * @param name       the name of the column
     * @param function   the function to get the value of the column
     * @param formatSize the size of the format
     * @return the column that has been created
     */
    private TableColumn<ObservableAircraftState, String> createNumericColumn(String name,
                                                                             Function<ObservableAircraftState,
                                                                                     DoubleExpression> function,
                                                                             int formatSize) {
        TableColumn<ObservableAircraftState, String> column = new TableColumn<>(name);
        column.getStyleClass().add("numeric");
        column.setPrefWidth(NUMERIC_COLUMN_WIDTH);
        column.setCellValueFactory(f -> {
            setFormat(formatSize);
            DoubleExpression value = function.apply(f.getValue());
            // If the value is unknown, we display an empty string, otherwise we display the value
            return Bindings.when(value.greaterThan((long) Double.NEGATIVE_INFINITY)).
                    then(value.map(NUMBER_FORMATTER::format).getValue()).otherwise("");
        });
        setComparator(column);
        return column;
    }

    /**
     * Set the format of the column to display the values as integers or as doubles
     *
     * @param formatSize the size of the format
     */
    private void setFormat(int formatSize) {
        NUMBER_FORMATTER.setMinimumFractionDigits(formatSize);
        NUMBER_FORMATTER.setMaximumFractionDigits(formatSize);
    }

    /**
     * Set the comparator of the column to compare the values as numbers
     *
     * @param column the column to set the comparator
     */
    private void setComparator(TableColumn<ObservableAircraftState, String> column) {
        column.setComparator((s1, s2) -> {
            try {
                if (s1.isEmpty() || s2.isEmpty()) {
                    return s1.compareTo(s2);
                } else {
                    return Double.compare(NUMBER_FORMATTER.parse(s1).doubleValue(), NUMBER_FORMATTER.parse(s2).doubleValue());
                }
            } catch (ParseException e) {
                throw new Error(e);
            }
        });
        aircraftTableView.getColumns().add(column);
    }
}


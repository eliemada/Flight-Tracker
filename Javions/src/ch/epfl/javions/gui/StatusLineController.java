package ch.epfl.javions.gui;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch) SCIPER : 355932
 */

public final class StatusLineController {

    private final BorderPane pane;
    private final SimpleIntegerProperty aircraftCountProperty;
    private final LongProperty messageCountProperty;


    public StatusLineController() {
        aircraftCountProperty = new SimpleIntegerProperty();
        messageCountProperty = new SimpleLongProperty();
        pane = new BorderPane();
        pane.getStylesheets().add("status.css");


        Text aircraftCountText = new Text();
        aircraftCountText.textProperty().bind(aircraftCountProperty.asString("Aéronefs visibles : %d"));
        pane.setLeft(aircraftCountText);

        Text messageCountText = new Text();
        messageCountText.textProperty().bind(messageCountProperty.asString("Messages reçus : %d"));
         pane.setRight(messageCountText);
    }

    public BorderPane pane() {
        return pane;
    }

    public SimpleIntegerProperty aircraftCountProperty() {
        return aircraftCountProperty;
    }

    public LongProperty messageCountProperty() {
        return messageCountProperty;
    }
}

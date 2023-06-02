package ch.epfl.javions.gui;

import ch.epfl.javions.ByteString;
import ch.epfl.javions.adsb.Message;
import ch.epfl.javions.adsb.MessageParser;
import ch.epfl.javions.adsb.RawMessage;
import ch.epfl.javions.aircraft.AircraftDatabase;
import ch.epfl.javions.demodulation.AdsbDemodulator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch) SCIPER : 355932
 * The Main class contains the entry point of the program. It launches the application and initializes various constants.
 */
public final class Main extends Application {

    /**
     * Constant representing one million.
     */
    private static final double MILLION = 1e6;

    /**
     * Minimum width of the primary stage.
     */
    private static final int MIN_WIDTH = 800;

    /**
     * Minimum height of the primary stage.
     */
    private static final int MIN_HEIGHT = 600;

    /**
     * Minimum x value for the map parameters.
     */
    private static final int MAP_PARAMETERS_MIN_X = 33_530;

    /**
     * Minimum y value for the map parameters.
     */
    private static final int MAP_PARAMETERS_MIN_Y = 23_070;

    /**
     * The path name for the tile cache.
     */
    private static final String TILE_CACHE_PATH_NAME = "tile-cache";

    /**
     * The server URL.
     */
    private static final String SERVER_URL = "tile.openstreetmap.org";

    /**
     * The application name.
     */
    private static final String APP_NAME = "Javions";
    /**
     * The database name.
     */
    private static final String DATABASE_NAME = "/aircraft.zip";

    /**
     * The queue for storing messages.
     */
    private final ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<>();

    /**
     * The main method which launches the application.
     *
     * @param args The command line arguments.
     */

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * A helper method which reads all messages from a given file.
     *
     * @param fileName The name of the file.
     * @return A list of messages.
     */

    private static List<Message> readAllMessages(String fileName) {
        List<Message> rawMessages = new ArrayList<>();
        try (DataInputStream s = new DataInputStream(
                new FileInputStream(fileName))) {
            byte[] bytes = new byte[RawMessage.LENGTH];
            long timeStampNs;
            while (true) {
                timeStampNs = s.readLong();
                int bytesRead = s.readNBytes(bytes, 0, bytes.length);
                assert bytesRead == RawMessage.LENGTH;
                RawMessage rawMessage = new RawMessage(timeStampNs, new ByteString(bytes));
                Message message = MessageParser.parse(rawMessage);
                if (message != null) {
                    rawMessages.add(message);
                }
            }
        } catch (EOFException e) {
            // End of file reached
        } catch (IOException e) {
            throw new UncheckedIOException("Error while reading file " + fileName, e);
        }

        return rawMessages;
    }

    /**
     * The start method initializes the user interface and starts the timer for the aircraft animation.
     *
     * @param primaryStage The primary stage for the user interface.
     * @throws URISyntaxException If the database name is not a valid URI.
     */
    @Override
    public void start(Stage primaryStage) throws URISyntaxException {
        AircraftStateManager aircraftStateManager = new AircraftStateManager(getAircraftDatabase());
        StatusLineController statusLine = new StatusLineController();
        setPane(primaryStage, aircraftStateManager, statusLine);
        long startTime = System.nanoTime();

        Supplier<Message> messageSupplier;
        List<String> rawParameters = getParameters().getRaw();
        if (!rawParameters.isEmpty()) {
            messageSupplier = createFilesSupplier(rawParameters, startTime);
        } else {
            messageSupplier = createSystemInSupplier();
        }

        launchMessageHandlingThread(messageSupplier);
        startAircraftAnimationTimer(aircraftStateManager, statusLine);
    }


    /**
     * Sets up the user interface pane by calling helper methods to create its components.
     *
     * @param primaryStage         The primary stage for the user interface.
     * @param aircraftStateManager The aircraft state manager.
     * @param statusLineController The status line controller.
     */
    private void setPane(Stage primaryStage, AircraftStateManager aircraftStateManager,
                         StatusLineController statusLineController) {
        Path tileCache = Path.of(TILE_CACHE_PATH_NAME);
        TileManager tileManager =
                new TileManager(tileCache, SERVER_URL);
        MapParameters mapParameters =
                new MapParameters(8, MAP_PARAMETERS_MIN_X, MAP_PARAMETERS_MIN_Y);
        BaseMapController baseMapController = new BaseMapController(tileManager, mapParameters);
        statusLineController.aircraftCountProperty().bind(Bindings.size(aircraftStateManager.getKnownAircraftStates()));
        ObjectProperty<ObservableAircraftState> selectedAircraftProperty =
                new SimpleObjectProperty<>();
        AircraftController aircraftController =
                new AircraftController(mapParameters,
                        aircraftStateManager.getKnownAircraftStates(), selectedAircraftProperty);
        StackPane stackPane = new StackPane(baseMapController.getPane(), aircraftController.pane());
        AircraftTableController aircraftTableController =
                new AircraftTableController(aircraftStateManager.getKnownAircraftStates(), selectedAircraftProperty);
        aircraftTableController.setOnDoubleClick((ObservableAircraftState state) ->
                baseMapController.centerOn(state.positionProperty().get()));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(aircraftTableController.pane());
        borderPane.setTop(statusLineController.pane());

        configurePrimaryStage(primaryStage, stackPane, borderPane);
    }

    /**
     * Configures the primary stage for the user interface with the given components.
     *
     * @param primaryStage The primary stage.
     * @param stackPane    The stack pane containing the base map and aircraft controllers.
     * @param borderPane   The border pane containing the aircraft table and status line controllers.
     */
    private void configurePrimaryStage(Stage primaryStage, StackPane stackPane, BorderPane borderPane) {
        var root = new SplitPane(stackPane, borderPane);
        root.setOrientation(Orientation.VERTICAL);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle(APP_NAME);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();

    }


    /**
     * This method creates a Supplier of Message objects based on a list of raw parameters and a start time.
     * It reads all the messages from the file specified by the first element of the rawParameters list and returns
     * a lambda expression that returns each message one by one, pausing the execution when necessary to maintain the
     * correct timing between messages.
     *
     * @param rawParameters the list of raw parameters, where the first element is the file path
     * @param startTime     the start time in nanoseconds
     * @return a Supplier of Message objects
     */
    private Supplier<Message> createFilesSupplier(List<String> rawParameters, long startTime) {
        List<Message> messages = readAllMessages(rawParameters.get(0));
        Iterator<Message> messageIterator = messages.iterator();
        return () -> {
            try {
                // If there are no more messages, return null
                if (!messageIterator.hasNext()) {
                    return null;
                }
                Message message = messageIterator.next();
                long currentTime = System.nanoTime() - startTime;
                if (message.timeStampNs() - currentTime >= 0) {
                    Thread.sleep((long) ((message.timeStampNs() - currentTime) / MILLION));
                }
                return message;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * This method creates a Supplier of Message objects based on the standard input stream.
     * It reads all the messages from the input stream and returns a lambda expression that returns each message one by one.
     *
     * @return a Supplier of Message objects
     */
    public Supplier<Message> createSystemInSupplier() {
        AdsbDemodulator ad;
        try {
            ad = new AdsbDemodulator(System.in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return () -> {
            try {
                while (true) {
                    RawMessage rawMessage = ad.nextMessage();
                    if (rawMessage != null) {
                        Message message = MessageParser.parse(rawMessage);
                        if (message != null) {
                            return message;
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * This method starts the animation timer for the aircraft objects.
     * It creates a new AnimationTimer that updates the aircraftStateManager with the messages from the messages list.
     * The method is called at the start of the application and runs indefinitely until an IOException occurs.
     *
     * @param aircraftStateManager the AircraftStateManager object to update
     * @param statusLine           the StatusLineController object to update with the message count
     */
    private void startAircraftAnimationTimer(AircraftStateManager aircraftStateManager, StatusLineController statusLine) {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    while (!messages.isEmpty()) {
                        aircraftStateManager.updateWithMessage(messages.remove());
                        statusLine.messageCountProperty().set(statusLine.messageCountProperty().get() + 1);
                    }
                    aircraftStateManager.purge();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }.start();
    }

    /**
     * This method launches a new thread that consumes messages from the specified messageSupplier and adds them to the messages list.
     * It creates a new Thread that runs indefinitely until a null message is received from the messageSupplier.
     * The thread is set as a daemon thread and started.
     *
     * @param messageSupplier the Supplier of Message objects to consume messages from
     */
    private void launchMessageHandlingThread(Supplier<Message> messageSupplier) {
        Thread secondThread = new Thread(() -> {
            while (true) {
                Message m = messageSupplier.get();
                if (m != null) {
                    messages.add(m);
                }
            }
        });
        secondThread.setDaemon(true);
        secondThread.start();
    }


    /**
     * Retrieves an instance of an AircraftDatabase by loading a ZIP file from the resources directory of this class.
     *
     * @return an instance of AircraftDatabase loaded from the ZIP file.
     * @throws URISyntaxException if the URL of the ZIP file is not formatted correctly.
     */
    private AircraftDatabase getAircraftDatabase() throws URISyntaxException {
        URL dbUrl = getClass().getResource(DATABASE_NAME);
        assert dbUrl != null;
        String f = Path.of(dbUrl.toURI()).toString();
        return new AircraftDatabase(f);
    }


}




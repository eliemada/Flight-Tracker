package ch.epfl.javions.gui;

import ch.epfl.javions.Units;
import ch.epfl.javions.adsb.AircraftStateAccumulator;
import ch.epfl.javions.adsb.Message;
import ch.epfl.javions.aircraft.AircraftData;
import ch.epfl.javions.aircraft.AircraftDatabase;
import ch.epfl.javions.aircraft.IcaoAddress;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * Manages the state of all aircraft in the airspace.
 */
public final class AircraftStateManager {
    /**
     * The time in nanoseconds after which an aircraft is considered to have left the airspace.
     */
    private static final long MINUTE_IN_NS = (long) (Units.Time.MINUTE * 1E9);

    /**
     * The map of aircraft state accumulators, indexed by ICAO address.
     */
    private final Map<IcaoAddress, AircraftStateAccumulator<ObservableAircraftState>>
            aircraftStateAccumulators;

    /**
     * The set of all aircraft states that are currently known.
     */
    private final ObservableSet<ObservableAircraftState> knownAircraftStates;

    /**
     * The unmodifiable set of all aircraft states that are currently known.
     */
    private final ObservableSet<ObservableAircraftState> unmodifiableObservableSet;

    /**
     * The aircraft database.
     */
    private AircraftDatabase database;

    /**
     * The last time an aircraft state was updated
     */
    private long updateLastTime;


    /**
     * Constructs an AircraftStateManager object with the specified aircraft database.
     *
     * @param database The aircraft database, should be a non-null object containing all necessary aircraft data.
     * @throws NullPointerException if the specified database is null
     */
    public AircraftStateManager(AircraftDatabase database) {
        this.database = Objects.requireNonNull(database);
        aircraftStateAccumulators = new HashMap<>();
        knownAircraftStates = FXCollections.observableSet();
        unmodifiableObservableSet = FXCollections.unmodifiableObservableSet(knownAircraftStates);
    }

    /**
     * /**
     * Returns an unmodifiable set of all known aircraft states.
     *
     * @return An unmodifiable ObservableSet of ObservableAircraftState objects, each representing a known state
     * of an aircraft. If no aircraft states are currently known, an empty set is returned.
     */
    public ObservableSet<ObservableAircraftState> getKnownAircraftStates() {
        return unmodifiableObservableSet;
    }

    /**
     * Updates the state of the aircraft with the specified ICAO address with the specified message.
     * The aircraft state accumulator for the specific aircraft is fetched from the map or created if
     * it doesn't exist. The state is updated with the provided message and if the state has a position,
     * it's added to the set of known aircraft states.
     *
     * @param message The message to update the aircraft state with. It is an object containing
     *                an aircraft's ICAO address and a timestamp.
     * @throws IOException If an I/O error occurs during the execution of the method, such as when fetching
     *                     aircraft data from the database.
     */
    public void updateWithMessage(Message message) throws IOException {
        // Update the last update time.
        updateLastTime = message.timeStampNs();
        IcaoAddress icaoAddress = message.icaoAddress();
        // Get the aircraft state accumulator for the aircraft with the specified ICAO address.
        AircraftStateAccumulator<ObservableAircraftState> aircraftState =
                aircraftStateAccumulators.get(icaoAddress);
        // If the aircraft state accumulator is null, then create a new one and add it to the map.
        if (aircraftState == null) {
            AircraftData data = database.get(icaoAddress);
            aircraftState = new AircraftStateAccumulator<>(new ObservableAircraftState(icaoAddress, data));
            aircraftStateAccumulators.put(icaoAddress, aircraftState);
        }
        // Update the state with the message.
        aircraftState.update(message);
        // If the aircraft state has a position, then add it to the set of known aircraft states.
        if (aircraftState.stateSetter().getPosition() != null) {
            knownAircraftStates.add(aircraftState.stateSetter());
        }

    }

    /**
     * Removes all aircraft states that have not been updated for more than a minute.
     * It goes through each aircraft state accumulator and known aircraft state, and removes
     * those that have been last updated more than a minute ago.
     */
    public void purge() {
        aircraftStateAccumulators.values().removeIf(
                state -> updateLastTime - state.stateSetter().getLastMessageTimeStampNs() > MINUTE_IN_NS);
        knownAircraftStates.removeIf(state -> updateLastTime - state.getLastMessageTimeStampNs() > MINUTE_IN_NS);
    }
}
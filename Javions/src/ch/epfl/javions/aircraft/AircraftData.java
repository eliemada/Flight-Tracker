package ch.epfl.javions.aircraft;

import java.util.Objects;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * Represents data about an aircraft, including its registration, type designator, model, description, and wake turbulence category.
 * This class is implemented as a Java record, which provides a concise way to declare a class whose main purpose is to hold data.
 */
public record AircraftData(
        AircraftRegistration registration,
        AircraftTypeDesignator typeDesignator,
        String model,
        AircraftDescription description,
        WakeTurbulenceCategory wakeTurbulenceCategory) {

    /**
     * Constructs a new instance of the {@code AircraftData} class with the specified values for its properties.
     *
     * @param registration           the aircraft registration (must not be {@code null})
     * @param typeDesignator         the aircraft type designator (must not be {@code null})
     * @param model                  the aircraft model (must not be {@code null})
     * @param description            the aircraft description (must not be {@code null})
     * @param wakeTurbulenceCategory the aircraft wake turbulence category (must not be {@code null})
     * @throws NullPointerException if any of the arguments are {@code null}
     */
    public AircraftData {
        Objects.requireNonNull(registration, "registration cannot be null");
        Objects.requireNonNull(typeDesignator, "typeDesignator cannot be null");
        Objects.requireNonNull(model, "model cannot be null");
        Objects.requireNonNull(description, "description cannot be null");
        Objects.requireNonNull(wakeTurbulenceCategory, "wakeTurbulenceCategory cannot be null");
    }
}

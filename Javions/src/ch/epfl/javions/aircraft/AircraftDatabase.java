package ch.epfl.javions.aircraft;

import java.io.*;
import java.util.Objects;
import java.util.zip.ZipFile;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * A class representing an aircraft database that can retrieve {@link AircraftData} based on an {@link IcaoAddress}.
 */
public final class AircraftDatabase {

    private final static int ICAOADDRESS_START_INDEX = 0;


    private final static int AIRCRAFT_REGISTRATION_START_INDEX    = 1;
    private final static int AIRCRAFT_TYPE_DESIGNATOR_START_INDEX = 2;
    private final static int AIRCRAFT_MODEL_START_INDEX           = 3;
    private final static int AIRCRAFT_DESCRIPTION_START_INDEX     = 4;
    private final static int WAKE_TURBULENCE_CATEGORY_START_INDEX = 5;

    private final static String AIRCRAFT_DATABASE_SEPARATOR = ",";
    /**
     * The name of the file containing the aircraft data.
     */

    private final        String fileName;

    /**
     * Constructs an {@code AircraftDatabase} with the specified file name.
     *
     * @param fileName the name of the file containing the aircraft data
     * @throws NullPointerException if {@code fileName} is {@code null}
     */
    public AircraftDatabase(String fileName) {

        this.fileName = Objects.requireNonNull(fileName, "The file name must not be null.");
    }

    /**
     * Retrieves the {@link AircraftData} associated with the specified {@link IcaoAddress}.
     *
     * @param icaoAddress the {@code IcaoAddress} for which to retrieve aircraft data
     * @return the {@code AircraftData} associated with the specified {@code IcaoAddress}, or {@code null} if no data is found
     * @throws IOException if an I/O error occurs while reading the file
     */
    public AircraftData get(IcaoAddress icaoAddress) throws IOException {
        // Get the resource path of the file using the class loader

        try (ZipFile zipFile = new ZipFile((fileName));
             InputStream inputStream = zipFile.getInputStream(
                     zipFile.getEntry(icaoAddress.string().substring(
                             IcaoAddress.ICAOADDRESS_INDEX_OF_LAST_2_CHARACTERS,
                             IcaoAddress.ICAOADDRESS_LENGTH) + ".csv"));
             Reader reader = new InputStreamReader(inputStream, UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            String line = "";
            // Read the file line by line
            while ((line = bufferedReader.readLine()) != null) {
                // Split each line into columns using comma as the delimiter
                String[] columns = line.split(AIRCRAFT_DATABASE_SEPARATOR, -1);
                // If the first column of the line matches the IcaoAddress, create and return an AircraftData object
                if (columns[ICAOADDRESS_START_INDEX].equals(icaoAddress.string())) {
                    return new AircraftData(
                            new AircraftRegistration(columns[AIRCRAFT_REGISTRATION_START_INDEX]),
                            new AircraftTypeDesignator(columns[AIRCRAFT_TYPE_DESIGNATOR_START_INDEX]),
                            columns[AIRCRAFT_MODEL_START_INDEX],
                            new AircraftDescription(columns[AIRCRAFT_DESCRIPTION_START_INDEX]),
                            WakeTurbulenceCategory.of(columns[WAKE_TURBULENCE_CATEGORY_START_INDEX]));
                }
                else if (columns[ICAOADDRESS_START_INDEX].compareTo(icaoAddress.string()) > 0) {
                    // The file is sorted by icaoAddress, so if we've gone past the desired icaoAddress, we can stop searching
                    break;
                }
            }
        }

        // If no matching data is found, return null
        return null;
    }
}

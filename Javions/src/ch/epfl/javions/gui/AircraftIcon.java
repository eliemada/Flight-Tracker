package ch.epfl.javions.gui;

import ch.epfl.javions.aircraft.AircraftDescription;
import ch.epfl.javions.aircraft.AircraftTypeDesignator;
import ch.epfl.javions.aircraft.WakeTurbulenceCategory;

import java.util.HashMap;
import java.util.Map;

import static ch.epfl.javions.aircraft.WakeTurbulenceCategory.HEAVY;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The AircraftIcon enum represents the icon of an aircraft.
 */
public enum AircraftIcon {
    AIRLINER("""
                     M 0.01 14.75 c -0.26 0 -0.74 -0.71 -0.86 -1.41 l -3.33 0.86 L -4.5
                     14.29 l 0.08 -1.41 l 0.11 -0.07 c 1.13 -0.68 2.68 -1.64 3.2 -2 c -0.37
                     -1.06 -0.51 -3.92 -0.43 -8.52 v 0 L -4.5 2.31 C -7.13 3.12 -11.3 4.39
                     -11.5 4.5 a 0.5 0.5 0 0 1 -0.21 0 a 0.52 0.52 0 0 1 -0.49 -0.45 a 1 1
                     0 0 1 0.52 -1 l 1.74 -0.91 c 1.36 -0.71 3.22 -1.69 4.66 -2.43 a 4 4 0
                     0 1 0 -0.52 c 0 -0.69 0 -1 0 -1.14 l 0.25 -0.13 H -5.34 A 1.07 1.07 0
                     0 1 -4.26 -3.27 A 1.12 1.12 0 0 1 -3.44 -3 a 1.46 1.46 0 0 1 0.26 0.87
                     L -3.42 -2 h 0.25 c 0 0.14 0 0.31 0 0.58 l 1.52 -0.84 c 0 -1.48 0
                     -7.06 1.1 -8.25 a 0.74 0.74 0 0 1 1.13 0 c 1.15 1.19 1.13 6.78 1.1
                     8.25 l 1.52 0.84 c 0 -0.32 0 -0.48 0 -0.58 l 0.25 -0.13 H 3.2 A 1.46
                     1.46 0 0 1 3.5 -3 a 1.11 1.11 0 0 1 0.82 -0.28 a 1.06 1.06 0 0 1 1.08
                     1.16 V -2 c 0 0.19 0 0.48 0 1.17 a 4 4 0 0 1 0 0.52 c 1.75 0.9 4.4
                     2.29 5.67 3 l 0.73 0.38 a 0.9 0.9 0 0 1 0.5 1 a 0.55 0.55 0 0 1 -0.5
                     0.47 h 0 l -0.11 0 c -0.28 -0.11 -4.81 -1.49 -7.16 -2.2 H 1.56 v 0 c
                     0.09 4.6 -0.06 7.46 -0.43 8.52 c 0.52 0.33 2.07 1.29 3.2 2 l 0.11 0.07
                     L 4.5 14.29 l -0.33 -0.09 l -3.33 -0.86 c -0.12 0.7 -0.6 1.41 -0.86
                     1.41 h 0 Z"""),
    BALLOON(false, """
            M -0.94 3.75 a 0.49 0.49 0 0 1 -0.46 -0.34 L -1.87 2 a 0.51 0.51 0 0 1
            0.07 -0.44 l 0.1 -0.1 l -2 -3.68 a 0.48 0.48 0 0 1 -0.05 -0.17 a 4.39
            4.39 0 0 1 -0.48 -2 A 4.29 4.29 0 0 1 0 -8.75 A 4.29 4.29 0 0 1 4.25
            -4.42 a 4.39 4.39 0 0 1 -0.48 2 a 0.45 0.45 0 0 1 -0.05 0.17 l -2 3.68
            a 0.44 0.44 0 0 1 0.1 0.1 a 0.51 0.51 0 0 1 0.07 0.45 L 1.4 3.41 a
            0.49 0.49 0 0 1 -0.46 0.34 Z m 1.6 -2.43 L 1.6 -0.41 A 4.22 4.22 0 0 1
            0.5 -0.12 v 1.44 Z M -0.5 1.32 V -0.12 A 4.22 4.22 0 0 1 -1.6 -0.41 l
            0.94 1.73 Z"""),
    CESSNA("""
                   M 0.01 7.75 c -0.17 0 -2 -0.27 -2.56 -0.35 A 0.41 0.41 0 0 1 -2.9 7 V
                   5.87 a 0.41 0.41 0 0 1 0.32 -0.4 l 1.81 -0.37 L -1.14 1.64 H -3.75 L
                   -7.9 1 a 0.41 0.41 0 0 1 -0.35 -0.41 V -1 a 0.41 0.41 0 0 1 0.38 -0.41
                   l 4.09 -0.28 h 2.6 v -0.4 l 0.25 0 l -0.24 -0.08 c 0 -0.21 0.1 -0.76
                   0.12 -1.06 A 0.9 0.9 0 0 1 -0.5 -4.06 L -0.38 -4.46 A 0.41 0.41 0 0 1
                   0 -4.75 a 0.4 0.4 0 0 1 0.39 0.29 L 0.5 -4.05 a 0.91 0.91 0 0 1 0.53
                   0.75 c 0 0.33 0.11 1 0.13 1.11 v 0.46 h 2.57 l 4.12 0.28 a 0.41 0.41 0
                   0 1 0.38 0.41 V 0.63 A 0.41 0.41 0 0 1 7.9 1 l -4.1 0.59 H 1.14 L 0.76
                   5.1 l 1.81 0.36 a 0.41 0.41 0 0 1 0.32 0.4 V 7 a 0.41 0.41 0 0 1 -0.34
                   0.41 c -0.56 0.08 -2.37 0.35 -2.55 0.35 Z"""),
    HEAVY_2E("""
                     M -5 15.35 c 0 -0.16 -0.17 -1 0.23 -1.36 c 0.65 -0.59 2.82 -2.38 3.4
                     -2.86 c -0.51 -1.33 -0.59 -5.15 -0.57 -8.22 L -4 3 L -13.75 6 v -0.34
                     a 1.78 1.78 0 0 1 0.82 -1.5 l 7.78 -5.07 a 4.87 4.87 0 0 1 -0.51 -3 l
                     0 -0.22 l 0.23 0 h 2.26 l 0 0.22 a 8.32 8.32 0 0 1 0 1.81 l 1.21 -0.81
                     c 0 -6.79 0.18 -9.58 1.91 -9.87 c 1.7 0.14 2 3 2 9.85 L 3.3 -2 a 8.3
                     8.3 0 0 1 0 -1.8 l 0 -0.22 h 2.51 v 0.24 a 4.87 4.87 0 0 1 -0.51 3 l
                     7.66 5 a 1.77 1.77 0 0 1 0.8 1.5 V 6 L 4 3 l -2 -0.06 c 0 3.06 -0.06
                     6.88 -0.57 8.21 a 28.87 28.87 0 0 1 3.5 3 A 2 2 0 0 1 5 15.34 l -0.05
                     0.31 L 0.6 13.71 c -0.14 1.85 -0.41 1.85 -0.6 1.85 s -0.47 0 -0.6
                     -1.84 L -5 15.66 Z"""),
    HEAVY_4E("""
                     M 0 16.62 c -0.23 0 -0.52 -0.16 -0.71 -1.33 L -5.18 16.58 V 15 l 3.56
                     -3.52 c -0.41 -1.51 -0.4 -7.57 -0.4 -9.11 L -5.54 3.59 L -12.73 7.76 l
                     -1 1.68 l 0 -0.91 c 0 -2.28 0.23 -2.45 0.3 -2.52 s 0.59 -0.51 3.5
                     -3.09 A 10.47 10.47 0 0 1 -10 0 l 0 -0.22 l 0.23 0 H -7.84 v 0.23 a
                     11.63 11.63 0 0 1 0 1.26 c 0.74 -0.68 1.36 -1.28 1.69 -1.61 a 9.54
                     9.54 0 0 1 -0.16 -3.15 l 0 -0.22 l 0.23 -0.05 H -4.13 v 0.23 a 11.49
                     11.49 0 0 1 0 1.31 l 0.87 -0.84 c 0.67 -0.66 1.06 -1 1.27 -1.19 c 0
                     -6.24 0.53 -8.46 2 -8.46 c 1.23 0 2 1.42 2 8.46 c 0.21 0.17 0.59 0.53
                     1.27 1.19 l 0.88 0.85 a 11.45 11.45 0 0 1 0 -1.32 V -3.81 h 2.18 v
                     0.24 a 9.53 9.53 0 0 1 -0.15 3.18 c 0.33 0.32 0.95 0.93 1.69 1.61 a
                     11.5 11.5 0 0 1 0 -1.27 v -0.23 H 10 V 0 a 10.49 10.49 0 0 1 -0.1 3 L
                     13.4 6 c 0.09 0.09 0.28 0.26 0.32 2.54 l 0 0.91 l -1 -1.68 L 5.5 3.57
                     L 2 2.34 c 0 1.53 0.07 7.49 -0.39 9.11 L 5.18 15 v 1.61 l -4.46 -1.29
                     C 0.52 16.46 0.23 16.62 0 16.62 Z"""),
    HELICOPTER("""
                       M 0 10.75 c -1.38 0 -2.46 -0.63 -2.46 -1.43 c 0 -0.6 0.58 -1.1 1.49
                       -1.32 V 5.06 A 5.27 5.27 0 0 1 -2 2.53 L -6.9 6.6 l -0.75 -1 L -2.22
                       1.09 c 0 -0.25 0 -0.51 0 -0.77 a 12.28 12.28 0 0 1 0.09 -1.49 L -7.62
                       -5.76 l 0.7 -0.89 l 5 4.2 C -1.52 -4 -0.83 -4.9 0 -4.9 s 1.52 1 1.91
                       2.57 l 5 -4.21 l 0.75 1 L 2.1 -0.93 a 12.4 12.4 0 0 1 0.06 1.24 c 0
                       0.22 0 0.44 0 0.65 l 5.47 4.59 l -0.7 0.89 L 2 2.31 a 8.44 8.44 0 0 1
                       -0.35 1.4 a 3.83 3.83 0 0 1 -0.55 1.11 L 1 5 v 3 c 0.91 0.22 1.49 0.72
                       1.49 1.32 C 2.46 10.12 1.38 10.75 0 10.75 Z"""),
    HI_PERF("""
                    M -4.36 8.76 v -1.6 l 2.57 -1.7 V 4.1 H -7.24 V 0.25 H -5.89 v 1.17 L
                    -2.22 -2.1 c 0.14 -1.16 1 -8.19 2 -9.3 L 0 -11.62 l 0.2 0.22 c 1 1.12
                    1.89 8.14 2 9.3 l 3.67 3.52 V 0.25 h 1.35 V 4.1 H 1.79 v 1.35 l 2.57
                    1.7 v 1.6 Z"""),
    JET_NONSWEPT("""
                         M 0 9.09 l -3.51 0.61 v -0.3 c 0 -0.65 0.11 -1 0.33 -1.09 L -0.5 7 a
                         5.61 5.61 0 0 1 -0.28 -1.32 l -0.53 -0.41 l -0.1 -0.69 H -1.88 l 0
                         -0.21 a 7.19 7.19 0 0 1 -0.15 -2.19 L -8.76 1.05 V 0.84 c 0 -1.1 0.51
                         -1.15 0.61 -1.15 L -1.2 -0.82 V -5.12 C -1.2 -7.36 -0.11 -7.7 -0.07
                         -7.72 L 0 -7.74 l 0.07 0 s 1.13 0.36 1.13 2.6 v 4.3 l 7 0.51 c 0.09 0
                         0.59 0.06 0.59 1.15 v 0.21 l -6.69 1.16 a 7.17 7.17 0 0 1 -0.15 2.19 l
                         0 0.21 h -0.47 l -0.1 0.69 l -0.53 0.41 A 5.61 5.61 0 0 1 0.5 7 l 2.74
                         1.28 c 0.2 0.07 0.31 0.43 0.31 1.08 v 0.3 Z"""),
    JET_SWEPT("""
                      M 0.44 12 c -0.1 0.6 -0.35 0.6 -0.44 0.6 s -0.34 0 -0.44 -0.6 l -3
                      0.67 V 11.6 A 0.54 0.54 0 0 1 -3 11.05 l 2.38 -1.12 L -1 8.33 H -2.31
                      l 0 -0.2 a 8.23 8.23 0 0 1 -0.14 -3.85 l 0.06 -0.18 H -1.27 V 2.19 h
                      -2 L -8.74 3.29 v -0.93 c 0 -0.28 0.07 -0.46 0.22 -0.53 l 7.25 -3.6 V
                      -7.15 A 4.47 4.47 0 0 1 -0.17 -10.51 L 0 -10.66 l 0.17 0.15 a 4.47
                      4.47 0 0 1 1.1 3.36 V -1.77 l 7.25 3.6 c 0.14 0.07 0.22 0.25 0.22 0.53
                      v 0.93 l -5.51 -1.1 h -2 V 4.1 h 1.17 l 0.06 0.18 a 8.24 8.24 0 0 1
                      -0.15 3.84 l 0 0.2 H 1 l -0.36 1.6 l 2.43 1.14 a 0.52 0.52 0 0 1 0.35
                      0.53 v 1.08 Z"""),
    TWIN_LARGE("""
                       M -0.4 9.34 H -3.5 l 0 -0.21 c -0.08 -0.54 0 -0.87 0.11 -1 L -3.31 8 l
                       0.2 0 l 2.35 -0.33 c -0.16 -0.82 -0.42 -2.9 -0.42 -3.14 s 0 -2.71 0
                       -3.51 H -2.5 c -0.12 1.34 -0.41 1.36 -0.55 1.37 h 0 c -0.19 0 -0.46 0
                       -0.6 -1.55 L -10.23 0.52 l 0 -0.25 c 0.06 -0.73 0.31 -0.9 0.45 -0.93 l
                       6 -0.48 a 3.65 3.65 0 0 1 0.3 -2 a 0.45 0.45 0 0 1 0.32 -0.16 h 0 a
                       0.39 0.39 0 0 1 0.3 0.12 A 3.67 3.67 0 0 1 -2.5 -1.23 l 1.26 -0.07 c 0
                       -0.71 0 -2.92 0 -4.48 A 3.84 3.84 0 0 1 -0.4 -8.6 a 0.4 0.4 0 0 1 0.28
                       -0.16 h 0.23 A 0.4 0.4 0 0 1 0.4 -8.6 a 3.84 3.84 0 0 1 0.87 2.81 c 0
                       1.55 0 3.77 0 4.48 L 2.5 -1.23 a 3.67 3.67 0 0 1 0.29 -1.94 a 0.38
                       0.38 0 0 1 0.28 -0.12 a 0.46 0.46 0 0 1 0.34 0.16 a 3.66 3.66 0 0 1
                       0.3 2 l 6 0.48 c 0.18 0 0.43 0.21 0.49 0.94 l 0 0.25 l -6.53 0.3 c
                       -0.14 1.55 -0.42 1.55 -0.59 1.55 s -0.45 0 -0.57 -1.37 H 1.24 c 0 0.8
                       0 3.27 0 3.51 s -0.26 2.32 -0.42 3.14 l 2.38 0.34 h 0.11 l 0.13 0.13 c
                       0.15 0.18 0.19 0.51 0.11 1 l 0 0.21 H 0.4 l -0.4 1 Z"""),
    TWIN_SMALL("""
                       M 0 9.75 c -0.21 0 -0.34 -0.17 -0.41 -0.51 l -2.88 0.23 v -0.27 c 0
                       -0.78 0 -1.11 0.28 -1.13 L -0.5 7.1 c -0.31 -1.86 -0.55 -5 -0.59 -5.55
                       l -0.08 -0.09 H -3.42 L -9.25 0.54 v -1 A 0.43 0.43 0 0 1 -8.83 -1 l
                       3.75 -0.27 L -4.5 -1.55 V -2.47 H -4.77 V -3.3 a 0.35 0.35 0 0 1 0.34
                       -0.35 h 0.07 c 0.12 -0.52 0.26 -0.83 0.54 -0.83 s 0.42 0.31 0.53 0.83
                       h 0.07 a 0.35 0.35 0 0 1 0.34 0.35 v 0.83 H -3.14 v 1 l 2 -0.08 C
                       -1.08 -5.19 -0.41 -5.75 -0.01 -5.75 s 1.09 0.55 1.12 4.21 l 2 0.08 v
                       -1 h -0.25 V -3.3 a 0.35 0.35 0 0 1 0.34 -0.35 h 0.07 c 0.12 -0.52
                       0.26 -0.83 0.53 -0.83 s 0.42 0.31 0.54 0.83 h 0.07 a 0.35 0.35 0 0 1
                       0.34 0.35 v 0.83 H 4.5 v 0.92 l 0.57 0.32 L 8.82 -1 a 0.42 0.42 0 0 1
                       0.43 0.46 v 1 L 3.5 1.46 H 1.21 l -0.08 0.09 c 0 0.56 -0.27 3.68 -0.59
                       5.55 l 2.46 1 c 0.28 0 0.28 0.35 0.28 1.13 v 0.27 l -2.88 -0.23 C 0.34
                       9.58 0.21 9.75 0 9.75 Z"""),
    UNKNOWN("""
                    M -3.25 8.76 c -0.92 0 -1.33 -0.46 -1.39 -0.86 a 1 1 0 0 1 0.79 -1.11
                    c 0.25 -0.08 1.22 -0.43 2.63 -1 V 2.65 h -6 c -0.68 0 -1 -0.35 -1
                    -0.66 a 0.81 0.81 0 0 1 0.6 -0.86 C -7.36 1 -3.7 -1 -1.22 -2.37 V -5 c
                    0 -1.11 0.44 -2.71 1.23 -2.71 S 1.27 -6.16 1.27 -5 V -2.37 C 3.72 -1
                    7.37 1 7.64 1.13 a 0.8 0.8 0 0 1 0.61 0.86 c -0.05 0.31 -0.36 0.67
                    -1.05 0.67 H 1.27 v 3.19 l 1.61 0.59 l 1 0.36 a 1.05 1.05 0 0 1 0.8
                    1.11 c -0.07 0.39 -0.47 0.86 -1.39 0.86 Z""");

    private static final Map<AircraftTypeDesignator, AircraftIcon> TYPE_DESIGNATOR_TABLE =
            createTypeDesignatorTable();
    private final        boolean                                   canRotate;
    private final        String                                    svgPath;

    AircraftIcon(boolean canRotate, String svgPath) {
        this.canRotate = canRotate;
        this.svgPath   = svgPath;
    }

    AircraftIcon(String svgPath) {
        this(true, svgPath);
    }

    private static Map<AircraftTypeDesignator, AircraftIcon> createTypeDesignatorTable() {
        // Note: we don't use Map.ofEntries here, as IntelliJ becomes slow if we do.
        var map = new HashMap<AircraftTypeDesignator, AircraftIcon>();
        map.put(new AircraftTypeDesignator("A10"), HI_PERF);
        map.put(new AircraftTypeDesignator("A148"), HI_PERF);
        map.put(new AircraftTypeDesignator("A225"), HEAVY_4E);
        map.put(new AircraftTypeDesignator("A3"), HI_PERF);
        map.put(new AircraftTypeDesignator("A37"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("A5"), CESSNA);
        map.put(new AircraftTypeDesignator("A6"), HI_PERF);
        map.put(new AircraftTypeDesignator("A700"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("AC80"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("AC90"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("AC95"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("AJ27"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("AJET"), HI_PERF);
        map.put(new AircraftTypeDesignator("AN28"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("ARCE"), HI_PERF);
        map.put(new AircraftTypeDesignator("AT3"), HI_PERF);
        map.put(new AircraftTypeDesignator("ATG1"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("B18T"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("B190"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("B25"), TWIN_LARGE);
        map.put(new AircraftTypeDesignator("B350"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("B52"), HEAVY_4E);
        map.put(new AircraftTypeDesignator("B712"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("B721"), AIRLINER);
        map.put(new AircraftTypeDesignator("B722"), AIRLINER);
        map.put(new AircraftTypeDesignator("BALL"), BALLOON);
        map.put(new AircraftTypeDesignator("BE10"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("BE20"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("BE30"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("BE32"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("BE40"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("BE99"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("BE9L"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("BE9T"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("BN2T"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("BPOD"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("BU20"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("C08T"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("C125"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("C212"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("C21T"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("C22J"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C25A"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C25B"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C25C"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C25M"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C425"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("C441"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("C46"), TWIN_LARGE);
        map.put(new AircraftTypeDesignator("C500"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C501"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C510"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C525"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C526"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C550"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C551"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C55B"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C560"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C56X"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C650"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("C680"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C68A"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("C750"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("C82"), TWIN_LARGE);
        map.put(new AircraftTypeDesignator("CKUO"), HI_PERF);
        map.put(new AircraftTypeDesignator("CL30"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("CL35"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("CL60"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("CRJ1"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("CRJ2"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("CRJ7"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("CRJ9"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("CRJX"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("CVLP"), TWIN_LARGE);
        map.put(new AircraftTypeDesignator("D228"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("DA36"), HI_PERF);
        map.put(new AircraftTypeDesignator("DA50"), AIRLINER);
        map.put(new AircraftTypeDesignator("DC10"), HEAVY_2E);
        map.put(new AircraftTypeDesignator("DC3"), TWIN_LARGE);
        map.put(new AircraftTypeDesignator("DC3S"), TWIN_LARGE);
        map.put(new AircraftTypeDesignator("DHA3"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("DHC4"), TWIN_LARGE);
        map.put(new AircraftTypeDesignator("DHC6"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("DLH2"), HI_PERF);
        map.put(new AircraftTypeDesignator("E110"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("E135"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("E145"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("E29E"), HI_PERF);
        map.put(new AircraftTypeDesignator("E45X"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("E500"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("E50P"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("E545"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("E55P"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("EA50"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("EFAN"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("EFUS"), HI_PERF);
        map.put(new AircraftTypeDesignator("ELIT"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("EUFI"), HI_PERF);
        map.put(new AircraftTypeDesignator("F1"), HI_PERF);
        map.put(new AircraftTypeDesignator("F100"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("F111"), HI_PERF);
        map.put(new AircraftTypeDesignator("F117"), HI_PERF);
        map.put(new AircraftTypeDesignator("F14"), HI_PERF);
        map.put(new AircraftTypeDesignator("F15"), HI_PERF);
        map.put(new AircraftTypeDesignator("F22"), HI_PERF);
        map.put(new AircraftTypeDesignator("F2TH"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("F4"), HI_PERF);
        map.put(new AircraftTypeDesignator("F406"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("F5"), HI_PERF);
        map.put(new AircraftTypeDesignator("F900"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("FA50"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("FA5X"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("FA7X"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("FA8X"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("FJ10"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("FOUG"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("FURY"), HI_PERF);
        map.put(new AircraftTypeDesignator("G150"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("G3"), AIRLINER);
        map.put(new AircraftTypeDesignator("GENI"), HI_PERF);
        map.put(new AircraftTypeDesignator("GL5T"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("GLEX"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("GLF2"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("GLF3"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("GLF4"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("GLF5"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("GLF6"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("GSPN"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("H25A"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("H25B"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("H25C"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("HA4T"), AIRLINER);
        map.put(new AircraftTypeDesignator("HDJT"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("HERN"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("J8A"), HI_PERF);
        map.put(new AircraftTypeDesignator("J8B"), HI_PERF);
        map.put(new AircraftTypeDesignator("JH7"), HI_PERF);
        map.put(new AircraftTypeDesignator("JS31"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("JS32"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("JU52"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("L101"), HEAVY_2E);
        map.put(new AircraftTypeDesignator("LAE1"), HI_PERF);
        map.put(new AircraftTypeDesignator("LEOP"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LJ23"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LJ24"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LJ25"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LJ28"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LJ31"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LJ35"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LJ40"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LJ45"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LJ55"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LJ60"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LJ70"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LJ75"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LJ85"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("LTNG"), HI_PERF);
        map.put(new AircraftTypeDesignator("M28"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("MD11"), HEAVY_2E);
        map.put(new AircraftTypeDesignator("MD81"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("MD82"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("MD83"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("MD87"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("MD88"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("MD90"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("ME62"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("METR"), HI_PERF);
        map.put(new AircraftTypeDesignator("MG19"), HI_PERF);
        map.put(new AircraftTypeDesignator("MG25"), HI_PERF);
        map.put(new AircraftTypeDesignator("MG29"), HI_PERF);
        map.put(new AircraftTypeDesignator("MG31"), HI_PERF);
        map.put(new AircraftTypeDesignator("MG44"), HI_PERF);
        map.put(new AircraftTypeDesignator("MH02"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("MS76"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("MT2"), HI_PERF);
        map.put(new AircraftTypeDesignator("MU2"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("P180"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("P2"), TWIN_LARGE);
        map.put(new AircraftTypeDesignator("P68T"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("PA47"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("PAT4"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("PAY1"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("PAY2"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("PAY3"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("PAY4"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("PIAE"), HI_PERF);
        map.put(new AircraftTypeDesignator("PIT4"), HI_PERF);
        map.put(new AircraftTypeDesignator("PITE"), HI_PERF);
        map.put(new AircraftTypeDesignator("PRM1"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("PRTS"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("Q5"), HI_PERF);
        map.put(new AircraftTypeDesignator("R721"), AIRLINER);
        map.put(new AircraftTypeDesignator("R722"), AIRLINER);
        map.put(new AircraftTypeDesignator("RFAL"), HI_PERF);
        map.put(new AircraftTypeDesignator("ROAR"), HI_PERF);
        map.put(new AircraftTypeDesignator("S3"), HI_PERF);
        map.put(new AircraftTypeDesignator("S32E"), HI_PERF);
        map.put(new AircraftTypeDesignator("S37"), HI_PERF);
        map.put(new AircraftTypeDesignator("S601"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("SATA"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("SB05"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("SC7"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("SF50"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("SJ30"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("SLCH"), HEAVY_4E);
        map.put(new AircraftTypeDesignator("SM60"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("SOL1"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("SOL2"), JET_SWEPT);
        map.put(new AircraftTypeDesignator("SP33"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("SR71"), HI_PERF);
        map.put(new AircraftTypeDesignator("SS2"), HI_PERF);
        map.put(new AircraftTypeDesignator("SU15"), HI_PERF);
        map.put(new AircraftTypeDesignator("SU24"), HI_PERF);
        map.put(new AircraftTypeDesignator("SU25"), HI_PERF);
        map.put(new AircraftTypeDesignator("SU27"), HI_PERF);
        map.put(new AircraftTypeDesignator("SW2"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("SW3"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("SW4"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("T154"), AIRLINER);
        map.put(new AircraftTypeDesignator("T2"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("T22M"), HI_PERF);
        map.put(new AircraftTypeDesignator("T37"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("T38"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("T4"), HI_PERF);
        map.put(new AircraftTypeDesignator("TJET"), JET_NONSWEPT);
        map.put(new AircraftTypeDesignator("TOR"), HI_PERF);
        map.put(new AircraftTypeDesignator("TRIM"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("TRIS"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("TRMA"), TWIN_SMALL);
        map.put(new AircraftTypeDesignator("TU22"), HI_PERF);
        map.put(new AircraftTypeDesignator("VAUT"), HI_PERF);
        map.put(new AircraftTypeDesignator("Y130"), HI_PERF);
        map.put(new AircraftTypeDesignator("Y141"), AIRLINER);
        map.put(new AircraftTypeDesignator("YK28"), HI_PERF);
        map.put(new AircraftTypeDesignator("YK38"), AIRLINER);
        map.put(new AircraftTypeDesignator("YK40"), AIRLINER);
        map.put(new AircraftTypeDesignator("YK42"), AIRLINER);
        map.put(new AircraftTypeDesignator("YURO"), HI_PERF);
        return Map.copyOf(map);
    }

    public static AircraftIcon iconFor(AircraftTypeDesignator typeDesignator,
                                       AircraftDescription typeDescription,
                                       int category,
                                       WakeTurbulenceCategory wakeTurbulenceCategory) {
        var maybeDesignatorIcon = TYPE_DESIGNATOR_TABLE.get(typeDesignator);
        if (maybeDesignatorIcon != null) {
            return maybeDesignatorIcon;
        }

        var description = typeDescription.string();
        if (description.startsWith("H")) {
            return HELICOPTER;
        }

        var maybeDescriptionIcon = switch (description) {
            case "L1P", "L1T" -> CESSNA;
            case "L1J" -> HI_PERF;
            case "L2P" -> TWIN_SMALL;
            case "L2T" -> TWIN_LARGE;
            case "L2J" -> switch (wakeTurbulenceCategory) {
                case LIGHT -> JET_SWEPT;
                case MEDIUM -> AIRLINER;
                case HEAVY -> HEAVY_2E;
                default -> null;
            };
            case "L4T" -> HEAVY_4E;
            case "L4J" -> wakeTurbulenceCategory == HEAVY ? HEAVY_4E : null;
            default -> null;
        };

        if (maybeDescriptionIcon != null) {
            return maybeDescriptionIcon;
        }

        return switch (category) {
            case 0xA1, 0xB1, 0xB4 -> CESSNA;
            case 0xA2 -> JET_NONSWEPT;
            case 0xA3 -> AIRLINER;
            case 0xA4 -> HEAVY_2E;
            case 0xA5 -> HEAVY_4E;
            case 0xA6 -> HI_PERF;
            case 0xA7 -> HELICOPTER;
            case 0xB2 -> BALLOON;
            default -> UNKNOWN;
        };
    }

    public boolean canRotate() {
        return canRotate;
    }

    public String svgPath() {
        return svgPath;
    }
}

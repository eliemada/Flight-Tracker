package ch.epfl.sigcheck;

// Attention : cette classe n'est *pas* un test JUnit, et son code n'est
// pas destiné à être exécuté. Son seul but est de vérifier, autant que
// possible, que les noms et les types des différentes entités à définir
// pour cette étape du projet sont corrects.

final class SignatureChecks_2 {
    private SignatureChecks_2() {}

    void checkCrc24() throws Exception {
        v01 = new ch.epfl.javions.Crc24(v02);
        v02 = ch.epfl.javions.Crc24.GENERATOR;
        v02 = v01.crc(v03);
    }

    void checkAircraftDescription() throws Exception {
        v04 = new ch.epfl.javions.aircraft.AircraftDescription(v05);
        v07 = v04.equals(v06);
        v02 = v04.hashCode();
        v05 = v04.string();
        v05 = v04.toString();
    }

    void checkAircraftRegistration() throws Exception {
        v08 = new ch.epfl.javions.aircraft.AircraftRegistration(v05);
        v07 = v08.equals(v06);
        v02 = v08.hashCode();
        v05 = v08.string();
        v05 = v08.toString();
    }

    void checkAircraftTypeDesignator() throws Exception {
        v09 = new ch.epfl.javions.aircraft.AircraftTypeDesignator(v05);
        v07 = v09.equals(v06);
        v02 = v09.hashCode();
        v05 = v09.string();
        v05 = v09.toString();
    }

    void checkIcaoAddress() throws Exception {
        v10 = new ch.epfl.javions.aircraft.IcaoAddress(v05);
        v07 = v10.equals(v06);
        v02 = v10.hashCode();
        v05 = v10.string();
        v05 = v10.toString();
    }

    void checkCallSign() throws Exception {
        v11 = new ch.epfl.javions.adsb.CallSign(v05);
        v07 = v11.equals(v06);
        v02 = v11.hashCode();
        v05 = v11.string();
        v05 = v11.toString();
    }

    void checkWakeTurbulenceCategory() throws Exception {
        v12 = ch.epfl.javions.aircraft.WakeTurbulenceCategory.HEAVY;
        v12 = ch.epfl.javions.aircraft.WakeTurbulenceCategory.LIGHT;
        v12 = ch.epfl.javions.aircraft.WakeTurbulenceCategory.MEDIUM;
        v12 = ch.epfl.javions.aircraft.WakeTurbulenceCategory.UNKNOWN;
        v12 = ch.epfl.javions.aircraft.WakeTurbulenceCategory.of(v05);
        v12 = ch.epfl.javions.aircraft.WakeTurbulenceCategory.valueOf(v05);
        v13 = ch.epfl.javions.aircraft.WakeTurbulenceCategory.values();
    }

    void checkAircraftData() throws Exception {
        v14 = new ch.epfl.javions.aircraft.AircraftData(v08, v09, v05, v04, v12);
        v04 = v14.description();
        v07 = v14.equals(v06);
        v02 = v14.hashCode();
        v05 = v14.model();
        v08 = v14.registration();
        v05 = v14.toString();
        v09 = v14.typeDesignator();
        v12 = v14.wakeTurbulenceCategory();
    }

    void checkAircraftDatabase() throws Exception {
        v15 = new ch.epfl.javions.aircraft.AircraftDatabase(v05);
        v14 = v15.get(v10);
    }

    ch.epfl.javions.Crc24 v01;
    int v02;
    byte[] v03;
    ch.epfl.javions.aircraft.AircraftDescription v04;
    java.lang.String v05;
    java.lang.Object v06;
    boolean v07;
    ch.epfl.javions.aircraft.AircraftRegistration v08;
    ch.epfl.javions.aircraft.AircraftTypeDesignator v09;
    ch.epfl.javions.aircraft.IcaoAddress v10;
    ch.epfl.javions.adsb.CallSign v11;
    ch.epfl.javions.aircraft.WakeTurbulenceCategory v12;
    ch.epfl.javions.aircraft.WakeTurbulenceCategory[] v13;
    ch.epfl.javions.aircraft.AircraftData v14;
    ch.epfl.javions.aircraft.AircraftDatabase v15;
}

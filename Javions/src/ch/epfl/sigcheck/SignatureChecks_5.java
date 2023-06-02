package ch.epfl.sigcheck;

// Attention : cette classe n'est *pas* un test JUnit, et son code n'est
// pas destiné à être exécuté. Son seul but est de vérifier, autant que
// possible, que les noms et les types des différentes entités à définir
// pour cette étape du projet sont corrects.

final class SignatureChecks_5 {
    private SignatureChecks_5() {}

    void checkCprDecoder() throws Exception {
        v03 = ch.epfl.javions.adsb.CprDecoder.decodePosition(v01, v01, v01, v01, v02);
    }

    void checkAircraftIdentificationMessage() throws Exception {
        v04 = new ch.epfl.javions.adsb.AircraftIdentificationMessage(v05, v06, v02, v07);
        v04 = ch.epfl.javions.adsb.AircraftIdentificationMessage.of(v08);
        v07 = v04.callSign();
        v02 = v04.category();
        v10 = v04.equals(v09);
        v02 = v04.hashCode();
        v06 = v04.icaoAddress();
        v05 = v04.timeStampNs();
        v11 = v04.toString();
    }

    void checkAirbornePositionMessage() throws Exception {
        v12 = new ch.epfl.javions.adsb.AirbornePositionMessage(v05, v06, v01, v02, v01, v01);
        v12 = ch.epfl.javions.adsb.AirbornePositionMessage.of(v08);
        v01 = v12.altitude();
        v10 = v12.equals(v09);
        v02 = v12.hashCode();
        v06 = v12.icaoAddress();
        v02 = v12.parity();
        v05 = v12.timeStampNs();
        v11 = v12.toString();
        v01 = v12.x();
        v01 = v12.y();
    }

    double v01;
    int v02;
    ch.epfl.javions.GeoPos v03;
    ch.epfl.javions.adsb.AircraftIdentificationMessage v04;
    long v05;
    ch.epfl.javions.aircraft.IcaoAddress v06;
    ch.epfl.javions.adsb.CallSign v07;
    ch.epfl.javions.adsb.RawMessage v08;
    java.lang.Object v09;
    boolean v10;
    java.lang.String v11;
    ch.epfl.javions.adsb.AirbornePositionMessage v12;
}

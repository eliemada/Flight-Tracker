package ch.epfl.sigcheck;

// Attention : cette classe n'est *pas* un test JUnit, et son code n'est
// pas destiné à être exécuté. Son seul but est de vérifier, autant que
// possible, que les noms et les types des différentes entités à définir
// pour cette étape du projet sont corrects.

final class SignatureChecks_4 {
    private SignatureChecks_4() {}

    void checkAdsbDemodulator() throws Exception {
        v01 = new ch.epfl.javions.demodulation.AdsbDemodulator(v02);
        v03 = v01.nextMessage();
    }

    void checkRawMessage() throws Exception {
        v03 = new ch.epfl.javions.adsb.RawMessage(v04, v05);
        v06 = ch.epfl.javions.adsb.RawMessage.LENGTH;
        v03 = ch.epfl.javions.adsb.RawMessage.of(v04, v07);
        v06 = ch.epfl.javions.adsb.RawMessage.size(v08);
        v06 = ch.epfl.javions.adsb.RawMessage.typeCode(v04);
        v05 = v03.bytes();
        v06 = v03.downLinkFormat();
        v10 = v03.equals(v09);
        v06 = v03.hashCode();
        v11 = v03.icaoAddress();
        v04 = v03.payload();
        v04 = v03.timeStampNs();
        v12 = v03.toString();
        v06 = v03.typeCode();
    }

    void checkAircraftStateSetter() throws Exception {
        v13.setAltitude(v14);
        v13.setCallSign(v15);
        v13.setCategory(v06);
        v13.setLastMessageTimeStampNs(v04);
        v13.setPosition(v16);
        v13.setTrackOrHeading(v14);
        v13.setVelocity(v14);
    }

    void checkMessage() throws Exception {
        v11 = v17.icaoAddress();
        v04 = v17.timeStampNs();
    }

    ch.epfl.javions.demodulation.AdsbDemodulator v01;
    java.io.InputStream v02;
    ch.epfl.javions.adsb.RawMessage v03;
    long v04;
    ch.epfl.javions.ByteString v05;
    int v06;
    byte[] v07;
    byte v08;
    java.lang.Object v09;
    boolean v10;
    ch.epfl.javions.aircraft.IcaoAddress v11;
    java.lang.String v12;
    ch.epfl.javions.adsb.AircraftStateSetter v13;
    double v14;
    ch.epfl.javions.adsb.CallSign v15;
    ch.epfl.javions.GeoPos v16;
    ch.epfl.javions.adsb.Message v17;
}

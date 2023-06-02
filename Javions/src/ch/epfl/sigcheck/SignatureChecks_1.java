package ch.epfl.sigcheck;

// Attention : cette classe n'est *pas* un test JUnit, et son code n'est
// pas destiné à être exécuté. Son seul but est de vérifier, autant que
// possible, que les noms et les types des différentes entités à définir
// pour cette étape du projet sont corrects.
/**
final class SignatureChecks_1 {
    private SignatureChecks_1() {}

    void checkPreconditions() throws Exception {
        ch.epfl.javions.Preconditions.checkArgument(v01);
    }

    void checkMath2() throws Exception {
        v02 = ch.epfl.javions.Math2.asinh(v02);
        v03 = ch.epfl.javions.Math2.clamp(v03, v03, v03);
    }

    void checkUnits() throws Exception {
        v02 = ch.epfl.javions.Units.CENTI;
        v02 = ch.epfl.javions.Units.KILO;
        v02 = ch.epfl.javions.Units.convert(v02, v02, v02);
        v02 = ch.epfl.javions.Units.convertFrom(v02, v02);
        v02 = ch.epfl.javions.Units.convertTo(v02, v02);
    }

    void checkSpeed() throws Exception {
        v02 = ch.epfl.javions.Units.Speed.KILOMETER_PER_HOUR;
        v02 = ch.epfl.javions.Units.Speed.KNOT;
    }

    void checkTime() throws Exception {
        v02 = ch.epfl.javions.Units.Time.HOUR;
        v02 = ch.epfl.javions.Units.Time.MINUTE;
        v02 = ch.epfl.javions.Units.Time.SECOND;
    }

    void checkLength() throws Exception {
        v02 = ch.epfl.javions.Units.Length.CENTIMETER;
        v02 = ch.epfl.javions.Units.Length.FOOT;
        v02 = ch.epfl.javions.Units.Length.INCH;
        v02 = ch.epfl.javions.Units.Length.KILOMETER;
        v02 = ch.epfl.javions.Units.Length.METER;
        v02 = ch.epfl.javions.Units.Length.NAUTICAL_MILE;
    }

    void checkAngle() throws Exception {
        v02 = ch.epfl.javions.Units.Angle.DEGREE;
        v02 = ch.epfl.javions.Units.Angle.RADIAN;
        v02 = ch.epfl.javions.Units.Angle.T32;
        v02 = ch.epfl.javions.Units.Angle.TURN;
    }

    void checkBits() throws Exception {
        v03 = ch.epfl.javions.Bits.extractUInt(v04, v03, v03);
        v01 = ch.epfl.javions.Bits.testBit(v04, v03);
    }

    void checkByteString() throws Exception {
        v05 = new ch.epfl.javions.ByteString(v06);
        v05 = ch.epfl.javions.ByteString.ofHexadecimalString(v07);
        v03 = v05.byteAt(v03);
        v04 = v05.bytesInRange(v03, v03);
        v01 = v05.equals(v08);
        v03 = v05.hashCode();
        v03 = v05.size();
        v07 = v05.toString();
    }

    void checkWebMercator() throws Exception {
        v02 = ch.epfl.javions.WebMercator.x(v03, v02);
        v02 = ch.epfl.javions.WebMercator.y(v03, v02);
    }

    void checkGeoPos() throws Exception {
        v09 = new ch.epfl.javions.GeoPos(v03, v03);
        v01 = ch.epfl.javions.GeoPos.isValidLatitudeT32(v03);
        v01 = v09.equals(v08);
        v03 = v09.hashCode();
        v02 = v09.latitude();
        v03 = v09.latitudeT32();
        v02 = v09.longitude();
        v03 = v09.longitudeT32();
        v07 = v09.toString();
    }

    boolean v01;
    double v02;
    int v03;
    long v04;
    ch.epfl.javions.ByteString v05;
    byte[] v06;
    java.lang.String v07;
    java.lang.Object v08;
    ch.epfl.javions.GeoPos v09;
}
**/
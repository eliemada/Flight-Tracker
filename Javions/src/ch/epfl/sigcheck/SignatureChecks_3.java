package ch.epfl.sigcheck;

// Attention : cette classe n'est *pas* un test JUnit, et son code n'est
// pas destiné à être exécuté. Son seul but est de vérifier, autant que
// possible, que les noms et les types des différentes entités à définir
// pour cette étape du projet sont corrects.

final class SignatureChecks_3 {
    private SignatureChecks_3() {}

    void checkSamplesDecoder() throws Exception {
        v01 = new ch.epfl.javions.demodulation.SamplesDecoder(v02, v03);
        v03 = v01.readBatch(v04);
    }

    void checkPowerComputer() throws Exception {
        v05 = new ch.epfl.javions.demodulation.PowerComputer(v02, v03);
        v03 = v05.readBatch(v06);
    }

    void checkPowerWindow() throws Exception {
        v07 = new ch.epfl.javions.demodulation.PowerWindow(v02, v03);
        v07.advance();
        v07.advanceBy(v03);
        v03 = v07.get(v03);
        v08 = v07.isFull();
        v09 = v07.position();
        v03 = v07.size();
    }

    ch.epfl.javions.demodulation.SamplesDecoder v01;
    java.io.InputStream v02;
    int v03;
    short[] v04;
    ch.epfl.javions.demodulation.PowerComputer v05;
    int[] v06;
    ch.epfl.javions.demodulation.PowerWindow v07;
    boolean v08;
    long v09;
}

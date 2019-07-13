package nationalcipher.cipher.base.enigma;

import javalibrary.util.ArrayUtil;

public class EnigmaLib {

    public static final EnigmaMachine ENIGMA_D = new EnigmaD("Enigma D");
    public static final EnigmaMachine ENIGMA_I = new EnigmaI("Enigma I");
    public static final EnigmaMachine NORENIGMA = new Norenigma("Norenigma");
    public static final EnigmaMachine ENIGMA_M3 = new EnigmaM3("Enigma M3");
    public static final EnigmaMachine ENIGMA_M4 = new EnigmaM4("Enigma M4");
    public static final EnigmaMachine ZAHLWERK = new Zahlwerk("Zahlwerk");
    public static final EnigmaMachine ENIGMA_G31 = new EnigmaG31("Enigma G-31 (WIP)");
    public static final EnigmaMachine ENIGMA_G260 = new EnigmaG260("Enigma G-260 (WIP)");
    public static final EnigmaMachine ENIGMA_G312 = new EnigmaG312("Enigma G-312 (WIP)");
    public static final EnigmaMachine ENIGMA_K = new EnigmaK("Enigma K");
    public static final EnigmaMachine SWISS_K = new SwissK("Swiss K");
    public static final EnigmaMachine ENIGMA_KD = new EnigmaKD("Enigma KD");
    public static final EnigmaMachine ROCKET_I = new RocketI("Rocket I");
    public static final EnigmaMachine ENIGMA_T = new EnigmaT("Enigma T");

    // ZAHLWERK, ENIGMA_G31, ENIGMA_G260, ENIGMA_G312 require an alternative method
    // to solving
    public static final EnigmaMachine[] MACHINES = new EnigmaMachine[] { ENIGMA_D, ENIGMA_I, NORENIGMA, ENIGMA_M3, ENIGMA_M4, ENIGMA_K, SWISS_K, ROCKET_I };

    // Enigma Uhr
    public static final int[] AB_WIRING = new int[] { 6, 31, 4, 29, 18, 39, 16, 25, 30, 23, 28, 1, 38, 11, 36, 37, 26, 27, 24, 21, 14, 3, 12, 17, 2, 7, 0, 33, 10, 35, 8, 5, 22, 19, 20, 13, 34, 15, 32, 9 };
    public static final int[] B_PLUG_ORDER = new int[] { 6, 0, 7, 5, 1, 8, 4, 2, 9, 3 };
    public static final int[] AB_WIRING_INDEXED = ArrayUtil.toIndexedArray(AB_WIRING);
    public static final int[] B_PLUG_ORDER_INDEXED = ArrayUtil.toIndexedArray(B_PLUG_ORDER);

    // Please dont change
    public static final Integer[] DEFAULT_SETTING = new Integer[] { 0, 0, 0 };
    public static final char[] DEFAULT_ETW = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
}

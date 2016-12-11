package nationalcipher.cipher.base.enigma;

public class EnigmaLib {

	public static final EnigmaMachine ENIGMA_D = new EnigmaD("Enigma D");
	public static final EnigmaMachine ENIGMA_I = new EnigmaI("Enigma I");
	public static final EnigmaMachine NORENIGMA = new Norenigma("Norenigma");
	public static final EnigmaMachine ENIGMA_M3 = new EnigmaM3("Enigma M3");
	public static final EnigmaMachine ENIGMA_M4 = new EnigmaM4("Enigma M4");
	public static final EnigmaMachine ENIGMA_G31 = new EnigmaG31("Enigma G-31 (WIP)");
	public static final EnigmaMachine ENIGMA_G260 = new EnigmaG260("Enigma G-260 (WIP)");
	public static final EnigmaMachine ENIGMA_G312 = new EnigmaG312("Enigma G-312 (WIP)");
	public static final EnigmaMachine ENIGMA_K = new EnigmaK("Enigma K");
	public static final EnigmaMachine SWISS_K = new SwissK("Swiss K");
	public static final EnigmaMachine ROCKET_I = new RocketI("Rocket I");
	
	//ENIGMA_G31, ENIGMA_G260, ENIGMA_G312 require an alternative method to solving
	public static final EnigmaMachine[] MACHINES = new EnigmaMachine[] {ENIGMA_D, ENIGMA_I, NORENIGMA, ENIGMA_M3, ENIGMA_M4, ENIGMA_K, SWISS_K, ROCKET_I};
	
	//Please dont change
	public static final int[] DEFAULT_SETTING = new int[] {0, 0, 0};
	public static final char[] DEFAULT_ETW = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
}

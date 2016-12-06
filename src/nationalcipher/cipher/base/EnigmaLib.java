package nationalcipher.cipher.base;

public class EnigmaLib {

	public static final char[][] ENIGMA_D_ROTORS = EnigmaLib.createNormal("LPGSZMHAEOQKVXRFYBUTNICJDW", "SLVGBTFXJQOHEWIRZYAMKPCNDU", "CJGDPSHKTURAWZXFMYNQOBVLIE");
	public static final char[][] ENIGMA_D_ROTORS_INVERSE = EnigmaLib.createInverse(ENIGMA_D_ROTORS);
	public static final int NO_ENIGMA_D_ROTORS = ENIGMA_D_ROTORS.length;
	public static final int[][] ENIGMA_D_ROTORS_NOTCHES = new int[][] {{24},{4},{13}};
	public static final char[] ENIGMA_D_ETW = createInverse("QWERTZUIOASDFGHJKPYXCVBNML".toCharArray());
	public static final char[] ENIGMA_D_UKW = "IMETCGFRAYSQBZXWLHKDVUPOJN".toCharArray();
	
	public static final char[][] ENIGMA_ROTORS = EnigmaLib.createNormal("EKMFLGDQVZNTOWYHXUSPAIBRCJ", "AJDKSIRUXBLHWTMCQGZNPYFVOE", "BDFHJLCPRTXVZNYEIWGAKMUSQO", "ESOVPZJAYQUIRHXLNFTGKDCMWB", "VZBRGITYUPSDNHLXAWMJQOFECK", "JPGVOUMFYQBENHZRDKASXLICTW", "NZJHGRCXMYSWBOUFAIVLPEKQDT", "FKQHTLXOCBJSPDZRAMEWNIUYGV");
	public static final char[][] ENIGMA_ROTORS_INVERSE = EnigmaLib.createInverse(ENIGMA_ROTORS);
	public static final int[][] ENIGMA_ROTORS_NOTCHES = new int[][] {{16},{4},{21},{9},{25},{25,12},{25,12},{25,12}};
	public static final int NO_ENIGMA_ROTORS = ENIGMA_ROTORS.length;
	
	public static final char[][] NORWAY_ENIGMA_ROTORS = EnigmaLib.createNormal("WTOKASUYVRBXJHQCPZEFMDINLG", "GJLPUBSWEMCTQVHXAOFZDRKYNI", "JWFMHNBPUSDYTIXVZGRQLAOEKC", "ESOVPZJAYQUIRHXLNFTGKDCMWB", "HEJXQOTZBVFDASCILWPGYNMURK");
	public static final char[][] NORWAY_ENIGMA_ROTORS_INVERSE = EnigmaLib.createInverse(NORWAY_ENIGMA_ROTORS);
	public static final int[][] NORWAY_ENIGMA_ROTORS_NOTCHES = new int[][] {{16},{4},{21},{9},{25}};
	public static final int NO_NORWAY_ENIGMA_ROTORS = NORWAY_ENIGMA_ROTORS.length;
	
	//Reflectors
	public static final char[] REFLECTOR_A = "EJMZALYXVBWFCRQUONTSPIKHGD".toCharArray();
	public static final char[] REFLECTOR_B = "YRUHQSLDPXNGOKMIEBFZCWVJAT".toCharArray();
	public static final char[] REFLECTOR_C = "FVPJIAOYEDRZXWGCTKUQSBNMHL".toCharArray();
	public static final char[][] ENIGMA_REFLECTORS = new char[][] {REFLECTOR_A, REFLECTOR_B, REFLECTOR_C};
	
	public static final char[] REFLECTOR_B_THIN = "ENKQAUYWJICOPBLMDXZVFTHRGS".toCharArray();
	public static final char[] REFLECTOR_C_THIN = "RDOBJNTKVEHMLFCWZAXGYIPSUQ".toCharArray();
	public static final char[] UKW = "QYHOGNECVPUZTFDJAXWMKISRBL".toCharArray();
	public static final char[] NORWAY_UKW = "MOWJYPUXNDSRAIBFVLKZGQCHET".toCharArray();
	
	public static char[][] createNormal(String... input) {
		char[][] normal = new char[input.length][26];
		for(int r = 0; r < input.length; r++)
			for(int i = 0; i < 26; i++)
				normal[r][i] = input[r].charAt(i);
			
		return normal;
	}
	
	public static char[][] createInverse(char[][] normal) {
		char[][] inverse = new char[normal.length][26];
		for(int r = 0; r < normal.length; r++)
			for(int i = 0; i < 26; i++)
				inverse[r][normal[r][i] - 'A'] = (char)(i + 'A');
			
		return inverse;
	}
	
	public static char[] createInverse(char[] normal) {
		char[] inverse = new char[26];
		for(int i = 0; i < 26; i++)
			inverse[normal[i] - 'A'] = (char)(i + 'A');
			
		return inverse;
	}
	
	//Please dont change
	public static final int[] DEFAULT_SETTING = new int[] {0, 0, 0};
}

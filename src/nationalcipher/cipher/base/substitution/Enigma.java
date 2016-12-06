package nationalcipher.cipher.base.substitution;

import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.EnigmaLib;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Enigma implements IRandEncrypter {

	public static String encode(String plainText, char[][] ROTORS, char[][] INVERSE, int[][] NOTCHES, char[] REFLECTOR, String notchSetting, String ringSetting, int[] rotors, String... plugBoardSettings) {
		int[] notchKey = new int[3];
		for(int i = 0; i < notchKey.length; i++)
			notchKey[i] = notchSetting.charAt(i) - 'A';
		
		int[] ring = new int[3];
		for(int i = 0; i < ring.length; i++)
			ring[i] = ringSetting.charAt(i) - 'A';
		
		return new String(decode(plainText.toCharArray(), new byte[plainText.length()], ROTORS, INVERSE, NOTCHES, REFLECTOR, notchKey, ring, rotors));//TODO, plugBoardSettings));
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, char[][] ROTORS, char[][] INVERSE, int[][] NOTCHES, char[] REFLECTOR, int[] indicator, int[] ring, int[] rotors, char[]... plugBoardSettings) {
		
		char[] plugBoardArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		
		for(char[] swap : plugBoardSettings) {
			if(swap[0] == 0 || swap[1] == 0) continue;
			int ichar = ArrayUtil.indexOf(plugBoardArray, swap[0]);
			int jchar = ArrayUtil.indexOf(plugBoardArray, swap[1]);
			char temp = plugBoardArray[jchar]; 
			plugBoardArray[jchar] = plugBoardArray[ichar];
			plugBoardArray[ichar] = temp;
		}
		 
		for(int i = 0; i < cipherText.length; i++) {
			//Next settings
			int[] middleNotches = NOTCHES[rotors[1]];
			int[] endNotches = NOTCHES[rotors[2]];
			
			if(ArrayUtil.contains(middleNotches, indicator[1])) {
				indicator[0] = (indicator[0] + 1) % 26;
			    indicator[1] = (indicator[1] + 1) % 26;
			}
	
			if(ArrayUtil.contains(endNotches, indicator[2]))
				indicator[1] = (indicator[1] + 1) % 26;
			
		    indicator[2] = (indicator[2] + 1) % 26;
			
		    char ch = cipherText[i];
		    
		    if(plugBoardSettings.length > 0)
		    	ch = nextCharacter(ch, plugBoardArray);
		    
		    for(int r = 2; r >= 0; r--)
		    	ch = nextCharacter(ch, ROTORS[rotors[r]], indicator[r] - ring[r]);
		    
		    ch = nextCharacter(ch, REFLECTOR);
	
		    for(int r = 0; r < 3; r++)
		    	ch = nextCharacter(ch, INVERSE[rotors[r]], indicator[r] - ring[r]);
		    
		    if(plugBoardSettings.length > 0)
		    	ch = nextCharacter(ch, plugBoardArray);
		    
		    plainText[i] = (byte)ch;
		}
		
		return plainText;
	}
	
	//Used for a plugboard
	public static byte[] decode(char[] cipherText, byte[] plainText, char[][] ROTORS, char[][] INVERSE, int[][] NOTCHES, char[] REFLECTOR, int[] indicator, int[] ring, int[] rotors, char[] plugBoard) {
		 
		for(int i = 0; i < cipherText.length; i++) {
			//Next settings
			int[] middleNotches = NOTCHES[rotors[1]];
			int[] endNotches = NOTCHES[rotors[2]];
			
			if(ArrayUtil.contains(middleNotches, indicator[1])) {
				indicator[0] = (indicator[0] + 1) % 26;
			    indicator[1] = (indicator[1] + 1) % 26;
			}
	
			if(ArrayUtil.contains(endNotches, indicator[2]))
				indicator[1] = (indicator[1] + 1) % 26;
			
		    indicator[2] = (indicator[2] + 1) % 26;
			
		    char ch = cipherText[i];
		    
		    ch = nextCharacter(ch, plugBoard);
		    
		    for(int r = 2; r >= 0; r--)
		    	ch = nextCharacter(ch, ROTORS[rotors[r]], indicator[r] - ring[r]);
		    
		    ch = nextCharacter(ch, REFLECTOR);
	
		    for(int r = 0; r < 3; r++)
		    	ch = nextCharacter(ch, INVERSE[rotors[r]], indicator[r] - ring[r]);
		    
		    ch = nextCharacter(ch, plugBoard);
		    
		    plainText[i] = (byte)ch;
		}
		
		return plainText;
	}
	
	public static char nextCharacter(char ch, char[] key) {
		return key[ch - 'A'];
	}
	
	public static char nextCharacter(char ch, char[] key, int offset) {
		return (char)(((key[((ch - 'A') + 26 + offset) % 26] - 'A') + 26 - offset) % 26 + 'A');
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, EnigmaLib.ENIGMA_ROTORS, EnigmaLib.ENIGMA_ROTORS_INVERSE, EnigmaLib.ENIGMA_ROTORS_NOTCHES, EnigmaLib.REFLECTOR_B, KeyGeneration.createShortKey26(3), KeyGeneration.createShortKey26(3), KeyGeneration.createOrder(3));
	}
}

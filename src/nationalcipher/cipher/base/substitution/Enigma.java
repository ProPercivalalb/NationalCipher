package nationalcipher.cipher.base.substitution;

import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.enigma.EnigmaMachine;
import nationalcipher.cipher.base.enigma.EnigmaLib;
import nationalcipher.cipher.tools.KeyGeneration;

public class Enigma implements IRandEncrypter {
	
	public static String encode(String plainText, EnigmaMachine machine, String notchSetting, String ringSetting, int[] rotors, int reflector, String... plugBoardSettings) {
		int[] notchKey = new int[3];
		for(int i = 0; i < notchKey.length; i++)
			notchKey[i] = notchSetting.charAt(i) - 'A';
		
		int[] ring = new int[3];
		for(int i = 0; i < ring.length; i++)
			ring[i] = ringSetting.charAt(i) - 'A';
		
		return new String(decode(plainText.toCharArray(), new byte[plainText.length()], machine, notchKey, ring, rotors, -1, reflector));
	}
	
	//Useful functions
	
	public static byte[] decode(char[] cipherText, byte[] plainText, EnigmaMachine machine, int[] indicator, int[] ring, int[] rotors, int reflectorIndex, char[][] plugboard) {
		return decode(cipherText, plainText, machine.createWithPlugboard(plugboard), indicator, ring, rotors, -1, reflectorIndex);
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, EnigmaMachine machine, int[] indicator, int[] ring, int[] rotors, int thinRotor, int reflectorIndex, char[][] plugboard) {
		return decode(cipherText, plainText, machine.createWithPlugboard(plugboard), indicator, ring, rotors, thinRotor, reflectorIndex);
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, EnigmaMachine machine, int[] indicator, int[] ring, int[] rotors, int reflectorIndex) {
		return decode(cipherText, plainText, machine, indicator, ring, rotors, -1, reflectorIndex);
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, EnigmaMachine machine, int[] indicator, int[] ring, int[] rotors, int thinRotor, int reflectorIndex) {
			
		int reflectorSetting = 0;
		int thinRotorSetting = 0;
		for(int i = 0; i < cipherText.length; i++) {
			
			//Next settings
			if(machine.getStepping()) { //Ratchet Setting
				int[] middleNotches = machine.notches[rotors[1]];
				int[] endNotches = machine.notches[rotors[2]];
				
				if(ArrayUtil.contains(middleNotches, indicator[1])) {
					indicator[0] = (indicator[0] + 1) % 26;
					indicator[1] = (indicator[1] + 1) % 26;
				}
			
				if(ArrayUtil.contains(endNotches, indicator[2]))
					indicator[1] = (indicator[1] + 1) % 26;
					
				indicator[2] = (indicator[2] + 1) % 26;
			}
			else { //Cog Setting
				int[] endNotches = machine.notches[rotors[2]];
				if(ArrayUtil.contains(endNotches, indicator[2])) {
					int[] middleNotches = machine.notches[rotors[1]];
					
					if(ArrayUtil.contains(middleNotches, indicator[1])) {
						int[] otherNotches = machine.notches[rotors[0]];
						
						if(ArrayUtil.contains(otherNotches, indicator[0]))
							reflectorSetting = (reflectorSetting + 1) % 26;
						
						indicator[0] = (indicator[0] + 1) % 26;
					}
					indicator[1] = (indicator[1] + 1) % 26;
				}
				indicator[2] = (indicator[2] + 1) % 26;
			}
			
			char ch = cipherText[i];
			
			if(machine.etwInverse != null)
			  	ch = nextCharacter(ch, machine.etwInverse);
			
			//if(machine.canUhr) {
			//	ch = machine.nextUhrCharacter(ch);
			//}
			    
			for(int r = 2; r >= 0; r--)
			  	ch = nextCharacter(ch, machine.rotors[rotors[r]], indicator[r] - ring[r]);
			
			if(machine.hasThinRotor) ch = nextCharacter(ch, machine.thinRotor[thinRotor], thinRotorSetting);
			
			ch = nextCharacter(ch, machine.reflector[reflectorIndex], reflectorSetting);
		
			if(machine.hasThinRotor) ch = nextCharacter(ch, machine.thinRotorInverse[thinRotor], thinRotorSetting);
			
			for(int r = 0; r < 3; r++)
			   	ch = nextCharacter(ch, machine.rotorsInverse[rotors[r]], indicator[r] - ring[r]);
			    
			if(machine.etw != null)
			    ch = nextCharacter(ch, machine.etw);
			    
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
		EnigmaMachine machine = EnigmaLib.NORENIGMA;//RandomUtil.pickRandomElement(EnigmaLib.MACHINES);
		return encode(plainText, machine, KeyGeneration.createShortKey26(3), KeyGeneration.createShortKey26(3), KeyGeneration.createOrder(3), RandomUtil.pickRandomInt(machine.getNumberOfReflectors()));
	}
}

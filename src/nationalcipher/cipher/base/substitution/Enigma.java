package nationalcipher.cipher.base.substitution;

import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.enigma.EnigmaLib;
import nationalcipher.cipher.base.enigma.EnigmaMachine;
import nationalcipher.cipher.tools.KeyGeneration;

public class Enigma implements IRandEncrypter {
	
	public static String encode(String plainText, EnigmaMachine machine, String indicatorStr, String ringStr, int[] rotors, int reflector, String... plugBoardSettings) {
		int[] indicator = new int[3];
		for(int i = 0; i < indicator.length; i++)
			indicator[i] = indicatorStr.charAt(i) - 'A';
		
		int[] ring = new int[3];
		for(int i = 0; i < ring.length; i++)
			ring[i] = ringStr.charAt(i) - 'A';
		
		return new String(decode(plainText.toCharArray(), new byte[plainText.length()], machine, indicator, ring, rotors, -1, reflector));
	}
	
	//Useful functions
	
	public static byte[] decode(char[] cipherText, byte[] plainText, EnigmaMachine machine, int[] indicator, int[] ring, int[] rotors, int reflectorIndex, int[][] plugboard) {
		return decode(cipherText, plainText, machine.createWithPlugboard(plugboard), indicator, ring, rotors, -1, reflectorIndex);
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, EnigmaMachine machine, int[] indicator, int[] ring, int[] rotors, int thinRotor, int reflectorIndex, int[][] plugboard) {
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
					indicator[0] += 1;
					indicator[1] += 1;
					if(indicator[0] > 25) indicator[0] = 0;
					if(indicator[1] > 25) indicator[1] = 0;
				}
				
				if(ArrayUtil.contains(endNotches, indicator[2])) {
					indicator[1] += 1;
					if(indicator[1] > 25) indicator[1] = 0;
				}
						
				indicator[2] += 1;
				if(indicator[2] > 25) indicator[2] = 0;
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
			
			int ch = cipherText[i] - 'A';
			
			if(machine.etwInverse != null)
			  	ch = nextCharacter(ch, machine.etwInverse);
			    
			for(int r = 2; r >= 0; r--)
			  	ch = nextCharacter(ch, machine.rotors[rotors[r]], indicator[r] - ring[r]);
			
			if(machine.hasThinRotor) ch = nextCharacter(ch, machine.thinRotor[thinRotor], thinRotorSetting);
			
			ch = nextCharacter(ch, machine.reflector[reflectorIndex], reflectorSetting);
		
			if(machine.hasThinRotor) ch = nextCharacter(ch, machine.thinRotorInverse[thinRotor], thinRotorSetting);
			
			for(int r = 0; r < 3; r++)
			   	ch = nextCharacter(ch, machine.rotorsInverse[rotors[r]], indicator[r] - ring[r]);
			    
			if(machine.etw != null)
			    ch = nextCharacter(ch, machine.etw);
			    
			plainText[i] = (byte)(ch + 'A');
		}
			
		return plainText;
	}
	
	public static byte[] decodeFast(char[] cipherText, byte[] plainText, EnigmaMachine machine, int[] indicator, int[] ring, int[] rotors, int reflectorIndex) {
		for(int i = 0; i < cipherText.length; i++) {

			int[] middleNotches = machine.notches[rotors[1]];
			int[] endNotches = machine.notches[rotors[2]];
				
			if(ArrayUtil.contains(middleNotches, indicator[1])) {
				indicator[0] += 1;
				indicator[1] += 1;
				if(indicator[0] > 25) indicator[0] = 0;
				if(indicator[1] > 25) indicator[1] = 0;
			}
			
			if(ArrayUtil.contains(endNotches, indicator[2])) {
				indicator[1] += 1;
				if(indicator[1] > 25) indicator[1] = 0;
			}
					
			indicator[2] += 1;
			if(indicator[2] > 25) indicator[2] = 0;
			
			int ch = cipherText[i] - 'A';
			
			if(machine.etwInverse != null)
			  	ch = nextCharacter(ch, machine.etwInverse);
			    
			for(int r = 2; r >= 0; r--)
			  	ch = nextCharacter(ch, machine.rotors[rotors[r]], indicator[r] - ring[r]);
			
			ch = nextCharacter(ch, machine.reflector[reflectorIndex]);

			for(int r = 0; r < 3; r++)
			   	ch = nextCharacter(ch, machine.rotorsInverse[rotors[r]], indicator[r] - ring[r]);
			    
			if(machine.etw != null)
			    ch = nextCharacter(ch, machine.etw);
			    
			plainText[i] = (byte)(ch + 'A');
		}
			
		return plainText;
	}
	
	public static int nextCharacter(int ch, int[] key) {
		return key[ch];
	}

	public static int nextCharacter(int ch, int[] key, int offset) {
		if(offset > 0) {
			ch += offset;
			if(ch > 25) ch -= 26;
			ch = nextCharacter(ch, key);
			ch -= offset;
			if(ch < 0) ch += 26;
		}
		else if(offset < 0) {
			ch += offset;
			if(ch < 0) ch += 26;
			ch = nextCharacter(ch, key);
			ch -= offset;
			if(ch > 25) ch -= 26;
		}
		else
			ch = nextCharacter(ch, key);
		return ch;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		EnigmaMachine machine = EnigmaLib.NORENIGMA;//RandomUtil.pickRandomElement(EnigmaLib.MACHINES);
		return encode(plainText, machine, KeyGeneration.createShortKey26(3), KeyGeneration.createShortKey26(3), KeyGeneration.createOrder(3), RandomUtil.pickRandomInt(machine.getNumberOfReflectors()));
	}
}

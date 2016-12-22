package nationalcipher.cipher.base.substitution;

import java.util.Arrays;
import java.util.List;

import javalibrary.string.MorseCode;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class FractionatedMorse implements IRandEncrypter {

	public static String encode(String plainText, String key) {
		
		String cipherText = "";
		String morseText = "";
		
		morseText = MorseCode.getMorseEquivalent(plainText);
		while(morseText.length() % 3 != 0)
			morseText += "X";
		
		List<Character> list = Arrays.asList('.', '-', 'X'); 
		for(int i = 0; i < morseText.length(); i += 3) {
			int a = list.indexOf(morseText.charAt(i));
			int b = list.indexOf(morseText.charAt(i + 1));
			int c = list.indexOf(morseText.charAt(i + 2));
			cipherText += key.charAt(a * 9 + b * 3 + c);
		}
		
		return cipherText;
	}
	
	private static char[] list = new char[] {'.', '-', 'X'}; 
	
	public static byte[] decode(char[] cipherText, byte[] plainText, String key) {
		char[] morseText = new char[cipherText.length * 3];
		
		for(int i = 0; i < cipherText.length; i++) {
			int index = key.indexOf(cipherText[i]);
			morseText[i * 3] = list[index / 9];
			morseText[i * 3 + 1] = list[(int)(index / 3) % 3];
			morseText[i * 3 + 2] = list[index % 3];
		}
		
		int index = 0;
		int lastX = 0;
		for(int i = 0; i < morseText.length; i++) {
			char morseCh = morseText[i];
			if(morseCh == 'X' || i == morseText.length - 1) { //When char is X or is at the end of the text
				char character = MorseCode.getCharFromMorse(morseText, lastX, i - lastX);
				if(character == ' ') 
					for(int j = lastX; j < i; j++)
						plainText[index++] = (byte)morseText[j];
				else 
					plainText[index++] = (byte)character;
				
				lastX = i + 1;
			}
		}
		
		return ArrayUtil.copyRange(plainText, 0, index);
	}

	/**
	 * public static byte[] decode(char[] cipherText, String key) {
		String plainText = "";
		String morseText = "";
		char[] list = new char[] {'.', '-', 'X'}; 
		
		
		for(int i = 0; i < cipherText.length; i++) {
			char a = cipherText[i];
			
			int index = key.indexOf(a);
			int first = index / 9;
			int second = (int)(index / 3) % 3;
			int third = index % 3;
			morseText += list[first] + "" + list[second] + "" + list[third];
		}
		
		String[] split = morseText.split("X");
		for(String code : split) {
			try {
			
				plainText += MorseCode.getCharFromMorse(code);
			}
			catch(NullPointerException e) {
				plainText += code;
			}
		}
		
		return ArrayUtil.convertCharType(plainText.replaceAll(" ", "").toCharArray());
	}
	 */
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey26());
	}
}

package nationalcipher.cipher.base.other;

import javalibrary.string.MorseCode;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.interfaces.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Pollux implements IRandEncrypter {
	
	public static String encode(String plainText, Character[] key) {
		
		String cipherText = "";
		String morseText = "";
		
		morseText = MorseCode.getMorseEquivalent(plainText);
		
		for(int i = 0; i < morseText.length(); i++) {
			char a = morseText.charAt(i);
			int no = 0;
			while(key[(no = RandomUtil.pickRandomInt(key.length))] != a) {}
			cipherText += no;
		}
		
		return cipherText;
	}
	
	public static byte[] decode(char[] cipherText, Character[] key) {

		char[] morseText = new char[cipherText.length];
		
		
		for(int i = 0; i < cipherText.length; i++) {
			int a = cipherText[i] - '0';
		
			morseText[i] = key[a];
		}
		
		String plainText = "";
		
		int last = 0;
		for(int i = 0; i < morseText.length; i++) {
			char a = morseText[i];
			boolean end = i == morseText.length - 1;
			if(a == 'X' || end) {
				String code = new String(morseText, last, i - last + (end ? 1 : 0));
				
				last = i + 1;
				try {
					plainText += MorseCode.getCharFromMorse(code);
				}
				catch(NullPointerException e) {
					plainText += code;
				}
			}
		}
		
		return plainText.getBytes(); //TODO Unchecked
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createPolluxKey());
	}
}

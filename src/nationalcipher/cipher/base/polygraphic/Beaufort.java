package nationalcipher.cipher.base.polygraphic;

import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Beaufort implements IRandEncrypter {
	
	public static String encode(String plainText, String key) {
		String cipherText = "";
		
		for(int index = 0; index < plainText.length(); index++)
			cipherText += (char)((26 + key.charAt(index % key.length()) - plainText.charAt(index)) % 26 + 'A');
		
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String key) {
		char[] plainText = new char[cipherText.length];
		
		for(int index = 0; index < cipherText.length; index++)
			plainText[index ] = (char)((26 + key.charAt(index % key.length()) - cipherText[index]) % 26 + 'A');
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15));
	}
}

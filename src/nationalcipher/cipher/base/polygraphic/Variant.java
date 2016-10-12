package nationalcipher.cipher.base.polygraphic;

import javalibrary.math.MathUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Variant implements IRandEncrypter {

	public static String encode(String plainText, String key) {
		String cipherText = "";
		
		for(int index = 0; index < plainText.length(); index++)
			cipherText += (char)(MathUtil.mod(plainText.charAt(index) - key.charAt(index % key.length()), 26) + 'A');
		
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String key) {

		char[] plainText = new char[cipherText.length];
		
		for(int index = 0; index < cipherText.length; index++)
			plainText[index] = (char)((cipherText[index] + key.charAt(index % key.length())) % 26 + 'A');
		
		return plainText;
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15));
	}
}

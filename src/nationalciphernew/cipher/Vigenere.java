package nationalciphernew.cipher;

import javalibrary.math.MathHelper;

/**
 * @author Alex Barter
 */
public class Vigenere {

	public static String encode(String plainText, String key) {
		String cipherText = "";
		
		for(int index = 0; index < plainText.length(); index++)
			cipherText += (char)(MathHelper.mod(plainText.charAt(index) + key.charAt(index % key.length()), 26) + 'A');
		
		return cipherText;
	}
	
	public static String decode(String cipherText, String key) {
		char[] charArray = cipherText.toCharArray();
		char[] keyArray = key.toCharArray();
		
		String plainText = "";
		
		for(int index = 0; index < cipherText.length(); index++)
			plainText += (char)(MathHelper.mod(charArray[index] - keyArray[index % key.length()], 26) + 'A');
		
		return plainText;
	}
}

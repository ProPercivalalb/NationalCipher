package nationalcipher.cipher.base.substitution;

import javalibrary.math.MathUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

/**
 * @author Alex Barter (10AS)
 */
public class VigenereAutokey implements IRandEncrypter {
	
	public static String encode(String plainText, String key) {
		String autoKey = key + plainText;
		String cipherText = "";
		
		for(int index = 0; index < plainText.length(); index++)
			cipherText += (char)(MathUtil.mod(plainText.charAt(index) + autoKey.charAt(index), 26) + 'A');
		
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String key) {
		String autoKey = key;
		char[] plainText = new char[cipherText.length];
		
		for(int index = 0; index < cipherText.length; index++) {
			char newCh = (char)((26 + cipherText[index] - autoKey.charAt(index)) % 26 + 'A');
			plainText[index] = newCh;
			autoKey += newCh;
		}
		
		return plainText;
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15));
	}
}

package nationalcipher.cipher;

import javalibrary.math.MathHelper;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class VigenereProgressiveKey implements IRandEncrypter {

	public static String encode(String plainText, String keyword, int period, int progressiveKey) {
		String cipherText = "";

		for(int index = 0; index < plainText.length(); index++) {
			int progression = (int)Math.floor(index / period) * progressiveKey;
			cipherText += (char)(MathHelper.mod(plainText.charAt(index) + progression + keyword.charAt(index % keyword.length()), 26) + 'A');
		}
		
		return cipherText;
	}
	
	public static char[] decode(char[] cipherText, String keyword, int period, int progressiveKey) {
		char[] plainText = new char[cipherText.length];

		for(int index = 0; index < cipherText.length; index++) {
			int progression = (int)Math.floor(index / period) * progressiveKey;
			plainText[index] = (char)(MathHelper.mod(cipherText[index] - progression - keyword.charAt(index % keyword.length()), 26) + 'A');
		}
		
		return plainText;
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createKey(2, 15), RandomUtil.pickRandomInt(1, 20), RandomUtil.pickRandomInt(1, 25));
	}
}

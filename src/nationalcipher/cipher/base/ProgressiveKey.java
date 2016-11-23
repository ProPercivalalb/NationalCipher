package nationalcipher.cipher.base;

import javalibrary.math.MathUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.tools.KeyGeneration;

public class ProgressiveKey implements IRandEncrypter {

	public static String encode(String encodedText, String key, int period, int progressiveKey, VigenereType type) {
		String cipherText = "";

		for(int index = 0; index < encodedText.length(); index++) {
			int progression = (int)(index / period) * progressiveKey;
			cipherText += (char)type.encode((byte)(MathUtil.mod((encodedText.charAt(index) - 'A' + progression), 26) + 'A'), (byte)key.charAt(index % key.length()));
		}
		
		return cipherText;
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, String key, int period, int progressiveKey, VigenereType type) {
		int progression = 0;
		int count = 0;
		for(int index = 0; index < cipherText.length; index++) {
			plainText[index] = type.decode((byte)((26 + cipherText[index] - 'A' - progression) % 26 + 'A'), (byte)key.charAt(index % key.length()));
			if(count++ == period) {
				count = 0;
				progression = (progression + progressiveKey) % 26;
			}
		}
		
		return plainText;
	}
	
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(3, 15), RandomUtil.pickRandomInt(1, 20), RandomUtil.pickRandomInt(1, 25), RandomUtil.pickRandomElement(VigenereType.NORMAL_LIST));
	}
}

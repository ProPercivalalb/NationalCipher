package nationalcipher.cipher.base.substitution;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.ProgressiveKey;
import nationalcipher.cipher.tools.KeyGeneration;

public class BeaufortProgressiveKey implements IRandEncrypter {

	public static String encode(String plainText, String keyword, int period, int progressiveKey) {
		String encodedText = Beaufort.encode(plainText, keyword);
		return ProgressiveKey.encodeBase(encodedText, period, progressiveKey);
	}
	
	public static char[] decode(char[] cipherText, String key, int period, int progressiveKey) {
		char[] decodedText = ProgressiveKey.decodeBase(cipherText, period, progressiveKey);
		return Beaufort.decode(decodedText, key);
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15), RandomUtil.pickRandomInt(1, 20), RandomUtil.pickRandomInt(1, 25));
	}
}

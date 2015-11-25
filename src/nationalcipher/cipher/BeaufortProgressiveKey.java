package nationalcipher.cipher;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class BeaufortProgressiveKey implements IRandEncrypter {

	public static String encode(String plainText, String keyword, int period, int progressiveKey) {
		String encodedText = Beaufort.encode(plainText, keyword);
		return ProgressiveKey.encodeBase(encodedText, period, progressiveKey);
	}
	
	public static char[] decode(char[] plainText, String keyword, int period, int progressiveKey) {
		char[] decodedText = ProgressiveKey.decodeBase(plainText, period, progressiveKey);
		return Beaufort.decode(decodedText, keyword);
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createKey(2, 15), RandomUtil.pickRandomInt(1, 20), RandomUtil.pickRandomInt(1, 25));
	}
}

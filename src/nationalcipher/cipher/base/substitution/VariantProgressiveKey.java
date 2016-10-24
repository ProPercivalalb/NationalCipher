package nationalcipher.cipher.base.substitution;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.ProgressiveKey;
import nationalcipher.cipher.tools.KeyGeneration;

public class VariantProgressiveKey implements IRandEncrypter {

	public static String encode(String plainText, String keyword, int period, int progressiveKey) {
		String encodedText = Variant.encode(plainText, keyword);
		return ProgressiveKey.encodeBase(encodedText, period, progressiveKey);
	}
	
	public static char[] decode(char[] plainText, String keyword, int period, int progressiveKey) {
		char[] decodedText = ProgressiveKey.decodeBase(plainText, period, progressiveKey);
		return Variant.decode(decodedText, keyword);
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15), RandomUtil.pickRandomInt(1, 20), RandomUtil.pickRandomInt(1, 25));
	}
}
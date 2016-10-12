package nationalcipher.cipher;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.polygraphic.Porta;
import nationalcipher.cipher.tools.KeyGeneration;

public class PortaProgressiveKey implements IRandEncrypter {

	public static String encode(String plainText, String keyword, boolean shiftRight, int period, int progressiveKey) {
		String encodedText = Porta.encode(plainText, keyword, shiftRight);
		return ProgressiveKey.encodeBase(encodedText, period, progressiveKey);
	}
	
	public static char[] decode(char[] plainText, String keyword, boolean shiftRight, int period, int progressiveKey) {
		char[] decodedText = ProgressiveKey.decodeBase(plainText, period, progressiveKey);
		return Porta.decode(decodedText, keyword, shiftRight);
	}
	
	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 15), RandomUtil.pickBoolean(), RandomUtil.pickRandomInt(1, 20), RandomUtil.pickRandomInt(1, 25));
	}
}

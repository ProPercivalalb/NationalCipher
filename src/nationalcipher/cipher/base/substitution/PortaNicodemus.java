package nationalcipher.cipher.base.substitution;

import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.Nicodemus;
import nationalcipher.cipher.tools.KeyGeneration;

public class PortaNicodemus implements IRandEncrypter {
	
	public static String encode(String plainText, String key, boolean move) {
		return Nicodemus.encodeBase(Porta.encode(plainText, key, move), key);
	}
	
	public static char[] decode(char[] cipherText, String key, boolean move) {
		return Porta.decode(Nicodemus.decodeBase(cipherText, key), key, move);
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(3, 15), RandomUtil.pickBoolean());
	}
}

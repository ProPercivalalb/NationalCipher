package nationalcipher.cipher.base.substitution;

import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.Nicodemus;
import nationalcipher.cipher.tools.KeyGeneration;

public class VigenereNicodemus implements IRandEncrypter {
	
	public static String encode(String plainText, String key) {
		return Nicodemus.encodeBase(Vigenere.encode(plainText, key), key);
	}
	
	public static char[] decode(char[] cipherText, String key) {
		return Vigenere.decode(Nicodemus.decodeBase(cipherText, key), key);
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(3, 15));
	}
}

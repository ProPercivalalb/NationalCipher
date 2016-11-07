package nationalcipher.cipher.base.substitution;

import nationalcipher.cipher.base.Nicodemus;

public class VigenereNicodemus {
	
	public static char[] decode(char[] cipherText, String key) {
		return Vigenere.decode(Nicodemus.decodeBase(cipherText, key), key);
	}
}
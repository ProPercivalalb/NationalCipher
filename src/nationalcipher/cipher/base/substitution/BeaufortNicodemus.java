package nationalcipher.cipher.base.substitution;

import nationalcipher.cipher.base.Nicodemus;

public class BeaufortNicodemus {
	
	public static char[] decode(char[] cipherText, String key) {
		return Beaufort.decode(Nicodemus.decodeBase(cipherText, key), key);
	}
}

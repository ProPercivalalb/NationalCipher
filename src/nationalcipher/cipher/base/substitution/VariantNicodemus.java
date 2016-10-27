package nationalcipher.cipher.base.substitution;

import nationalcipher.cipher.base.Nicodemus;

public class VariantNicodemus {
	
	public static char[] decode(char[] cipherText, String key) {
		return Variant.decode(Nicodemus.decodeBase(cipherText, key), key);
	}
}

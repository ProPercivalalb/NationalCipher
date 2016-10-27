package nationalcipher.cipher.base.substitution;

import nationalcipher.cipher.base.Nicodemus;

public class PortaNicodemus {
	
	public static char[] decode(char[] cipherText, String key, boolean shiftRight) {
		return Porta.decode(Nicodemus.decodeBase(cipherText, key), key, shiftRight);
	}
}

package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.VigenereNicodemus;
import nationalcipher.cipher.decrypt.NicodemusAttack;

public class VigenereNCAttack extends NicodemusAttack {

	public VigenereNCAttack() {
		super("Nicodemus Vigenere");
	}

	@Override
	public char[] decode(char[] cipherText, String key) {
		return VigenereNicodemus.decode(cipherText, key);
	}
}

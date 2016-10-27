package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.BeaufortNicodemus;
import nationalcipher.cipher.decrypt.NicodemusAttack;

public class BeaufortNCAttack extends NicodemusAttack {

	public BeaufortNCAttack() {
		super("Nicodemus Beaufort");
	}

	@Override
	public char[] decode(char[] cipherText, String key) {
		return BeaufortNicodemus.decode(cipherText, key);
	}
}

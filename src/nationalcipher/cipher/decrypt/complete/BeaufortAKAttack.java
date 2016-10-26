package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.BeaufortAutokey;
import nationalcipher.cipher.decrypt.AutokeyAttack;

public class BeaufortAKAttack extends AutokeyAttack {

	public BeaufortAKAttack() {
		super("Beaufort Autokey");
	}

	@Override
	public char[] decode(char[] cipherText, String key) {
		return BeaufortAutokey.decode(cipherText, key);
	}
}

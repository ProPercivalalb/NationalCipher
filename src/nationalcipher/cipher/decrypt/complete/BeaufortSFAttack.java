package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.BeaufortSlidefair;
import nationalcipher.cipher.decrypt.SlidefairAttack;

public class BeaufortSFAttack extends SlidefairAttack {

	public BeaufortSFAttack() {
		super("Beaufort Slidefair");
	}

	@Override
	public char[] decode(char[] cipherText, String key) {
		return BeaufortSlidefair.decode(cipherText, key);
	}
}

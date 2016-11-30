package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.other.PeriodicGromark;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.decrypt.LongKeyAttack;

public class PeriodicGromarkAttack extends LongKeyAttack {

	public PeriodicGromarkAttack() {
		super("Periodic Gromark");
	}

	@Override
	public byte[] decode(char[] cipherText, byte[] plainText, String key) {
		return PeriodicGromark.decode(cipherText, plainText, key);
	}

}

package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.BeaufortProgressiveKey;
import nationalcipher.cipher.decrypt.ProgressiveKeyAttack;

public class BeaufortPKAttack extends ProgressiveKeyAttack {

	public BeaufortPKAttack() {
		super("Beaufort Progressive Key");
	}

	@Override
	public char[] decode(char[] cipherText, String key, int progPeriod, int progKey) {
		return BeaufortProgressiveKey.decode(cipherText, key, progPeriod, progKey);
	}
}

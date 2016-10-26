package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.VariantProgressiveKey;
import nationalcipher.cipher.decrypt.ProgressiveKeyAttack;

public class VariantPKAttack extends ProgressiveKeyAttack {

	public VariantPKAttack() {
		super("Variant Progressive Key");
	}

	@Override
	public char[] decode(char[] cipherText, String key, int progPeriod, int progKey) {
		return VariantProgressiveKey.decode(cipherText, key, progPeriod, progKey);
	}
}

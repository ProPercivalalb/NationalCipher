package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.VariantNicodemus;
import nationalcipher.cipher.decrypt.NicodemusAttack;

public class VariantNCAttack extends NicodemusAttack {

	public VariantNCAttack() {
		super("Nicodemus Variant");
	}

	@Override
	public char[] decode(char[] cipherText, String key) {
		return VariantNicodemus.decode(cipherText, key);
	}
}

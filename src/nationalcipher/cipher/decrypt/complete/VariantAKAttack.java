package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.VariantAutokey;
import nationalcipher.cipher.decrypt.AutokeyAttack;

public class VariantAKAttack extends AutokeyAttack {

	public VariantAKAttack() {
		super("Variant Autokey");
	}

	@Override
	public char[] decode(char[] cipherText, String key) {
		return VariantAutokey.decode(cipherText, key);
	}
}

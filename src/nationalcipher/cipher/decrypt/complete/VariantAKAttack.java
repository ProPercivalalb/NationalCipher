package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.substitution.Autokey;
import nationalcipher.cipher.decrypt.AutokeyAttack;

public class VariantAKAttack extends AutokeyAttack {

	public VariantAKAttack() {
		super("Variant Autokey");
	}

	@Override
	public byte[] decode(char[] cipherText, byte[] plainText, String key) {
		return Autokey.decode(cipherText, plainText, key, VigenereType.VARIANT);
	}
}

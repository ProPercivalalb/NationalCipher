package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.substitution.Nicodemus;
import nationalcipher.cipher.decrypt.NicodemusAttack;

public class VariantNCAttack extends NicodemusAttack {

	public VariantNCAttack() {
		super("Nicodemus Variant");
	}

	@Override
	public byte[] decode(char[] cipherText, byte[] plainText, String key) {
		return Nicodemus.decode(cipherText, plainText, key, VigenereType.VARIANT);
	}
}

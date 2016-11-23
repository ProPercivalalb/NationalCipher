package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.substitution.Autokey;
import nationalcipher.cipher.decrypt.AutokeyAttack;

public class BeaufortAKAttack extends AutokeyAttack {

	public BeaufortAKAttack() {
		super("Beaufort Autokey");
	}

	@Override
	public byte[] decode(char[] cipherText, byte[] plainText, String key) {
		return Autokey.decode(cipherText, plainText, key, VigenereType.BEAUFORT);
	}
}

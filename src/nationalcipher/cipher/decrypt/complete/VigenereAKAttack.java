package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.VigenereAutokey;
import nationalcipher.cipher.decrypt.AutokeyAttack;

public class VigenereAKAttack extends AutokeyAttack {

	public VigenereAKAttack() {
		super("Vigenere Autokey");
	}

	@Override
	public char[] decode(char[] cipherText, String key) {
		return VigenereAutokey.decode(cipherText, key);
	}
}

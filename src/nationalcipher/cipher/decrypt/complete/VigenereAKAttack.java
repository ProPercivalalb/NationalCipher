package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.substitution.Autokey;
import nationalcipher.cipher.decrypt.AutokeyAttack;

public class VigenereAKAttack extends AutokeyAttack {

	public VigenereAKAttack() {
		super("Vigenere Autokey");
	}

	@Override
	public byte[] decode(char[] cipherText, byte[] plainText, String key) {
		return Autokey.decode(cipherText, plainText, key, VigenereType.VIGENERE);
	}
}

package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.Nicodemus;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.decrypt.NicodemusAttack;

public class BeaufortNCAttack extends NicodemusAttack {

	public BeaufortNCAttack() {
		super("Nicodemus Beaufort");
	}

	@Override
	public byte[] decode(char[] cipherText, byte[] plainText, String key) {
		return Nicodemus.decode(cipherText, plainText, key, VigenereType.BEAUFORT);
	}
}

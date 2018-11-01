package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.substitution.Slidefair;
import nationalcipher.cipher.decrypt.SlidefairAttack;

public class BeaufortSFAttack extends SlidefairAttack {

	public BeaufortSFAttack() {
		super("Beaufort Slidefair");
	}

	@Override
	public byte[] decode(char[] cipherText, byte[] plainText, String key) {
		return Slidefair.decode(cipherText, plainText, key, VigenereType.BEAUFORT);
	}
}

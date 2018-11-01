package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.base.substitution.ProgressiveKey;
import nationalcipher.cipher.decrypt.ProgressiveKeyAttack;

public class BeaufortPKAttack extends ProgressiveKeyAttack {

	public BeaufortPKAttack() {
		super("Beaufort Progressive Key");
	}

	@Override
	public byte[] decode(char[] cipherText, byte[] plainText, String key, int progPeriod, int progKey) {
		return ProgressiveKey.decode(cipherText, plainText, key, progPeriod, progKey, VigenereType.BEAUFORT);
	}
}

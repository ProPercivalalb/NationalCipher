package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.VigenereProgressiveKey;
import nationalcipher.cipher.decrypt.ProgressiveKeyAttack;

public class VigenerePKAttack extends ProgressiveKeyAttack {

	public VigenerePKAttack() {
		super("Vigenere Progressive Key");
	}

	@Override
	public char[] decode(char[] cipherText, String key, int progPeriod, int progKey) {
		return VigenereProgressiveKey.decode(cipherText, key, progPeriod, progKey);
	}
}

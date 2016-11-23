package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.ProgressiveKey;
import nationalcipher.cipher.base.VigenereType;
import nationalcipher.cipher.decrypt.ProgressiveKeyAttack;

public class VigenerePKAttack extends ProgressiveKeyAttack {

	public VigenerePKAttack() {
		super("Vigenere Progressive Key");
	}

	@Override
	public byte[] decode(char[] cipherText, byte[] plainText, String key, int progPeriod, int progKey) {
		return ProgressiveKey.decode(cipherText, plainText, key, progPeriod, progKey, VigenereType.VIGENERE);
	}
}

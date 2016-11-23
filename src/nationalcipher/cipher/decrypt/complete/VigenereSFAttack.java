package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.VigenereSlidefair;
import nationalcipher.cipher.decrypt.SlidefairAttack;

public class VigenereSFAttack extends SlidefairAttack {

	public VigenereSFAttack() {
		super("Vigenere Slidefair");
	}

	@Override
	public byte[] decode(char[] cipherText, String key) {
		return VigenereSlidefair.decode(cipherText, key);
	}
}

package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.VariantSlidefair;
import nationalcipher.cipher.decrypt.SlidefairAttack;

public class VariantSFAttack extends SlidefairAttack {

	public VariantSFAttack() {
		super("Variant Slidefair");
	}

	@Override
	public byte[] decode(char[] cipherText, String key) {
		return VariantSlidefair.decode(cipherText, key);
	}
}

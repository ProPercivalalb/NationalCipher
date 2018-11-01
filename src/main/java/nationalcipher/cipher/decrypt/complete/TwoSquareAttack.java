package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.TwoSquare;
import nationalcipher.cipher.decrypt.DoubleKeySquareAttack;

public class TwoSquareAttack extends DoubleKeySquareAttack {

	public TwoSquareAttack() {
		super("Two Square");
	}

	@Override
	public byte[] decode(char[] cipherText, byte[] plainText, String key1, String key2) {
		return TwoSquare.decode(cipherText, plainText, key1, key2);
	}

}

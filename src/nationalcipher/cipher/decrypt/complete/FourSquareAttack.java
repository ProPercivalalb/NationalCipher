package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.FourSquare;
import nationalcipher.cipher.decrypt.DoubleKeySquareAttack;

public class FourSquareAttack extends DoubleKeySquareAttack {

	public FourSquareAttack() {
		super("Four Square");
	}

	@Override
	public char[] decode(char[] cipherText, char[] plainText, String key1, String key2) {
		return FourSquare.decode(cipherText, plainText, key1, key2);
	}

}

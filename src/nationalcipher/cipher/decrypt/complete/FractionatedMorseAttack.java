package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.FractionatedMorse;
import nationalcipher.cipher.decrypt.LongKeyAttack;

public class FractionatedMorseAttack extends LongKeyAttack {

	public FractionatedMorseAttack() {
		super("Fractionated Morse");
	}

	@Override
	public char[] decode(char[] cipherText, String key) {
		return FractionatedMorse.decode(cipherText, key);
	}
	
}

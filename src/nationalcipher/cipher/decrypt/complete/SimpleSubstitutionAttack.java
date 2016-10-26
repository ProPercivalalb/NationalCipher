package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.decrypt.LongKeyAttack;

public class SimpleSubstitutionAttack extends LongKeyAttack {

	public SimpleSubstitutionAttack() {
		super("Simple Substitution");
	}

	@Override
	public char[] decode(char[] cipherText, String key) {
		return Keyword.decode(cipherText, key);
	}
	
}

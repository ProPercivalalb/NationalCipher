package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.decrypt.LongKeyAttack;

public class SimpleSubstitutionAttack extends LongKeyAttack {

    public SimpleSubstitutionAttack() {
        super("Simple Substitution");
    }

    @Override
    public byte[] decode(char[] cipherText, byte[] plainText, String key) {
        return Keyword.decode(cipherText, plainText, key);
    }

}

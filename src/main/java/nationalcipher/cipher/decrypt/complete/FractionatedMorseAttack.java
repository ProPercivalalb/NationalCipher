package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.base.substitution.FractionatedMorse;
import nationalcipher.cipher.decrypt.LongKeyAttack;

public class FractionatedMorseAttack extends LongKeyAttack {

    public FractionatedMorseAttack() {
        super("Fractionated Morse");
    }

    @Override
    public byte[] decode(char[] cipherText, byte[] result, String key) {
        return FractionatedMorse.decode(cipherText, result, key);
    }

    @Override
    public int getOutputTextLength(int inputLength) {
        return inputLength * 3;
    }
}

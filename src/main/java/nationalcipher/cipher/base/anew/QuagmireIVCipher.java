package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.QuadKeyCipher;
import nationalcipher.cipher.base.Quagmire;
import nationalcipher.cipher.base.keys.FullStringKeyType;
import nationalcipher.cipher.base.keys.ObjectKeyType;
import nationalcipher.cipher.base.keys.QuadKey;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class QuagmireIVCipher extends QuadKeyCipher<String, String, String, Character, FullStringKeyType.Builder, FullStringKeyType.Builder, VariableStringKeyType.Builder, ObjectKeyType.Builder<Character>> {

    public QuagmireIVCipher() {
        super(FullStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS),
                FullStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS),
                VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(2, Integer.MAX_VALUE),
                ObjectKeyType.<Character>builder().setUniverse(KeyGeneration.ALL_26_CHARS));
    }
    
    @Override
    public VariableStringKeyType.Builder limitDomainForThirdKey(VariableStringKeyType.Builder thirdKey) {
        return thirdKey.setRange(2, 15);
    }

    @Override
    public CharSequence encode(CharSequence plainText, QuadKey<String, String, String, Character> key, IFormat format) {
        return Quagmire.encode(plainText, key.getFirstKey(), key.getSecondKey(), key.getThirdKey(), key.getFourthKey());
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, QuadKey<String, String, String, Character> key) {
        return Quagmire.decode(cipherText, plainText, key.getFirstKey(), key.getSecondKey(), key.getThirdKey(), key.getFourthKey());
    }
}

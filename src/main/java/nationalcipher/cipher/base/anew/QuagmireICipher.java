package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import javalibrary.lib.Alphabet;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.Quagmire;
import nationalcipher.cipher.base.TriKeyCipher;
import nationalcipher.cipher.base.keys.FullStringKeyType;
import nationalcipher.cipher.base.keys.ObjectKeyType;
import nationalcipher.cipher.base.keys.TriKey;
import nationalcipher.cipher.base.keys.VariableStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class QuagmireICipher extends TriKeyCipher<String, String, Character, FullStringKeyType.Builder, VariableStringKeyType.Builder, ObjectKeyType.Builder<Character>> {

    public QuagmireICipher() {
        super(FullStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS),
                VariableStringKeyType.builder().setAlphabet(KeyGeneration.ALL_26_CHARS).setRange(2, Integer.MAX_VALUE),
                ObjectKeyType.<Character>builder().setUniverse(KeyGeneration.ALL_26_CHARS));
    }
    
    @Override
    public VariableStringKeyType.Builder limitDomainForSecondKey(VariableStringKeyType.Builder secondKey) {
        return secondKey.setRange(2, 15);
    }

    @Override
    public CharSequence encode(CharSequence plainText, TriKey<String, String, Character> key, IFormat format) {
        return Quagmire.encode(plainText, key.getFirstKey(), Alphabet.getUpperCase(), key.getSecondKey(), key.getThirdKey());
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, TriKey<String, String, Character> key) {
        return Quagmire.decode(cipherText, plainText, key.getFirstKey(), Alphabet.getUpperCase(), key.getSecondKey(), key.getThirdKey());
    }
}

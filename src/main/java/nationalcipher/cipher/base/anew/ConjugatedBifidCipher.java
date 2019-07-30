package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.TriKeyCipher;
import nationalcipher.cipher.base.keys.IntegerGenKeyType;
import nationalcipher.cipher.base.keys.SquareStringKeyType;
import nationalcipher.cipher.base.keys.TriKey;
import nationalcipher.cipher.tools.KeyGeneration;

public class ConjugatedBifidCipher extends TriKeyCipher<String, String, Integer, SquareStringKeyType.Builder, SquareStringKeyType.Builder, IntegerGenKeyType.Builder> {

    public ConjugatedBifidCipher() {
        super(SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setDim(5, 5),
                SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setDim(5, 5),
                IntegerGenKeyType.builder().setRange(0, 5000).addFilter(i -> i != 1));  
    }
    
    @Override
    public IntegerGenKeyType.Builder limitDomainForThirdKey(IntegerGenKeyType.Builder thirdKey) {
        return thirdKey.setRange(0, 15);
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, TriKey<String, String, Integer> key) {
        StringBuilder builder = new StringBuilder(plainText.length());
        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            builder.append(c == 'J' ? 'I' : c);
        }

        return builder;
    }

    @Override
    public CharSequence encode(CharSequence plainText, TriKey<String, String, Integer> key, IFormat format) {
        return BifidCipher.encodeGeneral(plainText, key.getFirstKey(), key.getSecondKey(), key.getThirdKey(), format);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, TriKey<String, String, Integer> key) {
        return BifidCipher.decodeGeneral(cipherText, plainText, key.getFirstKey(), key.getSecondKey(), key.getThirdKey());
    }
}

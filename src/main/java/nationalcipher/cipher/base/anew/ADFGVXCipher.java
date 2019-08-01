package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.QuadKeyCipher;
import nationalcipher.cipher.base.keys.ConstantKeyType;
import nationalcipher.cipher.base.keys.EnumKeyType;
import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;
import nationalcipher.cipher.base.keys.QuadKey;
import nationalcipher.cipher.base.keys.SquareStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class ADFGVXCipher extends QuadKeyCipher<String, Integer[], String, ReadMode, SquareStringKeyType.Builder, OrderedIntegerKeyType.Builder, ConstantKeyType.Builder<String>, EnumKeyType.Builder<ReadMode>> {

    private static ADFGXCipher adfgx = new ADFGXCipher();

    public ADFGVXCipher() {
        super(SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_36_CHARS).setDim(6, 6), OrderedIntegerKeyType.builder().setMin(1).setMax(Integer.MAX_VALUE), ConstantKeyType.builder("ADFGVX"), EnumKeyType.builder(ReadMode.class).setUniverse(ReadMode.DOWN));
    }
    
    @Override
    public IKeyBuilder<Integer[]> limitDomainForSecondKey(OrderedIntegerKeyType.Builder secondKey) {
        return secondKey.setRange(2, 9);
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, QuadKey<String, Integer[], String, ReadMode> key) {
        StringBuilder builder = new StringBuilder(plainText.length());
        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            builder.append(c == 'J' ? 'I' : c);
        }

        return builder;
    }

    @Override
    public boolean isValid(QuadKey<String, Integer[], String, ReadMode> key) {
        return Math.sqrt(key.getFirstKey().length()) == key.getThirdKey().length() && super.isValid(key);
    }

    @Override
    public CharSequence encode(CharSequence plainText, QuadKey<String, Integer[], String, ReadMode> key, IFormat format) {
        return adfgx.encode(plainText, key, format);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, QuadKey<String, Integer[], String, ReadMode> key) {
        return adfgx.decodeEfficently(cipherText, plainText, key);
    }
}

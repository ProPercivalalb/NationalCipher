package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.QuadKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.ConstantKeyType;
import nationalcipher.cipher.base.keys.EnumKeyType;
import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;
import nationalcipher.cipher.base.keys.QuadKey;
import nationalcipher.cipher.base.keys.SquareStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class ADFGXCipher extends QuadKeyCipher<String, Integer[], String, ReadMode, SquareStringKeyType.Builder, OrderedIntegerKeyType.Builder, ConstantKeyType.Builder<String>, EnumKeyType.Builder<ReadMode>> {

    private static ColumnarTranspositionCipher transpostion = new ColumnarTranspositionCipher();

    public ADFGXCipher() {
        super(SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setDim(5, 5), OrderedIntegerKeyType.builder().setMin(1).setMax(Integer.MAX_VALUE), ConstantKeyType.builder("ADFGX"), EnumKeyType.builder(ReadMode.class).setUniverse(ReadMode.DOWN));
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
        int size = key.getThirdKey().length();

        StringBuilder cipherText = new StringBuilder(plainText.length() * 2);

        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);

            int charIndex = key.getFirstKey().indexOf(c);
            int row = (int) Math.floor((double) charIndex / size);
            int column = charIndex % size;

            cipherText.append(key.getThirdKey().charAt(row));
            cipherText.append(key.getThirdKey().charAt(column));
        }

        return transpostion.encode(cipherText, BiKey.of(key.getSecondKey(), key.getFourthKey()), format);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, QuadKey<String, Integer[], String, ReadMode> key) {
        return ADFGXCipher.decodeTransformed(transpostion.decodeEfficently(cipherText, plainText, BiKey.of(key.getSecondKey(), key.getFourthKey())), key.getFirstKey(), key.getThirdKey());
    }

    private static char[] decodeTransformed(char[] untransformedText, String keysquare, String adfgvx) {
        char[] plainText = new char[untransformedText.length / 2];

        for (int i = 0; i < untransformedText.length; i += 2) {
            char c1 = untransformedText[i];
            char c2 = untransformedText[i + 1];

            int row = adfgvx.indexOf(c1);
            int column = adfgvx.indexOf(c2);
            if (row != -1 && column != -1)
                plainText[i / 2] = keysquare.charAt(row * adfgvx.length() + column);
        }

        return plainText;
    }
}

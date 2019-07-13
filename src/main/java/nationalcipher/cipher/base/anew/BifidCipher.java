package nationalcipher.cipher.base.anew;

import java.util.Map;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.IntegerKeyType;
import nationalcipher.cipher.base.keys.SquareStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.util.CipherUtils;

public class BifidCipher extends BiKeyCipher<String, Integer> {

    public BifidCipher() {
        super(SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setDim(5, 5), IntegerKeyType.builder().setRange(1, 15)); // TODO
                                                                                                                                             // Add
                                                                                                                                             // 0
                                                                                                                                             // period
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, BiKey<String, Integer> key) {
        StringBuilder builder = new StringBuilder(plainText.length());
        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            builder.append(c == 'J' ? 'I' : c);
        }

        return builder;
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<String, Integer> key, IFormat format) {
        return BifidCipher.encodeGeneral(plainText, key.getFirstKey(), key.getFirstKey(), key.getSecondKey(), format);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<String, Integer> key) {
        return BifidCipher.decodeGeneral(cipherText, plainText, key.getFirstKey(), key.getFirstKey(), key.getSecondKey());
    }

    protected static CharSequence encodeGeneral(CharSequence plainText, String keysquare1, String keysquare2, int period, IFormat format) {
        if (period == 0)
            period = plainText.length();

        int[] digits = new int[plainText.length() * 2];
        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            int charIndex = keysquare1.indexOf(c);
            int charRow = (int) Math.floor(charIndex / 5);
            int charCol = charIndex % 5;

            int blockNo = (int) Math.floor(i / period);
            int blockSize = Math.min(period, plainText.length() - blockNo * period);
            int blockCol = (i - blockNo * period) % blockSize;

            digits[blockNo * period * 2 + blockCol] = charRow;
            digits[blockNo * period * 2 + blockSize + blockCol] = charCol;
        }

        StringBuilder cipherText = new StringBuilder(plainText.length());

        for (int i = 0; i < digits.length; i += 2) {
            int row = digits[i];
            int column = digits[i + 1];
            cipherText.append(keysquare2.charAt(row * 5 + column));
        }

        return cipherText;
    }

    protected static char[] decodeGeneral(CharSequence cipherText, @Nullable char[] plainText, String keysquare1, String keysquare2, int period) {
        if (period == 0)
            period = cipherText.length();

        Map<Character, Integer> keyIndex2 = CipherUtils.createCharacterIndexMapping(keysquare2);

        int[] numberText = new int[cipherText.length() * 2];
        for (int i = 0; i < cipherText.length(); i++) {

            char a = cipherText.charAt(i);
            int index = keyIndex2.get(a);
            int row = index / 5;
            int column = index % 5;

            int lowestCol = (int) (i / period) * period;
            int actualPeriod = Math.min(period, cipherText.length() - lowestCol);
            int blockSize = 2 * actualPeriod;
            int relCol = i - lowestCol;
            int blockBase = lowestCol * 2;

            int posMultipler = relCol * 4;

            if (relCol * 2 < actualPeriod)
                numberText[blockBase + posMultipler] = row;
            else
                numberText[blockBase + posMultipler - blockSize + 1] = row;

            if (relCol * 2 + 1 < actualPeriod)
                numberText[blockBase + posMultipler + 2] = column;
            else
                numberText[blockBase + posMultipler - blockSize + 3] = column;
        }

        int index = 0;

        for (int i = 0; i < numberText.length; i += 2) {
            int a = numberText[i];
            int b = numberText[i + 1];
            plainText[index++] = keysquare1.charAt(a * 5 + b);
        }

        return plainText;
    }
}

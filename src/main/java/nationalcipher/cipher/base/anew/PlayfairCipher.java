package nationalcipher.cipher.base.anew;

import java.util.Map;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.SquareStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.util.CipherUtils;
import nationalcipher.util.CharacterArrayWrapper;

public class PlayfairCipher extends UniKeyCipher<String, SquareStringKeyType.Builder> {

    public PlayfairCipher() {
        super(SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setDim(5, 5));
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, String key) {
        StringBuilder builder = new StringBuilder(plainText.length());
        int i = 0;
        for (; plainText.length() - i >= 2; i += 2) {
            char a = plainText.charAt(i);
            char b = plainText.charAt(i + 1);
            if (a == 'J')
                a = 'I';
            if (b == 'J')
                b = 'I';

            builder.append(a);

            if (a == b) {
                builder.append(a == 'X' ? 'Q' : 'X');
                i--;
            } else {
                builder.append(b);
            }
        }

        if (i < plainText.length()) {
            char c = plainText.charAt(plainText.length() - 1);
            if (c == 'J')
                c = 'I';
            builder.append(c);
        }

        if (builder.length() % 2 == 1) {
            int f = builder.charAt(builder.length() - 1);
            builder.append(f == 'X' ? 'Q' : 'X');
        }

        return builder.toString();
    }

    @Override
    public CharSequence encode(CharSequence plainText, String key, IFormat format) {
        Character[] cipherText = new Character[plainText.length()];

        int size = 5;
        for (int i = 0; i < plainText.length(); i += 2) {
            char a = plainText.charAt(i);
            char b = plainText.charAt(i + 1);
            int i1 = key.indexOf(a);
            int i2 = key.indexOf(b);
            int row1 = (int) Math.floor(i1 / size);
            int col1 = i1 % size;
            int row2 = (int) Math.floor(i2 / size);
            int col2 = i2 % size;

            char c, d;

            if (row1 == row2) {
                c = key.charAt(row1 * size + (col1 + 1) % size);
                d = key.charAt(row2 * size + (col2 + 1) % size);
            } else if (col1 == col2) {
                c = key.charAt((row1 + 1) % size * size + col1);
                d = key.charAt((row2 + 1) % size * size + col2);
            } else {
                c = key.charAt(row1 * size + col2);
                d = key.charAt(row2 * size + col1);
            }

            cipherText[i] = c;
            cipherText[i + 1] = d;
        }

        return new CharacterArrayWrapper(cipherText);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, String key) {
        Map<Character, Integer> keyIndex = CipherUtils.createCharacterIndexMapping(key);

        int size = 5;
        for (int i = 0; i < cipherText.length(); i += 2) {
            int i1 = keyIndex.get(cipherText.charAt(i));
            int i2 = keyIndex.get(cipherText.charAt(i + 1));
            int row1 = i1 / size;
            int col1 = i1 % size;
            int row2 = i2 / size;
            int col2 = i2 % size;

            if (row1 == row2) {
                plainText[i] = key.charAt(row1 * size + (col1 + size - 1) % size);
                plainText[i + 1] = key.charAt(row2 * size + (col2 + size - 1) % size);
            } else if (col1 == col2) {
                plainText[i] = key.charAt(((row1 + size - 1) % size) * size + col1);
                plainText[i + 1] = key.charAt(((row2 + size - 1) % size) * size + col2);
            } else {
                plainText[i] = key.charAt(row1 * size + col2);
                plainText[i + 1] = key.charAt(row2 * size + col1);
            }
        }

        return plainText;
    }
}

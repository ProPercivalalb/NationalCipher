package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.SquareStringKeyType;
import nationalcipher.cipher.tools.KeyGeneration;

public class FourSquareCipher extends BiKeyCipher<String, String> {

    public FourSquareCipher() {
        super(SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setDim(5, 5), SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setDim(5, 5));
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, BiKey<String, String> key) {
        StringBuilder builder = new StringBuilder(plainText.length());
        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            builder.append(c == 'J' ? 'I' : c);
        }

        if (builder.length() % 2 == 1) {
            builder.append('X');
            return builder;
        } else {
            return builder;
        }
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<String, String> key, IFormat format) {
        StringBuilder cipherText = new StringBuilder(plainText.length());

        String shortAlpha = "ABCDEFGHIKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < plainText.length(); i += 2) {
            char a = plainText.charAt(i);
            char b = plainText.charAt(i + 1);
            int aIndex = shortAlpha.indexOf(a);
            int bIndex = shortAlpha.indexOf(b);
            int aRow = (int) Math.floor(aIndex / 5);
            int bRow = (int) Math.floor(bIndex / 5);
            int aCol = aIndex % 5;
            int bCol = bIndex % 5;

            cipherText.append(key.getFirstKey().charAt(5 * aRow + bCol));
            cipherText.append(key.getSecondKey().charAt(5 * bRow + aCol));
        }

        return cipherText;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<String, String> key) {
        String shortAlpha = "ABCDEFGHIKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < cipherText.length(); i += 2) {
            char a = (char) cipherText.charAt(i);
            char b = (char) cipherText.charAt(i + 1);
            int aIndex = key.getFirstKey().indexOf(a);
            int bIndex = key.getSecondKey().indexOf(b);
            int aRow = aIndex / 5;
            int bRow = bIndex / 5;
            int aCol = aIndex % 5;
            int bCol = bIndex % 5;

            plainText[i] = shortAlpha.charAt(5 * aRow + bCol);
            plainText[i + 1] = shortAlpha.charAt(5 * bRow + aCol);
        }

        return plainText;
    }
}

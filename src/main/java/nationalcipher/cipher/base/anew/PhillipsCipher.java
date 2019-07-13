package nationalcipher.cipher.base.anew;

import java.util.Map;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.TriKeyCipher;
import nationalcipher.cipher.base.keys.BooleanKeyType;
import nationalcipher.cipher.base.keys.SquareStringKeyType;
import nationalcipher.cipher.base.keys.TriKey;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.util.CipherUtils;

public class PhillipsCipher extends TriKeyCipher<String, Boolean, Boolean> {

    public static int[][] rows = new int[][] { { 0, 1, 2, 3, 4 }, { 1, 0, 2, 3, 4 }, { 1, 2, 0, 3, 4 }, { 1, 2, 3, 0, 4 }, { 1, 2, 3, 4, 0 }, { 2, 1, 3, 4, 0 }, { 2, 3, 1, 4, 0 }, { 2, 3, 4, 1, 0 } };
    public static int[][] rowsIndex = new int[][] { { 0, 1, 2, 3, 4 }, { 1, 0, 2, 3, 4 }, { 2, 0, 1, 3, 4 }, { 3, 0, 1, 2, 4 }, { 4, 0, 1, 2, 3 }, { 4, 1, 0, 2, 3 }, { 4, 2, 0, 1, 3 }, { 4, 3, 0, 1, 2 } };

    public PhillipsCipher() {
        super(SquareStringKeyType.builder().setAlphabet(KeyGeneration.ALL_25_CHARS).setDim(5, 5), BooleanKeyType.builder(), BooleanKeyType.builder()); // TODO
                                                                                                                                                       // one
                                                                                                                                                       // of
                                                                                                                                                       // the
                                                                                                                                                       // booleans
                                                                                                                                                       // must
                                                                                                                                                       // be
                                                                                                                                                       // true
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, TriKey<String, Boolean, Boolean> key) {
        StringBuilder builder = new StringBuilder(plainText.length());
        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            builder.append(c == 'J' ? 'I' : c);
        }

        return builder;
    }

    @Override
    public CharSequence encode(CharSequence plainText, TriKey<String, Boolean, Boolean> key, IFormat format) {
        StringBuilder cipherText = new StringBuilder(plainText.length());

        for (int i = 0; i < plainText.length(); i++) {
            int squareIndex = ((int) (i / 5) % rows.length);

            int[] order = rows[squareIndex];
            int[] orderIndex = rowsIndex[squareIndex];

            char ch = plainText.charAt(i);

            int index = key.getFirstKey().indexOf(ch);

            int row = index / 5;
            int column = index % 5;

            int newRow;
            int newColumn;

            if (key.getSecondKey())
                newRow = order[(orderIndex[row] + 1) % 5];
            else
                newRow = (row + 1) % 5;

            if (key.getThirdKey())
                newColumn = order[(orderIndex[column] + 1) % 5];
            else
                newColumn = (column + 1) % 5;

            cipherText.append(key.getFirstKey().charAt(newRow * 5 + newColumn));
        }

        return new String(cipherText);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, TriKey<String, Boolean, Boolean> key) {

        Map<Character, Integer> keyIndex = CipherUtils.createCharacterIndexMapping(key.getFirstKey());

        for (int i = 0; i < cipherText.length(); i++) {
            int squareIndex = ((int) (i / 5) % rows.length);

            int[] order = rows[squareIndex];
            int[] orderIndex = rowsIndex[squareIndex];

            int index = keyIndex.get((char) cipherText.charAt(i));

            int row = index / 5;
            int column = index % 5;

            int newRow;
            int newColumn;

            if (key.getSecondKey())
                newRow = order[(orderIndex[row] + 4) % 5];
            else
                newRow = (row + 4) % 5;

            if (key.getThirdKey())
                newColumn = order[(orderIndex[column] + 4) % 5];
            else
                newColumn = (column + 4) % 5;

            plainText[i] = key.getFirstKey().charAt(newRow * 5 + newColumn);
        }

        return plainText;
    }
}

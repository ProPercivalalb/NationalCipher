package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import javalibrary.util.ArrayUtil;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.BooleanKeyType;
import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;

public class AMSCOCipher extends BiKeyCipher<Integer[], Boolean> {

    public AMSCOCipher() {
        super(OrderedIntegerKeyType.builder().setRange(2, 9), BooleanKeyType.builder());
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<Integer[], Boolean> key, IFormat format) {
        StringBuilder[] columns = ArrayUtil.fill(new StringBuilder[key.getFirstKey().length], () -> new StringBuilder(plainText.length() / key.getFirstKey().length));

        int index = 0;
        int row = 0;
        while (index < plainText.length()) {
            for (int col = 0; col < key.getFirstKey().length; col++) {
                if (index >= plainText.length())
                    break;

                int fromIndex = index;
                // How many characters are in block
                index += ((col + row) % 2 == (key.getSecondKey() ? 0 : 1)) ? 2 : 1;

                for (int i = fromIndex; i < Math.min(index, plainText.length()); i++) {
                    columns[key.getFirstKey()[col]].append(plainText.charAt(i));
                }
            }
            row++;
        }

        StringBuilder read = new StringBuilder(plainText.length());
        for (int i = 0; i < key.getFirstKey().length; i++)
            read.append(columns[i]);

        return read;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<Integer[], Boolean> key) {
        Integer[] order = key.getFirstKey();
        int period = order.length;

        int[] reversedOrder = new int[order.length];
        for (int i = 0; i < order.length; i++)
            reversedOrder[order[i]] = i;

        int noChar1st = (int) ((period + 1) / 2) * 2 + (int) (period / 2);
        int noChar2nd = (int) ((period + 1) / 2) + (int) (period / 2) * 2;

        int rows = 0;
        int charactersLastRow = 0;

        boolean choose = key.getSecondKey();
        int chars = 0;

        do {
            charactersLastRow = chars;
            chars += choose ? noChar1st : noChar2nd;
            rows += 1;
            choose = !choose;
        } while (chars < cipherText.length());

        charactersLastRow = plainText.length - charactersLastRow;

        int noCharColumn1st = (int) ((rows - 1) / 2) + (int) (rows / 2) * 2;
        int noCharColumn2nd = (int) ((rows - 1) / 2) * 2 + (int) (rows / 2);

        int index = 0;

        CharSequence[] grid = new CharSequence[period];

        for (int column = 0; column < period; column++) {
            int realColumn = reversedOrder[column];
            boolean isDoubleLetter = ((realColumn + rows) % 2 == (key.getSecondKey() ? 0 : 1)); // Double letter first

            int length = isDoubleLetter ? noCharColumn1st : noCharColumn2nd;
            int noCharN1st = (int) ((realColumn + 1) / 2) * 2 + (int) (realColumn / 2);
            int noCharN2nd = (int) ((realColumn + 1) / 2) + (int) (realColumn / 2) * 2;

            int left = charactersLastRow - (isDoubleLetter ? noCharN1st : noCharN2nd);
            if (left > 0)
                length += isDoubleLetter ? 1 : Math.min(2, left);

            grid[realColumn] = cipherText.subSequence(index, index + length);
            index += length;
        }

        int[] indexTracker = new int[period];

        int textIndex = 0;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < period; column++) {
                int number = (column + row) % 2 == (key.getSecondKey() ? 0 : 1) ? 2 : 1;

                for (int i = 0; i < number; i++) {
                    if (indexTracker[column] + i >= grid[column].length())
                        break;
                    plainText[textIndex] = grid[column].charAt(indexTracker[column] + i);
                    textIndex++;
                }

                indexTracker[column] += number;
            }
        }

        return plainText;
    }
}

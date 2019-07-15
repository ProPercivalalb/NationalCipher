package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import javalibrary.util.ArrayUtil;
import nationalcipher.api.IFormat;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.EnumKeyType;
import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;

public class ColumnarTranspositionCipher extends BiKeyCipher<Integer[], ReadMode, OrderedIntegerKeyType.Builder, EnumKeyType.Builder<ReadMode>> {

    public ColumnarTranspositionCipher() {
        super(OrderedIntegerKeyType.builder().setRange(2, Integer.MAX_VALUE),
                EnumKeyType.builder(ReadMode.class).setUniverse(ReadMode.values()));
    }

    @Override
    public IKeyBuilder<Integer[]> limitDomainForFirstKey(OrderedIntegerKeyType.Builder secondKey) {
        return secondKey.setRange(2, 9);
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<Integer[], ReadMode> key, IFormat format) {
        Integer[] order = key.getFirstKey();

        StringBuilder[] columns = ArrayUtil.fill(new StringBuilder[key.getFirstKey().length], () -> new StringBuilder(plainText.length() / key.getFirstKey().length));

        int index = 0;
        label: while (true) {
            for (int col = 0; col < order.length; col++) {
                if (index >= plainText.length())
                    break label;

                columns[order[col]].append(plainText.charAt(Math.min(index++, plainText.length() - 1)));
            }
        }
        StringBuilder read = new StringBuilder(plainText.length());

        switch (key.getSecondKey()) {
        case ACROSS:
            int rows = (int) Math.ceil((double) plainText.length() / order.length);
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < order.length; col++) {
                    if (row < columns[col].length()) {
                        read.append(columns[col].charAt(row));
                    }
                }
            }
            break;
        case DOWN:
            for (int i = 0; i < order.length; i++) {
                read.append(columns[i]);
            }
            break;
        }

        return read;
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<Integer[], ReadMode> key) {
        Integer[] order = key.getFirstKey();

        Integer[] orderIndex = ArrayUtil.toIndexedArray(order);
        int period = order.length;
        int rows = (int) Math.ceil(cipherText.length() / (double) period);

        int index = 0;
        switch (key.getSecondKey()) {
        case DOWN:
            for (int col = 0; col < period; col++) {
                int trueColumn = orderIndex[col];
                for (int row = 0; row < rows; row++) {
                    if (row * period + trueColumn >= cipherText.length())
                        continue;

                    if (index >= cipherText.length())
                        break;

                    plainText[row * period + trueColumn] = cipherText.charAt(index++);
                }
            }
            break;
        case ACROSS:
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < period; col++) { // Swapped is all that needs to happen
                    int trueColumn = orderIndex[col];
                    if (row * period + trueColumn >= cipherText.length())
                        continue;

                    if (index >= cipherText.length())
                        break;

                    plainText[row * period + trueColumn] = cipherText.charAt(index++);
                }
            }
            break;
        }

        return plainText;
    }
}

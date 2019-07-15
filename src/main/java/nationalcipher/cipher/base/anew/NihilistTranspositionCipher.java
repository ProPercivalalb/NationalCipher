package nationalcipher.cipher.base.anew;

import javax.annotation.Nullable;

import nationalcipher.api.IFormat;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.EnumKeyType;
import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;

public class NihilistTranspositionCipher extends BiKeyCipher<Integer[], ReadMode, OrderedIntegerKeyType.Builder, EnumKeyType.Builder<ReadMode>> {

    public NihilistTranspositionCipher() {
        super(OrderedIntegerKeyType.builder().setRange(2, Integer.MAX_VALUE),
                EnumKeyType.builder(ReadMode.class).setUniverse(ReadMode.values()));
    }

    @Override
    public IKeyBuilder<Integer[]> limitDomainForFirstKey(OrderedIntegerKeyType.Builder firstKey) {
        return firstKey.setRange(2, 7);
    }

    @Override
    public CharSequence padPlainText(CharSequence plainText, BiKey<Integer[], ReadMode> key) {
        int blockSize = key.getFirstKey().length * key.getFirstKey().length;

        if (plainText.length() % blockSize != 0) {
            StringBuilder builder = new StringBuilder(plainText.length() + blockSize - plainText.length() % blockSize);
            builder.append(plainText);
            while (builder.length() % blockSize != 0) {
                builder.append('X');
            }

            return builder;
        } else {
            return plainText;
        }
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<Integer[], ReadMode> key, IFormat format) {
        int blockSize = key.getFirstKey().length * key.getFirstKey().length;

        if (plainText.length() != blockSize) {
            String combinedMulti = "";
            for (int i = 0; i < plainText.length() / (blockSize); i++)
                combinedMulti += encode(plainText.subSequence(i * blockSize, (i + 1) * blockSize), key, format);
            return combinedMulti;
        } else {
            int index = 0;
            int columns = key.getFirstKey().length;
            char[] cipherText = new char[blockSize];
            switch (key.getSecondKey()) {
            case ACROSS:
                for (int row = 0; row < key.getFirstKey().length; row++)
                    for (int column = 0; column < key.getFirstKey().length; column++)
                        cipherText[index++] = plainText.charAt(key.getFirstKey()[row] * columns + key.getFirstKey()[column]);
                break;
            case DOWN:
                for (int column = 0; column < key.getFirstKey().length; column++) {
                    for (int row = 0; row < key.getFirstKey().length; row++) {
                        cipherText[index++] = plainText.charAt(key.getFirstKey()[row] * columns + key.getFirstKey()[column]);
                    }
                }
                break;
            }
            return new String(cipherText);
        }
    }

    // TODO decode longer texts
    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<Integer[], ReadMode> key) {

        int index = 0;
        int columns = key.getFirstKey().length;

        int[] reversedOrder = new int[columns];
        for (int i = 0; i < columns; i++)
            reversedOrder[key.getFirstKey()[i]] = i;

        switch (key.getSecondKey()) {
        case ACROSS:
            for (int row = 0; row < key.getFirstKey().length; row++) {
                for (int column = 0; column < key.getFirstKey().length; column++) {
                    plainText[index++] = cipherText.charAt(reversedOrder[row] * columns + reversedOrder[column]);
                }
            }
            break;
        case DOWN:
            for (int column = 0; column < key.getFirstKey().length; column++) {
                for (int row = 0; row < key.getFirstKey().length; row++) {
                    plainText[index++] = cipherText.charAt(reversedOrder[row] * columns + reversedOrder[column]);
                }
            }
            break;
        }

        return plainText;
    }
}

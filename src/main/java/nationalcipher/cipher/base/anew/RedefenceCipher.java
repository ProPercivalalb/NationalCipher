package nationalcipher.cipher.base.anew;

import java.util.function.IntFunction;

import javax.annotation.Nullable;

import javalibrary.util.ArrayUtil;
import nationalcipher.api.IFormat;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.IntegerKeyType;
import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;

public class RedefenceCipher extends BiKeyCipher<Integer[], Integer, OrderedIntegerKeyType.Builder, IntegerKeyType.Builder> {

    // TODO Add read off diagonals mode

    public RedefenceCipher() {
        super(OrderedIntegerKeyType.builder().setRange(2, Integer.MAX_VALUE), IntegerKeyType.builder().setMin(0).setVariableMax(obj -> ((BiKey<Integer[], Integer>) obj).getFirstKey().length * 2 - 2));
    }
    
    @Override
    public IKeyBuilder<Integer[]> limitDomainForFirstKey(OrderedIntegerKeyType.Builder firstKey) {
        return firstKey.setRange(2, 9);
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<Integer[], Integer> key, IFormat format) {
        return RedefenceCipher.encodeGeneral(plainText, key.getFirstKey().length, key.getSecondKey(), i -> key.getFirstKey()[i]);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<Integer[], Integer> key) {
        return RedefenceCipher.decodeGeneral(cipherText, plainText, key.getFirstKey().length, key.getSecondKey(), i -> key.getFirstKey()[i]);
    }

    protected static CharSequence encodeGeneral(CharSequence plainText, int rows, int startingOffset, IntFunction<Integer> takeOffOrder) {
        StringBuilder[] rails = ArrayUtil.fill(new StringBuilder[rows], StringBuilder::new);

        int branchTotal = rows * 2 - 2;

        for (int i = 0; i < plainText.length(); ++i) {
            char character = plainText.charAt(i);
            int index_in_ite = (i + startingOffset) % branchTotal;
            if (index_in_ite < rows) {
                rails[index_in_ite].append(character);
            } else {
                rails[rows - (index_in_ite - rows) - 2].append(character);
            }
        }

        StringBuilder cipherText = new StringBuilder();

        for (int i = 0; i < rows; i++) {
            cipherText.append(rails[takeOffOrder.apply(i)]);
        }

        return cipherText;
    }

    protected static char[] decodeGeneral(CharSequence cipherText, @Nullable char[] plainText, int rows, int startingOffset, IntFunction<Integer> takeOffOrder) {

        int ghostLength = cipherText.length() + startingOffset;

        int branchTotal = 2 * (rows - 1);
        int branchs = ghostLength / branchTotal;
        int noUnassigned = ghostLength - (branchs * branchTotal);

        int index = 0;
        for (int k = 0; k < rows; k++) {
            if (index >= cipherText.length())
                break;
            int row = takeOffOrder.apply(k) + 1;

            int occurs = branchs; // Times a letter occurs in a row

            if (row > 1 && row < rows)
                occurs *= 2;

            if (noUnassigned >= row) {
                occurs += 1;
                if (row < rows && row + (rows - row) * 2 <= noUnassigned)
                    occurs += 1;
            }

            if (startingOffset >= row) {
                occurs -= 1;
                if (row < rows && row + (rows - row) * 2 <= startingOffset)
                    occurs -= 1;
            }

            for (int i = 0; i < occurs; i++) {
                int newIndex = 0;

                if (row > 1 && row < rows) {
                    int branch2 = i;
                    if (startingOffset >= row) {
                        branch2 += 1;
                        if (row < rows && row + (rows - row) * 2 <= startingOffset)
                            branch2 += 1;
                    }

                    int branch = (int) (branch2 / 2);
                    newIndex = branch * branchTotal + row - 1 - startingOffset;
                    if (branch2 % 2 == 1)
                        newIndex += (rows - row) * 2;
                    plainText[newIndex] = cipherText.charAt(index++);
                } else {
                    int branch = i;
                    if (startingOffset >= row)
                        branch += 1;
                    newIndex = branch * branchTotal + row - 1 - startingOffset;
                    plainText[newIndex] = cipherText.charAt(index++);
                }

            }
        }

        return plainText;
    }
}

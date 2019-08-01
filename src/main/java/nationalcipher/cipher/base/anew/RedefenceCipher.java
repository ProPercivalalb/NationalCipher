package nationalcipher.cipher.base.anew;

import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.IntFunction;

import javax.annotation.Nullable;

import javalibrary.math.MathUtil;
import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.ICipher;
import nationalcipher.api.IFormat;
import nationalcipher.cipher.base.KeyFunction;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.OrderedIntegerKeyType;

public class RedefenceCipher implements ICipher<BiKey<Integer[], Integer>> {

    // TODO Add read off diagonals mode


    protected final OrderedIntegerKeyType firstType;
    private OrderedIntegerKeyType firstTypeLimit;
    private final OrderedIntegerKeyType.Builder firstKeyBuilder;
    
    public RedefenceCipher() {
        OrderedIntegerKeyType.Builder firstKey = OrderedIntegerKeyType.builder().setRange(2, Integer.MAX_VALUE / 2 - 2);
        this.firstType = firstKey.create();
        this.firstTypeLimit = firstKey.setRange(2, 9).create();
        this.firstKeyBuilder = firstKey;
    }
    
    @Override
    public boolean isValid(BiKey<Integer[], Integer> key) {
        return this.firstType.isValid(key.getFirstKey()) && 0 <= key.getSecondKey() && key.getSecondKey() <= (key.getSecondKey() - 1) * 2 ;
    }

    @Override
    public BiKey<Integer[], Integer> randomiseKey() {
        Integer[] rails = this.firstTypeLimit.randomise();
        return BiKey.of(rails, RandomUtil.pickRandomInt(0, (rails.length - 1) * 2));
    }

    @Override
    public void iterateKeys(KeyFunction<BiKey<Integer[], Integer>> consumer) {
        this.firstTypeLimit.iterateKeys(f -> {
            Integer[] fCopy = ArrayUtil.copy(f);
            for (int s = 0; s < (f.length - 1) * 2; s++) {
                if (!consumer.apply(BiKey.of(fCopy, s))) {
                    return false;
                }
            }
            return true;
        });
    }

    @Override
    public BiKey<Integer[], Integer> alterKey(BiKey<Integer[], Integer> key, double temp, int count) {
        return key;
    }

    @Override
    public BigInteger getNumOfKeys() {
        BigInteger total = BigInteger.ZERO;
        for (int i = this.firstTypeLimit.getMin(); i <= this.firstTypeLimit.getMax(); i++) {
            total = total.add(MathUtil.factorialBig(i).multiply(BigInteger.valueOf((i - 1) * 2)));
        }
        
        return total;
    }
    
    @Override
    public String prettifyKey(BiKey<Integer[], Integer> key) {
        return String.join(" ",  this.firstType.prettifyKey(key.getFirstKey()), String.valueOf(key.getSecondKey()));
    }
    
    public void setFirstKeyLimit(Function<OrderedIntegerKeyType.Builder, OrderedIntegerKeyType.Builder> firstKeyFunc) {
        this.firstTypeLimit = firstKeyFunc.apply(this.firstKeyBuilder).create();
    }
    
    public OrderedIntegerKeyType getFirstKeyType() {
        return this.firstTypeLimit;
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

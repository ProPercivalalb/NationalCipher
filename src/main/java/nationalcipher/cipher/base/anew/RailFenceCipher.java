package nationalcipher.cipher.base.anew;

import java.math.BigInteger;
import java.util.function.Function;

import javax.annotation.Nullable;

import javalibrary.util.RandomUtil;
import nationalcipher.api.ICipher;
import nationalcipher.api.IFormat;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.BiKeyCipher;
import nationalcipher.cipher.base.KeyFunction;
import nationalcipher.cipher.base.UniKeyCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.base.keys.IntegerKeyType;

public class RailFenceCipher implements ICipher<BiKey<Integer, Integer>> {

    // TODO Add read off diagonals mode

    protected final IntegerKeyType firstType;
    private IntegerKeyType firstTypeLimit;
    private final IntegerKeyType.Builder firstKeyBuilder;
    
    public RailFenceCipher() {
        IntegerKeyType.Builder firstKey = IntegerKeyType.builder().setRange(2, Integer.MAX_VALUE / 2 - 2);
        this.firstType = firstKey.create();
        this.firstTypeLimit = firstKey.setRange(2, 50).create();
        this.firstKeyBuilder = firstKey;
    }
    
    @Override
    public boolean isValid(BiKey<Integer, Integer> key) {
        return this.firstType.isValid(key.getFirstKey()) && 0 <= key.getSecondKey() && key.getSecondKey() < (key.getSecondKey() - 1) * 2 ;
    }

    @Override
    public BiKey<Integer, Integer> randomiseKey() {
        int rails = this.firstTypeLimit.randomise();
        return BiKey.of(rails, RandomUtil.pickRandomInt(0, (rails - 1) * 2) - 1);
    }

    @Override
    public void iterateKeys(KeyFunction<BiKey<Integer, Integer>> consumer) {
        this.firstTypeLimit.iterateKeys(f -> {
            for (int s = 0; s < (f - 1) * 2; s++) {
                if (!consumer.apply(BiKey.of(f, s))) {
                    return false;
                }
            }
            return true;
        });
    }

    @Override
    public BiKey<Integer, Integer> alterKey(BiKey<Integer, Integer> key, double temp, int count) {
        return key;
    }

    @Override
    public BigInteger getNumOfKeys() {
        BigInteger total = BigInteger.ZERO;
        for (int i = this.firstTypeLimit.getMin(); i <= this.firstTypeLimit.getMax(); i++) {
            total = total.add(BigInteger.valueOf(i).multiply(BigInteger.valueOf(i).subtract(BigInteger.ONE).multiply(BigInteger.valueOf(2))));
        }
        
        return total;
    }
    
    @Override
    public String prettifyKey(BiKey<Integer, Integer> key) {
        return String.join(" ",  this.firstType.prettifyKey(key.getFirstKey()), String.valueOf(key.getSecondKey()));
    }
    
    public void setFirstKeyLimit(Function<IntegerKeyType.Builder, IntegerKeyType.Builder> firstKeyFunc) {
        this.firstTypeLimit = firstKeyFunc.apply(this.firstKeyBuilder).create();
    }
    
    public IntegerKeyType getFirstKeyType() {
        return this.firstTypeLimit;
    }

    @Override
    public CharSequence encode(CharSequence plainText, BiKey<Integer, Integer> key, IFormat format) {
        return RedefenceCipher.encodeGeneral(plainText, key.getFirstKey(), key.getSecondKey(), i -> i);
    }

    @Override
    public char[] decodeEfficently(CharSequence cipherText, @Nullable char[] plainText, BiKey<Integer, Integer> key) {
        return RedefenceCipher.decodeGeneral(cipherText, plainText, key.getFirstKey(), key.getSecondKey(), i -> i);
    }
}

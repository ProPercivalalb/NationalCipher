package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

import javalibrary.math.MathUtil;
import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IRangedKeyBuilder;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;

public class OrderedIntegerKeyType implements IKeyType<Integer[]> {

    // Both inclusive
    private final int min, max;

    private OrderedIntegerKeyType(int min, int max, boolean repeats) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer[] randomise(Object partialKey) {
        return KeyGeneration.createOrder(RandomUtil.pickRandomInt(this.min, this.max));
    }

    @Override
    public boolean isValid(Object fullKey, Integer[] key) {
        for (int i = 0; i < key.length; i++) {
            if (!ArrayUtil.contains(key, i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void iterateKeys(Object partialKey, Consumer<Integer[]> consumer) {
        for (int length = this.min; length <= this.max; length++) {
            KeyIterator.iterateIntegerArray(consumer, length, length, false);
        }
    }

    @Override
    public Integer[] alterKey(Object partialKey, Integer[] key) {
        return KeyManipulation.modifyOrder(key);
    }

    @Override
    public String prettifyKey(Integer[] key) {
        return Arrays.toString(key);
    }

    @Override
    public BigInteger getNumOfKeys() {
        BigInteger total = BigInteger.ZERO;
        for (int length = this.min; length <= this.max; ++length)
            total = total.add(MathUtil.factorialBig(length));
        return total;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IRangedKeyBuilder<Integer[]> {

        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();
        private boolean repeats = false;

        private Builder() {
        }

        @Override
        public Builder setMin(int min) {
            this.min = Optional.of(min);
            return this;
        }

        @Override
        public Builder setMax(int max) {
            this.max = Optional.of(max);
            return this;
        }
        
        @Override
        public Builder setRange(int min, int max) {
            return this.setMin(min).setMax(max);
        }

        @Override
        public Builder setSize(int size) {
            return this.setRange(size, size);
        }

        public Builder setRepeats() {
            this.repeats = true;
            return this;
        }

        @Override
        public OrderedIntegerKeyType create() {
            OrderedIntegerKeyType handler = new OrderedIntegerKeyType(this.min.orElse(2), this.max.orElse(6), this.repeats);
            return handler;
        }
    }
}

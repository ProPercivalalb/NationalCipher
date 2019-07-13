package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;

public class IntegerKeyType implements IKeyType<Integer> {

    // Both inclusive
    private final Function<Object, Integer> min, max;
    private final boolean alterable;

    private IntegerKeyType(Function<Object, Integer> min, Function<Object, Integer> max, boolean alterable) {
        this.min = min;
        this.max = max;
        this.alterable = alterable;
    }

    @Override
    public Integer randomise(Object partialKey) {
        return RandomUtil.pickRandomInt(this.min.apply(partialKey), this.max.apply(partialKey));
    }

    @Override
    public boolean isValid(Object partialKey, Integer key) {
        return this.min.apply(partialKey) <= key && key <= this.max.apply(partialKey);
    }

    @Override
    public void iterateKeys(Object partialKey, Consumer<Integer> consumer) {
        int min = this.min.apply(partialKey);
        int max = this.max.apply(partialKey);
        for (int i = min; i <= max; i++) {
            consumer.accept(i);
        }
    }

    @Override
    public Integer alterKey(Object partialKey, Integer key) {
        return this.alterable ? RandomUtil.pickRandomInt(this.min.apply(partialKey), this.max.apply(partialKey)) : key;
    }

    @Override
    public BigInteger getNumOfKeys() { // TODO
        return BigInteger.ONE;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IKeyBuilder<Integer, IntegerKeyType> {

        private Optional<Function<Object, Integer>> min = Optional.empty();
        private Optional<Function<Object, Integer>> max = Optional.empty();
        private boolean alterable = false;

        private Builder() {
        }

        public Builder setMin(int min) {
            return this.setVariableMin((obj) -> min);
        }

        public Builder setMax(int max) {
            return this.setVariableMax((obj) -> max);
        }

        public Builder setRange(int min, int max) {
            this.setMin(min);
            this.setMax(max);
            return this;
        }

        public Builder setVariableMin(Function<Object, Integer> supp) {
            this.min = Optional.of(supp);
            return this;
        }

        public Builder setVariableMax(Function<Object, Integer> supp) {
            this.max = Optional.of(supp);
            return this;
        }

        public Builder setAlterable() {
            this.alterable = true;
            return this;
        }

        @Override
        public IntegerKeyType create() {
            IntegerKeyType handler = new IntegerKeyType(this.min.orElse((obj) -> Integer.MIN_VALUE), this.max.orElse((obj) -> Integer.MAX_VALUE), this.alterable);
            return handler;
        }

    }
}

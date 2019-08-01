package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Optional;

import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IRangedKeyBuilder;
import nationalcipher.cipher.base.KeyFunction;

public class IntegerKeyType implements IKeyType<Integer> {

    // Both inclusive
    private final int min, max;
    private final boolean alterable;

    private IntegerKeyType(int min, int max, boolean alterable) {
        this.min = min;
        this.max = max;
        this.alterable = alterable;
    }

    @Override
    public Integer randomise() {
        return RandomUtil.pickRandomInt(this.min, this.max);
    }

    @Override
    public boolean isValid(Integer key) {
        return this.min <= key && key <= this.max;
    }

    @Override
    public boolean iterateKeys(KeyFunction<Integer> consumer) {
        for (int i = this.min; i <= this.max; i++) {
            if (!consumer.apply(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Integer alterKey(Integer key) {
        return this.alterable ? RandomUtil.pickRandomInt(this.min, this.max) : key;
    }

    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.valueOf(this.max).subtract(BigInteger.valueOf(this.min)).add(BigInteger.ONE);
    }

    @Override
    public Integer parse(String input) throws ParseException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new ParseException(input, 0);
        }
    }
    
    @Override
    public String getHelp() {
        StringBuilder builder = new StringBuilder();
        return this.min+ "-"+this.max;
    }
    
    public int getMin() {
        return this.min;
    }
    
    public int getMax() {
        return this.max;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IRangedKeyBuilder<Integer> {

        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();
        private boolean alterable = false;

        private Builder() {
        }

        @Override
        public Builder setRange(int min, int max) {
            return this.setMin(min).setMax(max);
        }

        @Override
        public Builder setSize(int size) {
            return this.setRange(size, size);
        }

        @Override
        public Builder setMin(int supp) {
            this.min = Optional.of(supp);
            return this;
        }

        @Override
        public Builder setMax(int supp) {
            this.max = Optional.of(supp);
            return this;
        }

        public Builder setAlterable() {
            this.alterable = true;
            return this;
        }

        @Override
        public IntegerKeyType create() {
            IntegerKeyType handler = new IntegerKeyType(this.min.orElse(Integer.MIN_VALUE), this.max.orElse(Integer.MAX_VALUE), this.alterable);
            return handler;
        }

    }
}

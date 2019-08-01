package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IRangedKeyBuilder;
import nationalcipher.cipher.base.KeyFunction;

public class IntegerGenKeyType implements IKeyType<Integer> {

    // Both inclusive
    private final List<Integer> universe;
    private boolean alterable;

    private IntegerGenKeyType(List<Integer> universe, boolean alterable) {
        this.universe = universe;
        this.alterable = alterable;
    }

    @Override
    public Integer randomise() {
        return RandomUtil.pickRandomElement(this.universe);
    }

    @Override
    public boolean isValid(Integer key) {
        return this.universe.contains(key);
    }

    @Override
    public boolean iterateKeys(KeyFunction<Integer> consumer) {
        for (Integer atom : universe) {
            if (!consumer.apply(atom)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Integer alterKey(Integer key) {
        return this.alterable ? RandomUtil.pickRandomElement(this.universe) : key;
    }

    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.valueOf(this.universe.size());
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
        return "int";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IRangedKeyBuilder<Integer> {

        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();
        private Predicate<Integer> filter = null;
        private boolean alterable = false;

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

        public Builder addFilter(Predicate<Integer> filterIn) {
            this.filter = this.filter == null ? filterIn : this.filter.and(filterIn);
            return this;
        }

        public Builder setAlterable() {
            this.alterable = true;
            return this;
        }

        @Override
        public IntegerGenKeyType create() {
            List<Integer> universe = new ArrayList<Integer>();
            int min = this.min.orElse(Integer.MIN_VALUE);
            int max = this.max.orElse(Integer.MAX_VALUE);
            for (int i = min; i <= max; i++) {
                if (this.filter == null || this.filter.test(i)) {
                    universe.add(i);
                }
            }

            IntegerGenKeyType handler = new IntegerGenKeyType(universe, this.alterable);
            return handler;
        }

    }
}

package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IRangedKeyBuilder;
import nationalcipher.cipher.base.KeyFunction;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;

public class OrderedIntegerKeyType implements IKeyType<Integer[]> {

    // Both inclusive
    private final int min, max;
    private final Optional<Integer> range;
    private final boolean repeats;
    private final Function<Integer[], String> displayFunc;

    private OrderedIntegerKeyType(int min, int max, Optional<Integer> range, boolean repeats, Function<Integer[], String> displayFunc) {
        this.min = min;
        this.max = max;
        this.range = range;
        this.repeats = repeats;
        this.displayFunc = displayFunc;
    }

    @Override
    public Integer[] randomise() {
        BiFunction<Integer, Integer, Integer[]> func = this.repeats ? KeyGeneration::createRepeatingShortOrderKey : KeyGeneration::createShortOrderKey;

        int length = RandomUtil.pickRandomInt(this.min, this.max);
        return func.apply(this.range.orElse(length), length);
    }

    @Override
    public boolean isValid(Integer[] key) {
        for (int i = 0; i < key.length; i++) {
            if (key[i] >= this.range.orElse(key.length) || key[i] < 0) {
                return false;
            } else if (!this.repeats && i < key.length - 1) {
                if (ArrayUtil.contains(key, i + 1, key.length, key[i])) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean iterateKeys(KeyFunction<Integer[]> consumer) {
        for (int length = this.min; length <= this.max; length++) {
            if (!KeyIterator.iterateIntegerArray(consumer, this.range.orElse(length), length, this.repeats)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Integer[] alterKey(Integer[] key) {
        return KeyManipulation.modifyOrder(key);
    }

    @Override
    public String prettifyKey(Integer[] key) {
        return this.displayFunc == null ? Arrays.toString(key) : this.displayFunc.apply(key);
    }

    @Override
    public BigInteger getNumOfKeys() {
        BigInteger total = BigInteger.ZERO;
        
        if(this.repeats) {
            for (int length = this.min; length <= this.max; length++) {
                total = total.add(BigInteger.valueOf(this.range.orElse(length)).pow(length));
            }
        } else {
            for (int length = this.min; length <= this.max; length++) {
                BigInteger subTotal = BigInteger.ONE;
                for (int i = 0; i < length; i++) {
                    subTotal = subTotal.multiply(BigInteger.valueOf(this.range.orElse(length) - i));
                }
                total = total.add(subTotal);
            }
        }
        
        return total;
    }
    
    @Override
    public Integer[] parse(String input) throws ParseException {
        if (input.startsWith("[") && input.endsWith("]")) {
            String[] elements = input.substring(1, input.length() - 1).split(",");
            Integer[] key = new Integer[elements.length];
            for (int i = 0; i < elements.length; i++) {
                try {
                    key[i] = Integer.valueOf(elements[i]);
                } catch (NumberFormatException e) {
                    throw new ParseException(input, 0);
                }
            }
            return key;
        }
        throw new ParseException(input, 0);
    }
    
    @Override
    public String getHelp() {
        return "array";
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

    public static class Builder implements IRangedKeyBuilder<Integer[]> {

        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();
        private Optional<Integer> entryRange = Optional.empty();
        private boolean repeats = false;
        private Function<Integer[], String> displayFunc;

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
        
        public Builder setEntryRange(int range) {
            this.entryRange = Optional.of(range);
            return this;
        }

        public Builder setRepeats() {
            this.repeats = true;
            return this;
        }
        
        @Override
        public Builder setDisplay(Function<Integer[], String> displayFunc) {
            this.displayFunc = displayFunc;
            return this;
        }

        @Override
        public OrderedIntegerKeyType create() {
            OrderedIntegerKeyType handler = new OrderedIntegerKeyType(this.min.orElse(2), this.max.orElse(6), this.entryRange, this.repeats, this.displayFunc);
            return handler;
        }
    }
}

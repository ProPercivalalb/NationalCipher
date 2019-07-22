package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IRangedKeyBuilder;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeyManipulation;

public class VariableStringKeyType implements IKeyType<String> {

    // Both inclusive
    private final int min, max;
    private final Character[] alphabet;
    private final boolean repeats;

    private VariableStringKeyType(Character[] alphabet, int min, int max, boolean repeats) {
        this.alphabet = alphabet;
        this.min = min;
        this.max = max;
        this.repeats = repeats;
    }

    @Override
    public String randomise(Object partialKey) {
        BiFunction<Character[], Integer, String> func = this.repeats ? KeyGeneration::createRepeatingShortKeyUniversal : KeyGeneration::createShortKeyUniversal;

        return func.apply(this.alphabet, RandomUtil.pickRandomInt(this.min, this.max));
    }

    @Override
    public boolean isValid(Object partialKey, String key) {
        // Quick check if length bigger than number
        if (!this.repeats && key.length() > this.alphabet.length) {
            return false;
        }
        
        for (int i = 0; i < key.length(); i++) {
            if (!ArrayUtil.contains(this.alphabet, key.charAt(i))) {
                return false;
            } else if (!this.repeats && i < key.length() - 1) {
                if (ArrayUtil.contains(key, i + 1, key.length(), key.charAt(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void iterateKeys(Object partialKey, Consumer<String> consumer) {
        for (int length = this.min; length <= this.max; length++) {
            KeyIterator.iterateShortKey(consumer, this.alphabet, length, this.repeats);
        }
    }

    @Override
    public String alterKey(Object fullKey, String key) {
        return new String(KeyManipulation.changeCharacters(key.toCharArray(), this.alphabet, this.repeats)); // TODO decrease
                                                                                                        // copying into
                                                                                                        // new arrays so
                                                                                                        // much
    }

    @Override
    public BigInteger getNumOfKeys() {
        BigInteger total = BigInteger.ZERO;
        
        if(this.repeats) {
            BigInteger alphaSize = BigInteger.valueOf(this.alphabet.length);
            
            for (int length = this.min; length <= this.max; length++) {
                total = total.add(alphaSize.pow(length));
            }
        } else {
            for (int length = this.min; length <= this.max; length++) {
                BigInteger subTotal = BigInteger.ONE;
                for (int i = 0; i < length; i++) {
                    subTotal = subTotal.multiply(BigInteger.valueOf(this.alphabet.length - i));
                }
                total = total.add(subTotal);
            }
        }
        
        return total;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IRangedKeyBuilder<String> {

        private Optional<Character[]> alphabet = Optional.empty();
        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();
        private boolean repeats = false;

        private Builder() {
        }

        public Builder setAlphabet(String alphabet) {
            return this.setAlphabet(PrimTypeUtil.toCharacterArray(alphabet));
        }

        public Builder setAlphabet(Character[] alphabet) {
            this.alphabet = Optional.of(alphabet);
            return this;
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
        public VariableStringKeyType create() {
            VariableStringKeyType handler = new VariableStringKeyType(this.alphabet.orElse(KeyGeneration.ALL_26_CHARS), this.min.orElse(2), this.max.orElse(6), this.repeats);
            return handler;
        }

    }
}

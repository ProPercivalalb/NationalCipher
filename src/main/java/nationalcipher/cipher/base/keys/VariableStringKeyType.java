package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.tools.KeyGeneration;

public class VariableStringKeyType implements IKeyType<String> {

    // Both inclusive
    private final int min, max;
    private final Character[] alphabet;
    private boolean repeats;
    
    private VariableStringKeyType(Character[] alphabet, int min, int max, boolean repeats) {
        this.alphabet = alphabet;
        this.min = min;
        this.max = max;
        this.repeats = repeats;
    }
    
    @Override
    public String randomise() {
        BiFunction<Character[], Integer, String> func = this.repeats ? 
                KeyGeneration::createRepeatingShortKeyUniversal : KeyGeneration::createShortKeyUniversal;
        
        return func.apply(this.alphabet, RandomUtil.pickRandomInt(this.min, this.max));
    }

    @Override
    public boolean isValid(String key) {
        for(int i = 0; i < key.length(); i++) {
            if(!ArrayUtil.contains(this.alphabet, key.charAt(i))) {
                return false;
            } else if(!this.repeats && i < key.length() - 1) {
                if(ArrayUtil.contains(key, i + 1, key.length(), key.charAt(i))) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public void iterateKeys(Consumer<String> consumer) {
        for(int length = this.min; length <= this.max; length++) {
            KeyIterator.iterateShortKey(consumer, this.alphabet, length, this.repeats);
        }
    }
    
    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.ZERO;
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        
        private Optional<Character[]> alphabet = Optional.empty();
        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();
        private boolean repeats = false;
        
        private Builder() {}
        
        public Builder setAlphabet(String alphabet) {
            return this.setAlphabet(PrimTypeUtil.toCharacterArray(alphabet));
        }
        
        public Builder setAlphabet(Character[] alphabet) {
            this.alphabet = Optional.of(alphabet);
            return this;
        }
        
        public Builder setMin(int min) {
            this.min = Optional.of(min);
            return this;
        }
        
        public Builder setMax(int max) {
            this.max = Optional.of(max);
            return this;
        }
        
        public Builder setRange(int min, int max) {
            this.setMin(min);
            this.setMax(max);
            return this;
        }
        
        public Builder setRepeats() {
            this.repeats = true;
            return this;
        }
        
        public VariableStringKeyType create() {
            VariableStringKeyType handler = new VariableStringKeyType(
                    this.alphabet.orElse(KeyGeneration.ALL_26_CHARS), 
                    this.min.orElse(2), 
                    this.max.orElse(6), this.repeats);
            return handler;
        }

    }
}

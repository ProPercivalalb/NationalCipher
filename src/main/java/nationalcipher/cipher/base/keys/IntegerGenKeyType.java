package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;

public class IntegerGenKeyType implements IKeyType<Integer> {

    // Both inclusive
    private final List<Integer> universe;
    private boolean alterable;
    
    private IntegerGenKeyType(List<Integer> universe, boolean alterable) {
        this.universe = universe;
        this.alterable = alterable;
    }
    
    @Override
    public Integer randomise(Object partialKey) {
        return RandomUtil.pickRandomElement(this.universe);
    }

    @Override
    public boolean isValid(Object partialKey, Integer key) {
        return this.universe.contains(key);
    }
    
    @Override
    public void iterateKeys(Object partialKey, Consumer<Integer> consumer) {
        this.universe.forEach(consumer);
    }
    
    @Override
    public Integer alterKey(Object partialKey, Integer key) {
        return RandomUtil.pickRandomElement(this.universe);
    }
    
    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.valueOf(this.universe.size());
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements IKeyBuilder<Integer, IntegerGenKeyType> {
        
        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();
        private Predicate<Integer> filter = n -> true;
        private boolean alterable = false;
        
        private Builder() {}
        
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
        
        public Builder setFilter(Predicate<Integer> filterIn) {
            this.filter = filterIn;
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
            for(int i = min; i <= max; i++) {
                if(this.filter.test(i)) {
                    universe.add(i);
                }
            }
            
            IntegerGenKeyType handler = new IntegerGenKeyType(universe, this.alterable);
            return handler;
        }

    }
}

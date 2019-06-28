package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Consumer;

import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.decrypt.methods.KeyIterator;

public class IntegerKeyType implements IKeyType<Integer> {

    // Both inclusive
    private final int min, max;
    
    private IntegerKeyType(int min, int max) {
        this.min = min;
        this.max = max;
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
    public void iterateKeys(Consumer<Integer> consumer) {
        KeyIterator.iterateIntegerKey(consumer::accept, this.min, this.max, 1);
    }
    
    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.valueOf(this.max).subtract(BigInteger.valueOf(this.min)).add(BigInteger.ONE);
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        
        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();
        
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
        
        public IntegerKeyType create() {
            IntegerKeyType handler = new IntegerKeyType(
                    this.min.orElse(Integer.MIN_VALUE), 
                    this.max.orElse(Integer.MAX_VALUE));
            return handler;
        }

    }

}

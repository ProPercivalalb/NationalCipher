package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Consumer;

import javalibrary.streams.PrimTypeUtil;
import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.base.keys.VariableStringKeyType.Builder;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.tools.KeyGeneration;

public class GrilleKeyType implements IKeyType<Integer[]> {

    // Both inclusive
    private final int min, max;
    
    private GrilleKeyType(int min, int max) {
        this.min = min;
        this.max = max;
    }
    
    @Override
    public Integer[] randomise() {
        return KeyGeneration.createGrilleKey(RandomUtil.pickRandomInt(this.min, this.max));
    }

    @Override
    public boolean isValid(Integer[] key) {
        //TODO
        return true;
    }
    
    @Override
    public void iterateKeys(Consumer<Integer[]> consumer) {
        for(int size = this.min; size <= this.max; size++) {
            KeyIterator.iterateGrille(consumer, size);
        }
    }
    
    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.valueOf(59049); // Calculate number
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
        
        public GrilleKeyType create() {
            GrilleKeyType handler = new GrilleKeyType(this.min.orElse(2), this.max.orElse(6));
            return handler;
        }

    }
}

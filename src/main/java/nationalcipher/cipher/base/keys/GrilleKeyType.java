package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IRangedKeyBuilder;
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
    public Integer[] randomise(Object partialKey) {
        return KeyGeneration.createGrilleKey(RandomUtil.pickRandomInt(this.min, this.max));
    }

    @Override
    public boolean isValid(Object partialKey, Integer[] key) {
        // TODO
        return true;
    }

    @Override
    public void iterateKeys(Object partialKey, Consumer<Integer[]> consumer) {
        for (int size = this.min; size <= this.max; size++) {
            KeyIterator.iterateGrille(consumer, size);
        }
    }

    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.valueOf(59049); //TODO Calculate number
    }

    @Override
    public Integer[] alterKey(Object partialKey, Integer[] key) {
        return key;
    }
    
    @Override
    public String prettifyKey(Integer[] key) {
        return Arrays.toString(key);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IRangedKeyBuilder<Integer[]> {

        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();

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

        @Override
        public GrilleKeyType create() {
            GrilleKeyType handler = new GrilleKeyType(this.min.orElse(2), this.max.orElse(6));
            return handler;
        }
    }
}

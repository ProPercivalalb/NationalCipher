package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Consumer;

import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
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
        return BigInteger.valueOf(59049); // Calculate number
    }

    @Override
    public Integer[] alterKey(Object partialKey, Integer[] key) {
        return key;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IKeyBuilder<Integer[]> {

        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();

        private Builder() {
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

        @Override
        public GrilleKeyType create() {
            GrilleKeyType handler = new GrilleKeyType(this.min.orElse(2), this.max.orElse(6));
            return handler;
        }

    }
}

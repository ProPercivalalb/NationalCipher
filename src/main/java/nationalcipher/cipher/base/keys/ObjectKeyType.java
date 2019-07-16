package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Consumer;

import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;

public class ObjectKeyType<T> implements IKeyType<T> {

    private final T[] universe;
    private final boolean alterable;

    private ObjectKeyType(T[] universe, boolean alterable) {
        this.universe = universe;
        this.alterable = alterable;
    }

    @Override
    public T randomise(Object partialKey) {
        return RandomUtil.pickRandomElement(this.universe);
    }

    @Override
    public boolean isValid(Object partialKey, T key) {
        return ArrayUtil.contains(this.universe, key);
    }

    @Override
    public void iterateKeys(Object partialKey, Consumer<T> consumer) {
        for (T atom : this.universe) {
            consumer.accept(atom);
        }
    }

    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.valueOf(this.universe.length);
    }

    @Override
    public T alterKey(Object fullKey, T key) {
        return this.alterable ? RandomUtil.pickRandomElement(this.universe) : key;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> implements IKeyBuilder<T> {

        private Optional<T[]> universe = Optional.empty();
        private boolean alterable = false;

        private Builder() {
        }

        public Builder<T> setUniverse(T... universe) {
            this.universe = Optional.of(universe);
            return this;
        }

        public Builder<T> setAlterable() {
            this.alterable = true;
            return this;
        }

        @Override
        public ObjectKeyType<T> create() {
            ObjectKeyType<T> handler = new ObjectKeyType<>(this.universe.orElse(null), this.alterable);
            return handler;
        }

    }
}

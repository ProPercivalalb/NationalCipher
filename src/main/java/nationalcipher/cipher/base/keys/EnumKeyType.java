package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Consumer;

import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;

public class EnumKeyType<T extends Enum<?>> implements IKeyType<T> {

    private final Class<T> enumType;
    private final T[] universe;
    private final boolean alterable;

    private EnumKeyType(Class<T> clazz, T[] universe, boolean alterable) {
        this.enumType = clazz;
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
        return BigInteger.ZERO;
    }

    @Override
    public T alterKey(Object fullKey, T key) {
        return this.alterable ? RandomUtil.pickRandomElement(this.universe) : key;
    }

    public static <T extends Enum<?>> Builder<T> builder(Class<T> enumType) {
        return new Builder<>(enumType);
    }

    public static class Builder<T extends Enum<?>> implements IKeyBuilder<T, EnumKeyType<T>> {

        private Class<T> enumType;
        private Optional<T[]> universe = Optional.empty();
        private boolean alterable = false;

        private Builder(Class<T> clazz) {
            this.enumType = clazz;
        }

        public Builder<T> setUniverse(T... universe) {
            this.universe = Optional.of(universe);
            return this;
        }

        public Builder setAlterable() {
            this.alterable = true;
            return this;
        }

        @Override
        public EnumKeyType<T> create() {
            EnumKeyType<T> handler = new EnumKeyType<>(this.enumType, this.universe.orElse(null), this.alterable);
            return handler;
        }

    }
}

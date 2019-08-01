package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Optional;
import java.util.StringJoiner;

import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.base.KeyFunction;

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
    public T randomise() {
        return RandomUtil.pickRandomElement(this.universe);
    }

    @Override
    public boolean isValid(T key) {
        return ArrayUtil.contains(this.universe, key);
    }

    @Override
    public boolean iterateKeys(KeyFunction<T> consumer) {
        for (T atom : this.universe) {
            if (!consumer.apply(atom)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.valueOf(this.universe.length);
    }

    @Override
    public T alterKey(T key) {
        return this.alterable ? RandomUtil.pickRandomElement(this.universe) : key;
    }
    
    @Override
    public T parse(String input) throws ParseException {
        for (T atom : this.universe) {
            if (atom.name().equalsIgnoreCase(input)) {
                return atom;
            }
        }
        throw new ParseException(input, 0);
    }
    
    @Override
    public String getHelp() {
        StringJoiner joiner = new StringJoiner("|");
        for (T atom : this.universe) {
            joiner.add(atom.name());
        }
        return joiner.toString();
    }

    public static <T extends Enum<?>> Builder<T> builder(Class<T> enumType) {
        return new Builder<>(enumType);
    }

    public static class Builder<T extends Enum<?>> implements IKeyBuilder<T> {

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

        public Builder<T> setAlterable() {
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

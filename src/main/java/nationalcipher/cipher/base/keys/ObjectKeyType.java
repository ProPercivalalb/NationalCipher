package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Optional;

import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.base.KeyFunction;

public class ObjectKeyType<T> implements IKeyType<T> {

    private final T[] universe;
    private final boolean alterable;

    private ObjectKeyType(T[] universe, boolean alterable) {
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
            if (atom instanceof Character) {
                if (((Character) atom) == input.charAt(0)) {
                    return atom;
                }
            } else if (atom instanceof String) {
                if (((String) atom).equalsIgnoreCase(input)) {
                    return atom;
                }
            } else if (atom instanceof Integer) {
                try {
                    if ((Integer) atom == Integer.parseInt(input)) {
                        return atom;
                    }
                } catch (NumberFormatException e) {}
            }
        }
        throw new ParseException(input, 0);
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

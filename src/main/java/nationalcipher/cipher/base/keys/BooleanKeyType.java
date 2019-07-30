package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Optional;

import javalibrary.util.ArrayUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IRangedKeyBuilder;
import nationalcipher.cipher.base.KeyFunction;

public class BooleanKeyType implements IKeyType<Boolean> {

    private Boolean[] universe;
    private final boolean alterable;

    private BooleanKeyType(Boolean[] universe, boolean alterable) {
        this.universe = universe;
        this.alterable = alterable;
    }

    @Override
    public Boolean randomise() {
        return RandomUtil.pickRandomElement(this.universe);
    }

    @Override
    public boolean isValid(Boolean key) {
        return ArrayUtil.contains(this.universe, key);
    }

    @Override
    public boolean iterateKeys(KeyFunction<Boolean> consumer) {
        for (Boolean value : this.universe) {
            if (!consumer.apply(value)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean alterKey(Boolean key) {
        return this.alterable ? !key : key; //TODO
    }

    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.valueOf(this.universe.length);
    }
    
    @Override
    public Boolean parse(String input) throws ParseException {
        if (input.equalsIgnoreCase("true")) {
            return true;
        } else if (input.equalsIgnoreCase("false")) {
            return false;
        }
        
        try {
            int i = Integer.parseInt(input);
            if (i == 0) {
                return false;
            } else if (i == 1) {
                return true;
            }
        } catch (NumberFormatException e) {}

        throw new ParseException(input, 0);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IKeyBuilder<Boolean> {

        private boolean alterable = false;
        private Optional<Boolean[]> universe = Optional.empty();
        
        private Builder() {
        }

        public Builder setAlterable() {
            this.alterable = true;
            return this;
        }

        @Override
        public BooleanKeyType create() {
            BooleanKeyType handler = new BooleanKeyType(this.universe.orElse(new Boolean[] {true, false}), this.alterable);
            return handler;
        }

        public Builder setUniverse(Boolean... universe) {
            this.universe = Optional.of(universe);
            return this;
        }

    }
}

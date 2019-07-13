package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.function.Consumer;

import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;

public class BooleanKeyType implements IKeyType<Boolean> {

    private final boolean alterable;

    private BooleanKeyType(boolean alterable) {
        this.alterable = alterable;
    }

    @Override
    public Boolean randomise(Object partialKey) {
        return RandomUtil.pickBoolean();
    }

    @Override
    public boolean isValid(Object partialKey, Boolean key) {
        return true;
    }

    @Override
    public void iterateKeys(Object partialKey, Consumer<Boolean> consumer) {
        consumer.accept(true);
        consumer.accept(false);
    }

    @Override
    public Boolean alterKey(Object fullKey, Boolean key) {
        return this.alterable ? !key : key;
    }

    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.valueOf(2); // Calculate number
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IKeyBuilder<Boolean, BooleanKeyType> {

        private boolean alterable = false;

        private Builder() {
        }

        public Builder setAlterable() {
            this.alterable = true;
            return this;
        }

        @Override
        public BooleanKeyType create() {
            BooleanKeyType handler = new BooleanKeyType(this.alterable);
            return handler;
        }

    }
}

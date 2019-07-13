package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.function.Consumer;

import nationalcipher.api.IKeyType;

public class ConstantKeyType<T> implements IKeyType<T> {

    private final T constant;
    
    private ConstantKeyType(T constant) {
        this.constant = constant;
    }
    
    @Override
    public T randomise(Object partialKey) {
        return this.constant;
    }

    @Override
    public void iterateKeys(Object partialKey, Consumer<T> consumer) {
        consumer.accept(this.constant);
    }

    @Override
    public boolean isValid(Object fullKey, T key) {
        return key == this.constant ? true : key.equals(this.constant);
    }

    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.ONE;
    }
    
    @Override
    public T alterKey(Object fullKey, T key) {
        return key;
    }

    public static <T> Builder<T> builder(T constant) {
        return new Builder<>(constant);
    }
    
    public static class Builder<T> implements IKeyBuilder<T, ConstantKeyType<T>> {

        private final T constant;
        
        private Builder(T constant) {
            this.constant = constant;
        }
        
        @Override
        public ConstantKeyType<T> create() {
            return new ConstantKeyType<>(this.constant);
        }
    }
}

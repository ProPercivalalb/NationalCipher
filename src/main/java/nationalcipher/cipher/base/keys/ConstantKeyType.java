package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.StringJoiner;

import nationalcipher.api.IKeyType;
import nationalcipher.cipher.base.KeyFunction;

public class ConstantKeyType<T> implements IKeyType<T> {

    private final T constant;

    private ConstantKeyType(T constant) {
        this.constant = constant;
    }

    @Override
    public T randomise() {
        return this.constant;
    }

    @Override
    public boolean iterateKeys(KeyFunction<T> consumer) {
        return consumer.apply(this.constant);
    }

    @Override
    public boolean isValid(T key) {
        return key == this.constant ? true : key.equals(this.constant);
    }

    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.ONE;
    }

    @Override
    public T alterKey(T key) {
        return key;
    }
    
    @Override
    public T parse(String input) throws ParseException {
        return this.constant;
    }
    
    @Override
    public String getHelp() {
        return this.constant.toString();
    }

    public static <T> Builder<T> builder(T constant) {
        return new Builder<>(constant);
    }

    public static class Builder<T> implements IKeyBuilder<T> {

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

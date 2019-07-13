package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.util.function.Consumer;

import nationalcipher.api.ICipher;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IKeyType.IKeyBuilder;

public abstract class UniKeyCipher<T> implements ICipher<T> {

    private final IKeyType<T> firstType;

    public UniKeyCipher(IKeyBuilder<T, ?> firstKey) {
        this.firstType = firstKey.create();
    }

    @Override
    public boolean isValid(T key) {
        return this.firstType.isValid(null, key);
    }

    @Override
    public T randomiseKey() {
        return this.firstType.randomise(null);
    }

    @Override
    public void iterateKeys(Consumer<T> consumer) {
        this.firstType.iterateKeys(null, consumer::accept);
    }

    @Override
    public T alterKey(T key) {
        return this.firstType.alterKey(null, key);
    }

    @Override
    public BigInteger getNumOfKeys() {
        return this.firstType.getNumOfKeys();
    }
}

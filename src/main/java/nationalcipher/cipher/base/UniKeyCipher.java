package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.util.function.Consumer;

import nationalcipher.api.ICipher;
import nationalcipher.api.IKeyType;

public abstract class UniKeyCipher<T> implements ICipher<T> {

    private final IKeyType<T> firstType;
    
    public UniKeyCipher(IKeyType<T> firstKey) {
        this.firstType = firstKey;
    }
    
    @Override
    public boolean isValid(T key) {
        return this.firstType.isValid(key);
    }

    @Override
    public T randomise() {
        return this.firstType.randomise();
    }
    
    @Override
    public void iterateKeys(Consumer<T> consumer) {
        this.firstType.iterateKeys(consumer::accept);
    }
    
    @Override
    public BigInteger getNumOfKeys() {
        return this.firstType.getNumOfKeys();
    }
}

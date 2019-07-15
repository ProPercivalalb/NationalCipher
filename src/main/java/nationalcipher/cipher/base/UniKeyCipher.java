package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import nationalcipher.api.ICipher;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IKeyType.IKeyBuilder;

public abstract class UniKeyCipher<T, A extends IKeyBuilder<T>> implements ICipher<T> {

    private final IKeyType<T> firstType;
    private IKeyType<T> firstTypeLimit;
    private final A firstKeyBuilder;

    public UniKeyCipher(A firstKey) {
        this.firstType = firstKey.create();
        this.firstTypeLimit = this.limitDomainForFirstKey(firstKey).create();
        this.firstKeyBuilder = firstKey;
    }
    
    @Override
    public boolean isValid(T key) {
        return this.firstType.isValid(null, key);
    }

    @Override
    public T randomiseKey() {
        return this.firstTypeLimit.randomise(null);
    }

    @Override
    public void iterateKeys(Consumer<T> consumer) {
        this.firstTypeLimit.iterateKeys(null, consumer::accept);
    }

    @Override
    public T alterKey(T key) {
        return this.firstType.alterKey(null, key);
    }

    @Override
    public BigInteger getNumOfKeys() {
        return this.firstTypeLimit.getNumOfKeys();
    }
    
    @Override
    public String prettifyKey(T key) {
        return this.firstType.prettifyKey(key);
    }
    
    public IKeyBuilder<T> limitDomainForFirstKey(A firstKey) {
        return firstKey;
    }
    
    public void setDomain(Function<A, IKeyBuilder<T>> firstKeyFunc) {
        this.firstTypeLimit = firstKeyFunc.apply(this.firstKeyBuilder).create();
    }
}

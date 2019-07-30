package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.text.ParseException;
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
        return this.firstType.isValid(key);
    }

    @Override
    public T randomiseKey() {
        return this.firstTypeLimit.randomise();
    }

    @Override
    public void iterateKeys(KeyFunction<T> consumer) {
        this.firstTypeLimit.iterateKeys(consumer);
    }

    @Override
    public T alterKey(T key, double temp, int count) {
        return this.firstType.alterKey(key);
    }

    @Override
    public BigInteger getNumOfKeys() {
        return this.firstTypeLimit.getNumOfKeys();
    }
    
    @Override
    public String prettifyKey(T key) {
        return this.firstType.prettifyKey(key);
    }
    
    @Override
    public T parseKey(String input) throws ParseException {
        return this.firstType.parse(input);
    }
    
    public IKeyBuilder<T> limitDomainForFirstKey(A firstKey) {
        return firstKey;
    }
    
    public void setDomain(Function<A, IKeyBuilder<T>> firstKeyFunc) {
        this.firstTypeLimit = firstKeyFunc.apply(this.firstKeyBuilder).create();
    }
    
    public IKeyType<T> getDomain() {
        return this.firstTypeLimit;
    }
}

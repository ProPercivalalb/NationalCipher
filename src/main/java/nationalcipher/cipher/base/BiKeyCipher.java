package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import nationalcipher.api.ICipher;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.keys.BiKey;

public abstract class BiKeyCipher<F, S, A extends IKeyBuilder<F>, B extends IKeyBuilder<S>> implements ICipher<BiKey<F, S>> {

    protected final IKeyType<F> firstType;
    protected final IKeyType<S> secondType;
    private IKeyType<F> firstTypeLimit;
    private IKeyType<S> secondTypeLimit;
    private final A firstKeyBuilder;
    private final B secondKeyBuilder;
    
    public BiKeyCipher(A firstKey, B secondKey) {
        this.firstType = firstKey.create();
        this.secondType = secondKey.create();
        this.firstTypeLimit = this.limitDomainForFirstKey(firstKey).create();
        this.secondTypeLimit = this.limitDomainForSecondKey(secondKey).create();
        this.firstKeyBuilder = firstKey;
        this.secondKeyBuilder = secondKey;
    }
    
    @Override
    public boolean isValid(BiKey<F, S> key) {
        return this.firstType.isValid(key, key.getFirstKey()) && this.secondType.isValid(key, key.getSecondKey());
    }

    @Override
    public BiKey<F, S> randomiseKey() {
        BiKey<F, S> key = BiKey.empty();
        return key.setFirst(this.firstTypeLimit.randomise(key)).setSecond(this.secondTypeLimit.randomise(key));
    }

    @Override
    public void iterateKeys(Consumer<BiKey<F, S>> consumer) {
//        BiKey<F, S> key = BiKey.empty();
//        this.firstTypeLimit.iterateKeys(null, f -> {
//            key.setFirst(f);
//            this.secondTypeLimit.iterateKeys(key, s -> consumer.accept(key.setSecond(s)));
//        });
        BiKey<F, S> key = BiKey.empty();
        this.firstTypeLimit.iterateKeys(null, f -> {
            key.setFirst(f);
            this.secondTypeLimit.iterateKeys(key, s -> consumer.accept(key.setSecond(s).clone()));
        });
    }

    @Override
    public BiKey<F, S> alterKey(BiKey<F, S> key, double temp, int count, double lastDF) {
        return BiKey.of(this.firstType.alterKey(key, key.getFirstKey()), this.secondType.alterKey(key, key.getSecondKey()));
    }

    @Override
    public BigInteger getNumOfKeys() {
        return this.firstTypeLimit.getNumOfKeys().multiply(this.secondTypeLimit.getNumOfKeys());
    }
    
    @Override
    public String prettifyKey(BiKey<F, S> key) {
        return String.join(" ",  this.firstType.prettifyKey(key.getFirstKey()), this.secondType.prettifyKey(key.getSecondKey()));
    }
    
    public IKeyBuilder<F> limitDomainForFirstKey(A firstKey) {
        return firstKey;
    }
    
    public IKeyBuilder<S> limitDomainForSecondKey(B secondKey) {
        return secondKey;
    }
    
    public void setFirstKeyLimit(Function<A, IKeyBuilder<F>> firstKeyFunc) {
        this.firstTypeLimit = firstKeyFunc.apply(this.firstKeyBuilder).create();
    }
    
    public void setSecondKeyLimit(Function<B, IKeyBuilder<S>> secondKeyFunc) {
        this.secondTypeLimit = secondKeyFunc.apply(this.secondKeyBuilder).create();
    }
}

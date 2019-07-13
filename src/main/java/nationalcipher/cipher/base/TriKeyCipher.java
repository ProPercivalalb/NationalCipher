package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.util.function.Consumer;

import nationalcipher.api.ICipher;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.keys.TriKey;

public abstract class TriKeyCipher<F, S, T> implements ICipher<TriKey<F, S, T>> {

    private final IKeyType<F> firstType;
    private final IKeyType<S> secondType;
    private final IKeyType<T> thirdType;
    
    public TriKeyCipher(IKeyBuilder<F, ?> firstKey, IKeyBuilder<S, ?> secondKey, IKeyBuilder<T, ?> thirdKey) {
        this.firstType = firstKey.create();
        this.secondType = secondKey.create();
        this.thirdType = thirdKey.create();
    }
    
    @Override
    public boolean isValid(TriKey<F, S, T> key) {
        return this.firstType.isValid(key, key.getFirstKey()) && this.secondType.isValid(key, key.getSecondKey()) && this.thirdType.isValid(key, key.getThirdKey());
    }

    @Override
    public TriKey<F, S, T> randomiseKey() {
        TriKey<F, S, T> key = TriKey.empty();
        return key.setFirst(this.firstType.randomise(key))
                  .setSecond(this.secondType.randomise(key))
                  .setThird(this.thirdType.randomise(key));
    }
    
    @Override
    public void iterateKeys(Consumer<TriKey<F, S, T>> consumer) {
        TriKey<F, S, T> key = TriKey.empty();
        this.firstType.iterateKeys(null, f -> {
            key.setFirst(f);
            this.secondType.iterateKeys(key, s -> {
                key.setSecond(s);
                this.thirdType.iterateKeys(key, t -> consumer.accept(key.setThird(t)));
            });
        });
    }
    
    @Override
    public TriKey<F, S, T> alterKey(TriKey<F, S, T> key) {
        return TriKey.of(this.firstType.alterKey(key, key.getFirstKey()),
                    this.secondType.alterKey(key, key.getSecondKey()),
                    this.thirdType.alterKey(key, key.getThirdKey()));
    }
    
    @Override
    public BigInteger getNumOfKeys() {
        return this.firstType.getNumOfKeys().multiply(this.secondType.getNumOfKeys()).multiply(this.thirdType.getNumOfKeys());
    }
}

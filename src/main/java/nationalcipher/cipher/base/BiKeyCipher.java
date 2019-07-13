package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.util.function.Consumer;

import nationalcipher.api.ICipher;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.keys.BiKey;

public abstract class BiKeyCipher<F, S> implements ICipher<BiKey<F, S>> {

    protected final IKeyType<F> firstType;
    protected final IKeyType<S> secondType;
    
    public BiKeyCipher(IKeyBuilder<F, ?> firstKey, IKeyBuilder<S, ?> secondKey) {
        this.firstType = firstKey.create();
        this.secondType = secondKey.create();
    }
    
    @Override
    public boolean isValid(BiKey<F, S> key) {
        return this.firstType.isValid(key, key.getFirstKey()) && this.secondType.isValid(key, key.getSecondKey());
    }

    @Override
    public BiKey<F, S> randomiseKey() {
        BiKey<F, S> key = BiKey.empty();
        return key.setFirst(this.firstType.randomise(key))
                  .setSecond(this.secondType.randomise(key));
    }
    
    @Override
    public void iterateKeys(Consumer<BiKey<F, S>> consumer) {
        BiKey<F, S> key = BiKey.empty();
        this.firstType.iterateKeys(null, f -> {
            key.setFirst(f);
            this.secondType.iterateKeys(key, s -> consumer.accept(key.setSecond(s)));
        });
    }
    
    @Override
    public BiKey<F, S> alterKey(BiKey<F, S> key) {
        return BiKey.of(this.firstType.alterKey(key, key.getFirstKey()),
                    this.secondType.alterKey(key, key.getSecondKey()));
    }
    
    @Override
    public BigInteger getNumOfKeys() {
        return this.firstType.getNumOfKeys().multiply(this.secondType.getNumOfKeys());
    }
}

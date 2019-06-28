package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.util.function.Consumer;

import nationalcipher.api.ICipher;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.base.keys.BiKey;

public abstract class BiKeyCipher<F, S> implements ICipher<BiKey<F, S>> {

    private final IKeyType<F> firstType;
    private final IKeyType<S> secondType;
    
    public BiKeyCipher(IKeyType<F> firstKey, IKeyType<S> secondKey) {
        this.firstType = firstKey;
        this.secondType = secondKey;
    }
    
    @Override
    public boolean isValid(BiKey<F, S> key) {
        return this.firstType.isValid(key.getFirstKey()) && this.secondType.isValid(key.getSecondKey());
    }

    @Override
    public BiKey<F, S> randomise() {
        return new BiKey<>(this.firstType.randomise(), this.secondType.randomise());
    }
    
    @Override
    public void iterateKeys(Consumer<BiKey<F, S>> consumer) {
        this.firstType.iterateKeys(f -> 
            this.secondType.iterateKeys(s -> consumer.accept(new BiKey<>(f, s))));
    }
    
    @Override
    public BigInteger getNumOfKeys() {
        return this.firstType.getNumOfKeys().add(this.secondType.getNumOfKeys());
    }
}

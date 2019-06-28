package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.util.function.Consumer;

import nationalcipher.api.ICipher;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.base.keys.TriKey;

public abstract class TriKeyCipher<F, S, T> implements ICipher<TriKey<F, S, T>> {

    private final IKeyType<F> firstType;
    private final IKeyType<S> secondType;
    private final IKeyType<T> thirdType;
    
    public TriKeyCipher(IKeyType<F> firstKey, IKeyType<S> secondKey, IKeyType<T> thirdKey) {
        this.firstType = firstKey;
        this.secondType = secondKey;
        this.thirdType = thirdKey;
    }
    
    @Override
    public boolean isValid(TriKey<F, S, T> key) {
        return this.firstType.isValid(key.getFirstKey()) && this.secondType.isValid(key.getSecondKey()) && this.thirdType.isValid(key.getThirdKey());
    }

    @Override
    public TriKey<F, S, T> randomise() {
        return new TriKey<>(this.firstType.randomise(), this.secondType.randomise(), this.thirdType.randomise());
    }
    
    @Override
    public void iterateKeys(Consumer<TriKey<F, S, T>> consumer) {
        this.firstType.iterateKeys(f -> 
            this.secondType.iterateKeys(s -> 
                this.thirdType.iterateKeys(t -> consumer.accept(new TriKey<>(f, s, t)))));
    }
    
    @Override
    public BigInteger getNumOfKeys() {
        return this.firstType.getNumOfKeys().add(this.secondType.getNumOfKeys()).add(this.thirdType.getNumOfKeys());
    }
}

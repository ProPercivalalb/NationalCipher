package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.util.function.Consumer;

import nationalcipher.api.ICipher;
import nationalcipher.api.IKeyType;
import nationalcipher.cipher.base.keys.QuadKey;

public abstract class QuadKeyCipher<F, S, T, N> implements ICipher<QuadKey<F, S, T, N>> {

    private final IKeyType<F> firstType;
    private final IKeyType<S> secondType;
    private final IKeyType<T> thirdType;
    private final IKeyType<N> fourthType;
    
    public QuadKeyCipher(IKeyType<F> firstKey, IKeyType<S> secondKey, IKeyType<T> thirdKey, IKeyType<N> fourthKey) {
        this.firstType = firstKey;
        this.secondType = secondKey;
        this.thirdType = thirdKey;
        this.fourthType = fourthKey;
    }
    
    @Override
    public boolean isValid(QuadKey<F, S, T, N> key) {
        return this.firstType.isValid(key.getFirstKey()) && this.secondType.isValid(key.getSecondKey()) && this.thirdType.isValid(key.getThirdKey()) && this.fourthType.isValid(key.getFourthKey());
    }

    @Override
    public QuadKey<F, S, T, N> randomise() {
        return new QuadKey<>(this.firstType.randomise(), this.secondType.randomise(), this.thirdType.randomise(), this.fourthType.randomise());
    }
    
    @Override
    public void iterateKeys(Consumer<QuadKey<F, S, T, N>> consumer) {
        this.firstType.iterateKeys(f -> 
            this.secondType.iterateKeys(s -> 
                this.thirdType.iterateKeys(t -> 
                    this.fourthType.iterateKeys(n -> consumer.accept(new QuadKey<>(f, s, t, n))))));
    }
    
    @Override
    public BigInteger getNumOfKeys() {
        return this.firstType.getNumOfKeys().add(this.secondType.getNumOfKeys()).add(this.thirdType.getNumOfKeys()).add(this.fourthType.getNumOfKeys());
    }
}

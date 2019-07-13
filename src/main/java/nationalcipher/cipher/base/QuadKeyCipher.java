package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.util.function.Consumer;

import nationalcipher.api.ICipher;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.keys.QuadKey;

public abstract class QuadKeyCipher<F, S, T, N> implements ICipher<QuadKey<F, S, T, N>> {

    private final IKeyType<F> firstType;
    private final IKeyType<S> secondType;
    private final IKeyType<T> thirdType;
    private final IKeyType<N> fourthType;

    public QuadKeyCipher(IKeyBuilder<F, ?> firstKey, IKeyBuilder<S, ?> secondKey, IKeyBuilder<T, ?> thirdKey, IKeyBuilder<N, ?> fourthKey) {
        this.firstType = firstKey.create();
        this.secondType = secondKey.create();
        this.thirdType = thirdKey.create();
        this.fourthType = fourthKey.create();
    }

    @Override
    public boolean isValid(QuadKey<F, S, T, N> key) {
        return this.firstType.isValid(key, key.getFirstKey()) && this.secondType.isValid(key, key.getSecondKey()) && this.thirdType.isValid(key, key.getThirdKey()) && this.fourthType.isValid(key, key.getFourthKey());
    }

    @Override
    public QuadKey<F, S, T, N> randomiseKey() {
        QuadKey<F, S, T, N> key = QuadKey.empty();
        return key.setFirst(this.firstType.randomise(key)).setSecond(this.secondType.randomise(key)).setThird(this.thirdType.randomise(key)).setFourth(this.fourthType.randomise(key));
    }

    @Override
    public void iterateKeys(Consumer<QuadKey<F, S, T, N>> consumer) {
        QuadKey<F, S, T, N> key = QuadKey.empty();
        this.firstType.iterateKeys(null, f -> {
            key.setFirst(f);
            this.secondType.iterateKeys(key, s -> {
                key.setSecond(s);
                this.thirdType.iterateKeys(key, t -> {
                    key.setThird(t);
                    this.fourthType.iterateKeys(key, n -> consumer.accept(key.setFourth(n)));
                });
            });
        });
    }

    @Override
    public QuadKey<F, S, T, N> alterKey(QuadKey<F, S, T, N> key) {
        return QuadKey.of(this.firstType.alterKey(key, key.getFirstKey()), this.secondType.alterKey(key, key.getSecondKey()), this.thirdType.alterKey(key, key.getThirdKey()), this.fourthType.alterKey(key, key.getFourthKey()));
    }

    @Override
    public BigInteger getNumOfKeys() {
        return this.firstType.getNumOfKeys().multiply(this.secondType.getNumOfKeys()).multiply(this.thirdType.getNumOfKeys()).multiply(this.fourthType.getNumOfKeys());
    }
}

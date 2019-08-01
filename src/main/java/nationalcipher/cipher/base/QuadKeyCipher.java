package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.function.Function;

import javax.annotation.Nullable;

import nationalcipher.api.ICipher;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.keys.QuadKey;

public abstract class QuadKeyCipher<F, S, T, N, A extends IKeyBuilder<F>, B extends IKeyBuilder<S>, C extends IKeyBuilder<T>, D extends IKeyBuilder<N>> implements ICipher<QuadKey<F, S, T, N>> {

    private final IKeyType<F> firstType;
    private final IKeyType<S> secondType;
    private final IKeyType<T> thirdType;
    private final IKeyType<N> fourthType;
    private IKeyType<F> firstTypeLimit;
    private IKeyType<S> secondTypeLimit;
    private IKeyType<T> thirdTypeLimit;
    private IKeyType<N> fourthTypeLimit;
    private final A firstKeyBuilder;
    private final B secondKeyBuilder;
    private final C thirdKeyBuilder;
    private final D fourthKeyBuilder;
    
    public QuadKeyCipher(A firstKey, B secondKey, C thirdKey, D fourthKey) {
        this.firstType = firstKey.create();
        this.secondType = secondKey.create();
        this.thirdType = thirdKey.create();
        this.fourthType = fourthKey.create();
        this.firstTypeLimit = this.limitDomainForFirstKey(firstKey).create();
        this.secondTypeLimit = this.limitDomainForSecondKey(secondKey).create();
        this.thirdTypeLimit = this.limitDomainForThirdKey(thirdKey).create();
        this.fourthTypeLimit = this.limitDomainForFourthKey(fourthKey).create();
        this.firstKeyBuilder = firstKey;
        this.secondKeyBuilder = secondKey;
        this.thirdKeyBuilder = thirdKey;
        this.fourthKeyBuilder = fourthKey;
    }

    @Override
    public boolean isValid(QuadKey<F, S, T, N> key) {
        return this.firstType.isValid(key.getFirstKey()) && this.secondType.isValid(key.getSecondKey()) && this.thirdType.isValid(key.getThirdKey()) && this.fourthType.isValid(key.getFourthKey());
    }

    @Override
    public QuadKey<F, S, T, N> randomiseKey() {
        QuadKey<F, S, T, N> key = QuadKey.empty();
        return key.setFirst(this.firstTypeLimit.randomise()).setSecond(this.secondTypeLimit.randomise()).setThird(this.thirdTypeLimit.randomise()).setFourth(this.fourthTypeLimit.randomise());
    }

    @Override
    public void iterateKeys(KeyFunction<QuadKey<F, S, T, N>> consumer) {
//        QuadKey<F, S, T, N> key = QuadKey.empty();
//        this.firstTypeLimit.iterateKeys(null, f -> {
//            key.setFirst(f);
//            this.secondTypeLimit.iterateKeys(key, s -> {
//                key.setSecond(s);
//                this.thirdTypeLimit.iterateKeys(key, t -> {
//                    key.setThird(t);
//                    this.fourthTypeLimit.iterateKeys(key, n -> consumer.accept(key.setFourth(n)));
//                });
//            });
//        });
        QuadKey<F, S, T, N> key = QuadKey.empty();
        this.firstTypeLimit.iterateKeys(f -> {
            key.setFirst(f);
            return this.secondTypeLimit.iterateKeys(s -> {
                key.setSecond(s);
                return this.thirdTypeLimit.iterateKeys(t -> {
                    key.setThird(t);
                    return this.fourthTypeLimit.iterateKeys(n -> consumer.apply(key.setFourth(n).clone()));
                });
            });
        });
//        QuadKey<F, S, T, N> key = QuadKey.empty();
//        CipherUtils.recussivelyIterate(key, System.out::println, this.firstTypeLimit::iterateKeys, this.secondTypeLimit::iterateKeys, this.thirdTypeLimit::iterateKeys, this.fourthTypeLimit::iterateKeys);
    }

    @Override
    public QuadKey<F, S, T, N> alterKey(QuadKey<F, S, T, N> key, double temp, int count) {
        return QuadKey.of(this.firstType.alterKey(key.getFirstKey()), this.secondType.alterKey(key.getSecondKey()), this.thirdType.alterKey(key.getThirdKey()), this.fourthType.alterKey(key.getFourthKey()));
    }

    @Override
    public BigInteger getNumOfKeys() {
        return this.firstTypeLimit.getNumOfKeys().multiply(this.secondTypeLimit.getNumOfKeys()).multiply(this.thirdTypeLimit.getNumOfKeys()).multiply(this.fourthTypeLimit.getNumOfKeys());
    }
    
    @Override
    public String prettifyKey(QuadKey<F, S, T, N> key) {
        return String.join(" ",  this.firstType.prettifyKey(key.getFirstKey()), this.secondType.prettifyKey(key.getSecondKey()), this.thirdType.prettifyKey(key.getThirdKey()), this.fourthType.prettifyKey(key.getFourthKey()));
    }
    
    @Override
    public QuadKey<F, S, T, N> parseKey(String input) throws ParseException {
        String[] parts = input.split(" ");
        if (parts.length != 4) {
            throw new ParseException(input, 0);
        }
        
        return QuadKey.of(this.firstType.parse(parts[0]), this.secondType.parse(parts[1]), this.thirdType.parse(parts[2]), this.fourthType.parse(parts[3]));
    }
    
    @Nullable
    public String getHelp() {
        return String.join(" ",  this.firstType.getHelp(), this.secondType.getHelp(), this.thirdType.getHelp(), this.fourthType.getHelp());
    }
    
    public IKeyBuilder<F> limitDomainForFirstKey(A firstKey) {
        return firstKey;
    }
    
    public IKeyBuilder<S> limitDomainForSecondKey(B secondKey) {
        return secondKey;
    }
    
    public IKeyBuilder<T> limitDomainForThirdKey(C thirdKey) {
        return thirdKey;
    }
    
    public IKeyBuilder<N> limitDomainForFourthKey(D fourthKey) {
        return fourthKey;
    }
    
    public IKeyType<F> getFirstKeyType() {
        return this.firstTypeLimit;
    }
    
    public IKeyType<S> getSecondKeyType() {
        return this.secondTypeLimit;
    }
    
    public IKeyType<T> getThirdKeyType() {
        return this.thirdTypeLimit;
    }
    
    public IKeyType<N> getFourthKeyType() {
        return this.fourthTypeLimit;
    }
    
    public QuadKeyCipher<F, S, T, N, A, B, C, D> setFirstKeyLimit(Function<A, IKeyBuilder<F>> firstKeyFunc) {
        this.firstTypeLimit = firstKeyFunc.apply(this.firstKeyBuilder).create();
        return this;
    }
    
    public QuadKeyCipher<F, S, T, N, A, B, C, D> setSecondKeyLimit(Function<B, IKeyBuilder<S>> secondKeyFunc) {
        this.secondTypeLimit = secondKeyFunc.apply(this.secondKeyBuilder).create();
        return this;
    }
    
    public C setThirdKeyLimit(Function<C, IKeyBuilder<T>> thirdKeyFunc) {
        this.thirdTypeLimit = thirdKeyFunc.apply(this.thirdKeyBuilder).create();
        return this.thirdKeyBuilder;
    }
    
    public D setFourthKeyLimit(Function<D, IKeyBuilder<N>> fourthKeyFunc) {
        this.fourthTypeLimit = fourthKeyFunc.apply(this.fourthKeyBuilder).create();
        return this.fourthKeyBuilder;
    }
}

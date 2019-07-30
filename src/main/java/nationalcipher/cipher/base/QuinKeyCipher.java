package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.function.Function;

import nationalcipher.api.ICipher;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.keys.QuinKey;
import nationalcipher.cipher.base.keys.TriKey;

public abstract class QuinKeyCipher<F, S, T, N, Q, A extends IKeyBuilder<F>, B extends IKeyBuilder<S>, C extends IKeyBuilder<T>, D extends IKeyBuilder<N>, E extends IKeyBuilder<Q>> implements ICipher<QuinKey<F, S, T, N, Q>> {

    private final IKeyType<F> firstType;
    private final IKeyType<S> secondType;
    private final IKeyType<T> thirdType;
    private final IKeyType<N> fourthType;
    private final IKeyType<Q> fifthType;
    private IKeyType<F> firstTypeLimit;
    private IKeyType<S> secondTypeLimit;
    private IKeyType<T> thirdTypeLimit;
    private IKeyType<N> fourthTypeLimit;
    private IKeyType<Q> fifthTypeLimit;
    private final A firstKeyBuilder;
    private final B secondKeyBuilder;
    private final C thirdKeyBuilder;
    private final D fourthKeyBuilder;
    private final E fifthKeyBuilder;
    
    public QuinKeyCipher(A firstKey, B secondKey, C thirdKey, D fourthKey, E fifthKey) {
        this.firstType = firstKey.create();
        this.secondType = secondKey.create();
        this.thirdType = thirdKey.create();
        this.fourthType = fourthKey.create();
        this.fifthType = fifthKey.create();
        this.firstTypeLimit = this.limitDomainForFirstKey(firstKey).create();
        this.secondTypeLimit = this.limitDomainForSecondKey(secondKey).create();
        this.thirdTypeLimit = this.limitDomainForThirdKey(thirdKey).create();
        this.fourthTypeLimit = this.limitDomainForFourthKey(fourthKey).create();
        this.fifthTypeLimit = this.limitDomainForFifthKey(fifthKey).create();
        this.firstKeyBuilder = firstKey;
        this.secondKeyBuilder = secondKey;
        this.thirdKeyBuilder = thirdKey;
        this.fourthKeyBuilder = fourthKey;
        this.fifthKeyBuilder = fifthKey;
    }

    @Override
    public boolean isValid(QuinKey<F, S, T, N, Q> key) {
        return this.firstType.isValid(key.getFirstKey()) && this.secondType.isValid(key.getSecondKey()) && this.thirdType.isValid(key.getThirdKey()) && this.fourthType.isValid(key.getFourthKey()) && this.fifthType.isValid(key.getFifthKey());
    }

    @Override
    public QuinKey<F, S, T, N, Q> randomiseKey() {
        QuinKey<F, S, T, N, Q> key = QuinKey.empty();
        return key.setFirst(this.firstTypeLimit.randomise()).setSecond(this.secondTypeLimit.randomise()).setThird(this.thirdTypeLimit.randomise()).setFourth(this.fourthTypeLimit.randomise()).setFifth(this.fifthTypeLimit.randomise());
    }

    @Override
    public void iterateKeys(KeyFunction<QuinKey<F, S, T, N, Q>> consumer) {
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
        QuinKey<F, S, T, N, Q> key = QuinKey.empty();
        this.firstTypeLimit.iterateKeys(f -> {
            key.setFirst(f);
            return this.secondTypeLimit.iterateKeys(s -> {
                key.setSecond(s);
                return this.thirdTypeLimit.iterateKeys(t -> {
                    key.setThird(t);
                    return this.fourthTypeLimit.iterateKeys(n -> {
                        key.setFourth(n);
                        return this.fifthTypeLimit.iterateKeys(q -> consumer.apply(key.setFifth(q).clone()));
                    });
                });
            });
        });
//        QuadKey<F, S, T, N> key = QuadKey.empty();
//        CipherUtils.recussivelyIterate(key, System.out::println, this.firstTypeLimit::iterateKeys, this.secondTypeLimit::iterateKeys, this.thirdTypeLimit::iterateKeys, this.fourthTypeLimit::iterateKeys);
    }

    @Override
    public QuinKey<F, S, T, N, Q> alterKey(QuinKey<F, S, T, N, Q> key, double temp, int count) {
        return QuinKey.of(this.firstType.alterKey(key.getFirstKey()), this.secondType.alterKey(key.getSecondKey()), this.thirdType.alterKey(key.getThirdKey()), this.fourthType.alterKey(key.getFourthKey()), this.fifthType.alterKey(key.getFifthKey()));
    }

    @Override
    public BigInteger getNumOfKeys() {
        return this.firstTypeLimit.getNumOfKeys().multiply(this.secondTypeLimit.getNumOfKeys()).multiply(this.thirdTypeLimit.getNumOfKeys()).multiply(this.fourthTypeLimit.getNumOfKeys()).multiply(this.fifthTypeLimit.getNumOfKeys());
    }
    
    @Override
    public String prettifyKey(QuinKey<F, S, T, N, Q> key) {
        return String.join(" ",  this.firstType.prettifyKey(key.getFirstKey()), this.secondType.prettifyKey(key.getSecondKey()), this.thirdType.prettifyKey(key.getThirdKey()), this.fourthType.prettifyKey(key.getFourthKey()), this.fifthType.prettifyKey(key.getFifthKey()));
    }
    
    @Override
    public QuinKey<F, S, T, N, Q> parseKey(String input) throws ParseException {
        String[] parts = input.split(" ");
        if (parts.length != 5) {
            throw new ParseException(input, 0);
        }
        
        return QuinKey.of(this.firstType.parse(parts[0]), this.secondType.parse(parts[1]), this.thirdType.parse(parts[2]), this.fourthType.parse(parts[3]), this.fifthType.parse(parts[4]));
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
    
    public IKeyBuilder<Q> limitDomainForFifthKey(E fifthKey) {
        return fifthKey;
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
    
    public IKeyType<Q> getFifthKeyType() {
        return this.fifthTypeLimit;
    }
    
    public A setFirstKeyLimit(Function<A, IKeyBuilder<F>> firstKeyFunc) {
        this.firstTypeLimit = firstKeyFunc.apply(this.firstKeyBuilder).create();
        return this.firstKeyBuilder;
    }
    
    public B setSecondKeyLimit(Function<B, IKeyBuilder<S>> secondKeyFunc) {
        this.secondTypeLimit = secondKeyFunc.apply(this.secondKeyBuilder).create();
        return this.secondKeyBuilder;
    }
    
    public C setThirdKeyLimit(Function<C, IKeyBuilder<T>> thirdKeyFunc) {
        this.thirdTypeLimit = thirdKeyFunc.apply(this.thirdKeyBuilder).create();
        return this.thirdKeyBuilder;
    }
    
    public D setFourthKeyLimit(Function<D, IKeyBuilder<N>> fourthKeyFunc) {
        this.fourthTypeLimit = fourthKeyFunc.apply(this.fourthKeyBuilder).create();
        return this.fourthKeyBuilder;
    }
    
    public E setFifthKeyLimit(Function<E, IKeyBuilder<Q>> fifthKeyFunc) {
        this.fifthTypeLimit = fifthKeyFunc.apply(this.fifthKeyBuilder).create();
        return this.fifthKeyBuilder;
    }
}

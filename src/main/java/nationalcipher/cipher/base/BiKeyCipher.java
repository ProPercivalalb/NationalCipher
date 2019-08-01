package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.function.Function;

import javax.annotation.Nullable;

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
        return this.firstType.isValid(key.getFirstKey()) && this.secondType.isValid(key.getSecondKey());
    }

    @Override
    public BiKey<F, S> randomiseKey() {
        return BiKey.of(this.firstTypeLimit.randomise(), this.secondTypeLimit.randomise());
    }

    @Override
    public void iterateKeys(KeyFunction<BiKey<F, S>> consumer) {
        this.firstTypeLimit.iterateKeys(f -> 
            this.secondTypeLimit.iterateKeys(s -> consumer.apply(BiKey.of(f, s)))
        );
    }

    @Override
    public BiKey<F, S> alterKey(BiKey<F, S> key, double temp, int count) {
        return BiKey.of(this.firstType.alterKey(key.getFirstKey()), this.secondType.alterKey(key.getSecondKey()));
    }

    @Override
    public BigInteger getNumOfKeys() {
        return this.firstTypeLimit.getNumOfKeys().multiply(this.secondTypeLimit.getNumOfKeys());
    }
    
    @Override
    public String prettifyKey(BiKey<F, S> key) {
        return String.join(" ",  this.firstType.prettifyKey(key.getFirstKey()), this.secondType.prettifyKey(key.getSecondKey()));
    }
    
    @Override
    public BiKey<F, S> parseKey(String input) throws ParseException {
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            throw new ParseException(input, 0);
        }
        
        return BiKey.of(this.firstType.parse(parts[0]), this.secondType.parse(parts[1]));
    }
    
    @Nullable
    public String getHelp() {
        return String.join(" ",  this.firstType.getHelp(), this.secondType.getHelp());
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
    
    public void setSecondKeyDomain(Function<B, IKeyBuilder<S>> secondKeyFunc) {
        this.secondTypeLimit = secondKeyFunc.apply(this.secondKeyBuilder).create();
    }
    
    public IKeyType<F> getFirstKeyType() {
        return this.firstTypeLimit;
    }
    
    public IKeyType<S> getSecondKeyType() {
        return this.secondTypeLimit;
    }
}
